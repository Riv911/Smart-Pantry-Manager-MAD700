package com.example.smartpantrymanager;

public class RecipeIngredient {
    private long id;
    private long recipeId;
    private String ingredientName;
    private String canonicalName;
    private double requiredQuantity;
    private String unit;

    public RecipeIngredient(long id, long recipeId, String ingredientName, String canonicalName, double requiredQuantity, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.canonicalName = canonicalName;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }

    public long getId() { return id; }
    public long getRecipeId() { return recipeId; }
    public String getIngredientName() { return ingredientName; }
    public String getCanonicalName() { return canonicalName; }
    public double getRequiredQuantity() { return requiredQuantity; }
    public String getUnit() { return unit; }
}
