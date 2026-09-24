package com.example.smartpantrymanager;

public class UnitValue {
    private final double amount;
    private final String dimension;

    public UnitValue(double amount, String dimension) {
        this.amount = amount;
        this.dimension = dimension;
    }

    public double getAmount() { return amount; }
    public String getDimension() { return dimension; }
}
