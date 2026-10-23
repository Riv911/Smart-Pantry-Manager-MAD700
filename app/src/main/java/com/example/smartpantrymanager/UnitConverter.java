package com.example.smartpantrymanager;

import java.util.Locale;

public final class UnitConverter {
    private UnitConverter() { }

    public static UnitValue convertToBase(double quantity, String unit) {
        if (unit == null || !Double.isFinite(quantity) || quantity <= 0) return null;
        String u = unit.toLowerCase(Locale.US).trim();
        switch (u) {
            case "g": return new UnitValue(quantity, "mass");
            case "kg": return new UnitValue(quantity * 1000.0, "mass");
            case "ml": return new UnitValue(quantity, "volume");
            case "l": return new UnitValue(quantity * 1000.0, "volume");
            case "tsp": return new UnitValue(quantity * 4.92892, "volume");
            case "tbsp": return new UnitValue(quantity * 14.7868, "volume");
            case "cup":
            case "cups": return new UnitValue(quantity * 236.588, "volume");
            case "pc":
            case "pcs":
            case "piece":
            case "pieces": return new UnitValue(quantity, "count");
            default: return null;
        }
    }

    public static String normalizeUnitLabel(String unit) {
        if (unit == null) return "";
        String u = unit.toLowerCase(Locale.US).trim();
        switch (u) {
            case "pc":
            case "pcs":
            case "piece":
            case "pieces": return "pieces";
            case "cup":
            case "cups": return "cups";
            default: return u;
        }
    }
}
