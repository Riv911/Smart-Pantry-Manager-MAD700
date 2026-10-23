package com.example.smartpantrymanager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class IngredientNormalizer {
    private IngredientNormalizer() { }

    public static String canonicalize(String input) {
        if (input == null) return "";
        String value = input.toLowerCase(Locale.US).trim()
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ");

        Map<String, String> aliases = new HashMap<>();
        aliases.put("tomatoes", "tomato");
        aliases.put("tomato", "tomato");
        aliases.put("eggs", "egg");
        aliases.put("egg", "egg");
        aliases.put("onions", "onion");
        aliases.put("onion", "onion");
        aliases.put("potatoes", "potato");
        aliases.put("potato", "potato");
        aliases.put("garlic cloves", "garlic");
        aliases.put("garlic clove", "garlic");
        aliases.put("garlic", "garlic");
        aliases.put("bell peppers", "bell pepper");
        aliases.put("bell pepper", "bell pepper");
        aliases.put("mushrooms", "mushroom");
        aliases.put("mushroom", "mushroom");
        aliases.put("bananas", "banana");
        aliases.put("banana", "banana");
        aliases.put("apples", "apple");
        aliases.put("apple", "apple");
        aliases.put("chickpeas", "chickpea");
        aliases.put("chickpea", "chickpea");
        aliases.put("beans", "bean");
        aliases.put("bean", "bean");
        aliases.put("slices bread", "bread");
        aliases.put("bread slices", "bread");
        aliases.put("bread", "bread");
        aliases.put("pastas", "pasta");
        aliases.put("pasta", "pasta");
        aliases.put("oils", "oil");
        aliases.put("oil", "oil");
        aliases.put("butters", "butter");
        aliases.put("butter", "butter");
        aliases.put("cheeses", "cheese");
        aliases.put("cheese", "cheese");
        aliases.put("milks", "milk");
        aliases.put("milk", "milk");
        aliases.put("flours", "flour");
        aliases.put("flour", "flour");
        aliases.put("rice", "rice");
        aliases.put("rices", "rice");
        aliases.put("soy sauces", "soy sauce");
        aliases.put("soy sauce", "soy sauce");
        aliases.put("tuna", "tuna");
        aliases.put("chicken", "chicken");
        aliases.put("oats", "oat");
        aliases.put("oat", "oat");

        if (aliases.containsKey(value)) return aliases.get(value);
        if (value.endsWith("ies")) return value.substring(0, value.length() - 3) + "y";
        if (value.endsWith("es")) return value.substring(0, value.length() - 2);
        if (value.endsWith("s") && !value.endsWith("ss") && !value.endsWith("us") && !value.endsWith("ous")) return value.substring(0, value.length() - 1);
        return value;
    }
}
