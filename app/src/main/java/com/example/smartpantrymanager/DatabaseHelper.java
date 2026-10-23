package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "canonical_name TEXT NOT NULL," +
                "quantity REAL NOT NULL," +
                "unit TEXT NOT NULL," +
                "expiry_date TEXT)");

        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "method TEXT NOT NULL)");

        db.execSQL("CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "recipe_id INTEGER NOT NULL," +
                "ingredient_name TEXT NOT NULL," +
                "canonical_name TEXT NOT NULL," +
                "required_quantity REAL NOT NULL," +
                "unit TEXT NOT NULL," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Future schema migrations belong here.
    }

    public long insertPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("canonical_name", item.getCanonicalName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", item.getExpiryDate());
        return db.insert("pantry_items", null, values);
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query("pantry_items", null, null, null, null, null, "name COLLATE NOCASE ASC")) {
            while (cursor.moveToNext()) {
                items.add(pantryFromCursor(cursor));
            }
        }
        return items;
    }

    public PantryItem getPantryItem(long id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query("pantry_items", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor.moveToFirst()) return pantryFromCursor(cursor);
        }
        return null;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("canonical_name", item.getCanonicalName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", item.getExpiryDate());
        return db.update("pantry_items", values, "id = ?", new String[]{String.valueOf(item.getId())});
    }

    public int deletePantryItem(long id) {
        return getWritableDatabase().delete("pantry_items", "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query("recipes", null, null, null, null, null, "name COLLATE NOCASE ASC")) {
            while (cursor.moveToNext()) {
                recipes.add(new Recipe(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("method"))));
            }
        }
        return recipes;
    }

    public Recipe getRecipe(long id) {
        try (Cursor cursor = getReadableDatabase().query("recipes", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                return new Recipe(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("method")));
            }
        }
        return null;
    }

    public List<RecipeIngredient> getRecipeIngredients(long recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(
                "recipe_ingredients", null, "recipe_id = ?", new String[]{String.valueOf(recipeId)}, null, null, "id ASC")) {
            while (cursor.moveToNext()) {
                ingredients.add(new RecipeIngredient(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("recipe_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ingredient_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("canonical_name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("required_quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("unit"))));
            }
        }
        return ingredients;
    }

    public List<String> getExpiringSoonItems(int days) {
        List<String> results = new ArrayList<>();
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
        java.util.Calendar today = java.util.Calendar.getInstance();
        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
        today.set(java.util.Calendar.MINUTE, 0);
        today.set(java.util.Calendar.SECOND, 0);
        today.set(java.util.Calendar.MILLISECOND, 0);
        java.util.Calendar lastDay = (java.util.Calendar) today.clone();
        lastDay.add(java.util.Calendar.DAY_OF_YEAR, days);
        long maxTime = lastDay.getTimeInMillis();

        for (PantryItem item : getAllPantryItems()) {
            if (item.getExpiryDate() == null || item.getExpiryDate().trim().isEmpty()) continue;
            try {
                java.util.Date date = fmt.parse(item.getExpiryDate());
                if (date != null && date.getTime() >= today.getTimeInMillis() && date.getTime() <= maxTime) {
                    results.add(item.getName() + " (" + item.getExpiryDate() + ")");
                }
            } catch (Exception ignored) { }
        }
        return results;
    }

    private PantryItem pantryFromCursor(Cursor cursor) {
        return new PantryItem(
                cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("canonical_name")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                cursor.getString(cursor.getColumnIndexOrThrow("expiry_date")));
    }

    private void insertRecipe(SQLiteDatabase db, String name, String method, String[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("method", method);
        long recipeId = db.insert("recipes", null, recipeValues);

        for (String[] ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put("recipe_id", recipeId);
            values.put("ingredient_name", ingredient[0]);
            values.put("canonical_name", IngredientNormalizer.canonicalize(ingredient[0]));
            values.put("required_quantity", Double.parseDouble(ingredient[1]));
            values.put("unit", ingredient[2]);
            db.insert("recipe_ingredients", null, values);
        }
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Banana Pancakes", "Mash the bananas. Whisk with eggs, flour and milk. Cook small pancakes in a lightly oiled pan until golden on both sides.", new String[][]{
                {"bananas", "2", "pieces"}, {"eggs", "2", "pieces"}, {"flour", "100", "g"}, {"milk", "100", "ml"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Omelette", "Beat the eggs. Saute onion and tomato in oil, add eggs and cheese, then fold when set.", new String[][]{
                {"eggs", "2", "pieces"}, {"onion", "0.5", "pieces"}, {"tomato", "1", "pieces"}, {"cheese", "30", "g"}, {"oil", "1", "tsp"}});
        insertRecipe(db, "Egg Fried Rice", "Heat oil. Cook onion, add eggs and scramble. Stir in cooked rice and soy sauce and cook until hot.", new String[][]{
                {"eggs", "2", "pieces"}, {"onion", "0.5", "pieces"}, {"rice", "300", "g"}, {"soy sauce", "1", "tbsp"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Vegetable Fried Rice", "Stir-fry onion and bell pepper in oil. Add cooked rice and soy sauce and toss until hot.", new String[][]{
                {"onion", "1", "pieces"}, {"bell pepper", "1", "pieces"}, {"rice", "300", "g"}, {"soy sauce", "1", "tbsp"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Tomato Pasta", "Boil pasta. Saute onion, tomato and garlic in oil, then toss with the cooked pasta.", new String[][]{
                {"pasta", "200", "g"}, {"tomatoes", "2", "pieces"}, {"onion", "1", "pieces"}, {"garlic", "2", "pieces"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Garlic Butter Pasta", "Boil pasta. Melt butter, saute garlic, then toss with pasta and a splash of pasta water.", new String[][]{
                {"pasta", "200", "g"}, {"butter", "30", "g"}, {"garlic", "2", "pieces"}, {"cheese", "20", "g"}});
        insertRecipe(db, "Grilled Cheese", "Butter both slices of bread, add cheese and toast in a pan until golden and melted.", new String[][]{
                {"bread", "2", "pieces"}, {"cheese", "60", "g"}, {"butter", "10", "g"}});
        insertRecipe(db, "French Toast", "Whisk eggs with milk. Dip bread, then cook in butter until golden on both sides.", new String[][]{
                {"bread", "2", "pieces"}, {"eggs", "2", "pieces"}, {"milk", "80", "ml"}, {"butter", "10", "g"}, {"sugar", "10", "g"}});
        insertRecipe(db, "Potato Hash", "Cook diced potato in oil until almost tender. Add onion and bell pepper and cook until browned.", new String[][]{
                {"potato", "300", "g"}, {"onion", "1", "pieces"}, {"bell pepper", "1", "pieces"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Chicken Stir-Fry", "Slice chicken. Cook in oil, add onion and bell pepper, then stir in soy sauce and cook through.", new String[][]{
                {"chicken", "250", "g"}, {"bell pepper", "1", "pieces"}, {"onion", "1", "pieces"}, {"soy sauce", "2", "tbsp"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Chickpea Curry", "Saute onion and garlic in oil. Add chickpeas and a splash of water, then simmer until thick.", new String[][]{
                {"chickpeas", "250", "g"}, {"onion", "1", "pieces"}, {"garlic", "2", "pieces"}, {"tomato", "2", "pieces"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Bean Wrap", "Warm the beans with tomato and onion. Spoon into bread, add cheese and fold into a wrap.", new String[][]{
                {"beans", "200", "g"}, {"tomato", "1", "pieces"}, {"onion", "0.5", "pieces"}, {"cheese", "40", "g"}, {"bread", "2", "pieces"}});
        insertRecipe(db, "Tuna Pasta Salad", "Boil pasta and cool. Mix with tuna, tomato, onion and a spoon of oil.", new String[][]{
                {"pasta", "200", "g"}, {"tuna", "150", "g"}, {"tomato", "1", "pieces"}, {"onion", "0.5", "pieces"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Tomato Soup", "Saute onion and garlic, add tomatoes and water, simmer, then blend or mash to a smooth soup.", new String[][]{
                {"tomatoes", "4", "pieces"}, {"onion", "1", "pieces"}, {"garlic", "2", "pieces"}, {"butter", "15", "g"}});
        insertRecipe(db, "Shakshuka", "Saute onion and tomato with garlic. Crack in eggs, cover and cook until the eggs are set.", new String[][]{
                {"tomato", "3", "pieces"}, {"onion", "1", "pieces"}, {"garlic", "2", "pieces"}, {"eggs", "3", "pieces"}, {"oil", "1", "tbsp"}});
        insertRecipe(db, "Creamy Mushroom Pasta", "Boil pasta. Saute mushrooms and garlic in butter, add milk and cheese, then toss with pasta.", new String[][]{
                {"pasta", "200", "g"}, {"mushrooms", "150", "g"}, {"garlic", "2", "pieces"}, {"milk", "150", "ml"}, {"butter", "20", "g"}, {"cheese", "30", "g"}});
        insertRecipe(db, "Apple Oatmeal", "Cook oats with milk until creamy. Stir in diced apple and serve warm.", new String[][]{
                {"oats", "60", "g"}, {"milk", "250", "ml"}, {"apple", "1", "pieces"}, {"sugar", "10", "g"}});
        insertRecipe(db, "Simple Tomato Toast", "Toast bread. Top with sliced tomato, cheese and a little oil, then warm until the cheese softens.", new String[][]{
                {"bread", "2", "pieces"}, {"tomato", "1", "pieces"}, {"cheese", "40", "g"}, {"oil", "1", "tsp"}});
    }
}
