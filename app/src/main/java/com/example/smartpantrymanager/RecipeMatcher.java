package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatcher {
    private final DatabaseHelper databaseHelper;

    public RecipeMatcher(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public List<Recipe> getStrictMatches() {
        List<Recipe> matches = new ArrayList<>();
        List<PantryItem> pantry = databaseHelper.getAllPantryItems();
        for (Recipe recipe : databaseHelper.getAllRecipes()) {
            if (StrictMatchEngine.qualifies(databaseHelper.getRecipeIngredients(recipe.getId()), pantry)) {
                matches.add(recipe);
            }
        }
        return matches;
    }
}
