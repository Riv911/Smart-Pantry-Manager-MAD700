package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RecipeAdapter extends BaseAdapter {
    private final Context context;
    private final List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override public int getCount() { return recipes.size(); }
    @Override public Recipe getItem(int position) { return recipes.get(position); }
    @Override public long getItemId(int position) { return recipes.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        Recipe recipe = getItem(position);
        TextView name = view.findViewById(R.id.recipeName);
        TextView detail = view.findViewById(R.id.recipeDetail);
        name.setText(recipe.getName());
        detail.setText("All required ingredients and quantities are available");
        return view;
    }
}
