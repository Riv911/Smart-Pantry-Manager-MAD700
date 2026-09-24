package com.example.smartpantrymanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class RecipeDetailActivity extends Activity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        databaseHelper = new DatabaseHelper(this);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        loadRecipe();
    }

    private void loadRecipe() {
        long id = getIntent().getLongExtra("recipe_id", -1);
        Recipe recipe = databaseHelper.getRecipe(id);
        if (recipe == null) return;

        ((TextView)findViewById(R.id.recipeTitle)).setText(recipe.getName());
        LinearLayout ingredientContainer = findViewById(R.id.ingredientContainer);
        ingredientContainer.removeAllViews();
        List<RecipeIngredient> ingredients = databaseHelper.getRecipeIngredients(id);
        for (RecipeIngredient ingredient : ingredients) {
            TextView row = new TextView(this);
            row.setText(String.format(Locale.US, "✓ %.2f %s %s", ingredient.getRequiredQuantity(), UnitConverter.normalizeUnitLabel(ingredient.getUnit()), ingredient.getIngredientName()));
            row.setTextSize(16);
            row.setTextColor(getResources().getColor(R.color.text_primary));
            row.setPadding(0, 8, 0, 8);
            ingredientContainer.addView(row);
        }
        ((TextView)findViewById(R.id.methodText)).setText(recipe.getMethod());
    }
}
