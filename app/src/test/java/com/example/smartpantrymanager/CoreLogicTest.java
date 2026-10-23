package com.example.smartpantrymanager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreLogicTest {
    public static void main(String[] args) {
        check("tomato".equals(IngredientNormalizer.canonicalize("Tomatoes")), "plural normalisation");
        check("egg".equals(IngredientNormalizer.canonicalize(" eggs ")), "case/space normalisation");
        UnitValue kg = UnitConverter.convertToBase(1, "kg");
        check(Math.abs(kg.getAmount() - 1000) < 0.000001 && kg.getDimension().equals("mass"), "kg conversion");

        List<RecipeIngredient> omelette = Arrays.asList(
                new RecipeIngredient(1, 1, "eggs", "egg", 2, "pieces"),
                new RecipeIngredient(2, 1, "tomatoes", "tomato", 1, "pieces"),
                new RecipeIngredient(3, 1, "cheese", "cheese", 30, "g"));

        List<PantryItem> enough = Arrays.asList(
                new PantryItem("Egg", "egg", 2, "pieces", ""),
                new PantryItem("Tomatoes", "tomato", 1, "pieces", ""),
                new PantryItem("Cheese", "cheese", 40, "g", ""));
        check(StrictMatchEngine.qualifies(omelette, enough), "complete recipe qualifies");

        List<PantryItem> missing = Arrays.asList(
                new PantryItem("Egg", "egg", 2, "pieces", ""),
                new PantryItem("Tomatoes", "tomato", 1, "pieces", ""));
        check(!StrictMatchEngine.qualifies(omelette, missing), "missing ingredient rejected");

        List<PantryItem> insufficient = Arrays.asList(
                new PantryItem("Egg", "egg", 2, "pieces", ""),
                new PantryItem("Tomatoes", "tomato", 1, "pieces", ""),
                new PantryItem("Cheese", "cheese", 20, "g", ""));
        check(!StrictMatchEngine.qualifies(omelette, insufficient), "insufficient quantity rejected");

        List<PantryItem> mixedUnits = Collections.singletonList(
                new PantryItem("Flour", "flour", 1.5, "kg", ""));
        List<RecipeIngredient> flourNeed = Collections.singletonList(
                new RecipeIngredient(4, 2, "flour", "flour", 1200, "g"));
        check(StrictMatchEngine.qualifies(flourNeed, mixedUnits), "kg/g matching");

        check(!StrictMatchEngine.qualifies(Collections.emptyList(), enough), "empty recipe rejected");
        List<RecipeIngredient> twice = Arrays.asList(
                new RecipeIngredient(6, 2, "milk", "milk", 150, "ml"),
                new RecipeIngredient(7, 2, "milk", "milk", 150, "ml"));
        check(!StrictMatchEngine.qualifies(twice, Collections.singletonList(
                new PantryItem("Milk", "milk", 250, "ml", ""))), "repeated requirement aggregated");
        check(StrictMatchEngine.qualifies(twice, Arrays.asList(
                new PantryItem("Milk", "milk", 200, "ml", ""),
                new PantryItem("milk", "milk", 150, "ml", ""))), "duplicate pantry records sum");
        check(!StrictMatchEngine.qualifies(Collections.singletonList(
                new RecipeIngredient(8, 3, "cheese", "cheese", 100, "g")),
                Collections.singletonList(new PantryItem("Cheese", "cheese", 150, "ml", ""))), "incompatible units rejected");
        check(!StrictMatchEngine.qualifies(omelette, Arrays.asList(
                new PantryItem("Egg", "egg", 2, "pieces", ""),
                new PantryItem("Tomatoes", "tomato", 1, "pieces", ""),
                new PantryItem("Cheese", "cheese", Double.NaN, "g", ""))), "non-finite pantry amount rejected");
        check(UnitConverter.convertToBase(Double.POSITIVE_INFINITY, "g") == null, "non-finite conversion rejected");
        check("hummus".equals(IngredientNormalizer.canonicalize("Hummus")), "do not singularize hummus");

        System.out.println("All core logic checks passed: normalisation, unit conversion, strict inclusion, missing ingredient rejection and insufficient quantity rejection.");
    }

    private static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError("Failed: " + name);
    }
}
