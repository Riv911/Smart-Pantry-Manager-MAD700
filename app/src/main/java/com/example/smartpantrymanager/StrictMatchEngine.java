package com.example.smartpantrymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Implements all-or-nothing recipe matching, with unit conversion and quantity accounting. */
public final class StrictMatchEngine {
    private static final double EPSILON = 0.000001;
    private StrictMatchEngine() { }

    public static boolean qualifies(List<RecipeIngredient> requiredIngredients, List<PantryItem> pantryItems) {
        // A recipe without ingredient requirements must not be suggested by accident.
        if (requiredIngredients == null || requiredIngredients.isEmpty() || pantryItems == null) return false;
        Map<String, Double> available = new HashMap<>();
        for (PantryItem item : pantryItems) {
            if (item == null || !Double.isFinite(item.getQuantity()) || item.getQuantity() <= 0) continue;
            UnitValue value = UnitConverter.convertToBase(item.getQuantity(), item.getUnit());
            if (value == null) continue;
            String name = IngredientNormalizer.canonicalize(item.getName());
            if (name.isEmpty()) continue;
            String key = name + "|" + value.getDimension();
            available.put(key, available.getOrDefault(key, 0.0) + value.getAmount());
        }
        for (RecipeIngredient required : requiredIngredients) {
            if (required == null || !Double.isFinite(required.getRequiredQuantity()) || required.getRequiredQuantity() <= 0) return false;
            UnitValue needed = UnitConverter.convertToBase(required.getRequiredQuantity(), required.getUnit());
            if (needed == null) return false;
            String name = IngredientNormalizer.canonicalize(required.getIngredientName());
            if (name.isEmpty()) return false;
            String key = name + "|" + needed.getDimension();
            double remaining = available.getOrDefault(key, 0.0) - needed.getAmount();
            if (remaining < -EPSILON) return false;
            // If a recipe lists an ingredient in two steps, both amounts are required.
            available.put(key, Math.max(0, remaining));
        }
        return true;
    }
}
