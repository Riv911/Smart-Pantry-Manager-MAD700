package com.example.smartpantrymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StrictMatchEngine {
    private StrictMatchEngine() { }

    public static boolean qualifies(List<RecipeIngredient> requiredIngredients, List<PantryItem> pantryItems) {
        Map<String, Double> available = new HashMap<>();
        for (PantryItem item : pantryItems) {
            UnitValue value = UnitConverter.convertToBase(item.getQuantity(), item.getUnit());
            if (value == null) continue;
            String key = item.getCanonicalName() + "|" + value.getDimension();
            available.put(key, available.getOrDefault(key, 0.0) + value.getAmount());
        }

        for (RecipeIngredient required : requiredIngredients) {
            UnitValue requiredValue = UnitConverter.convertToBase(required.getRequiredQuantity(), required.getUnit());
            if (requiredValue == null) return false;
            String key = required.getCanonicalName() + "|" + requiredValue.getDimension();
            double pantryAmount = available.getOrDefault(key, 0.0);
            if (pantryAmount + 0.000001 < requiredValue.getAmount()) return false;
        }
        return true;
    }
}
