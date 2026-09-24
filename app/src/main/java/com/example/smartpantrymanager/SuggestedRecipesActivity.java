package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends Activity {
    private DatabaseHelper databaseHelper;
    private RecipeAdapter adapter;
    private final List<Recipe> recipes = new ArrayList<>();
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        databaseHelper = new DatabaseHelper(this);
        emptyMessage = findViewById(R.id.emptyRecipeMessage);
        ListView listView = findViewById(R.id.recipeListView);
        adapter = new RecipeAdapter(this, recipes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", id);
            startActivity(intent);
        });
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        Button pantry = findViewById(R.id.navPantry);
        Button suggestions = findViewById(R.id.navSuggestions);
        Button settings = findViewById(R.id.navSettings);
        pantry.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        suggestions.setOnClickListener(v -> loadSuggestions());
        settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions();
    }

    private void loadSuggestions() {
        recipes.clear();
        recipes.addAll(new RecipeMatcher(databaseHelper).getStrictMatches());
        adapter.notifyDataSetChanged();
        boolean empty = recipes.isEmpty();
        emptyMessage.setVisibility(empty ? View.VISIBLE : View.GONE);
        findViewById(R.id.recipeListView).setVisibility(empty ? View.GONE : View.VISIBLE);
    }
}
