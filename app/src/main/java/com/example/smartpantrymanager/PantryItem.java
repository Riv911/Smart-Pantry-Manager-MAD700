package com.example.smartpantrymanager;

public class PantryItem {
    private long id;
    private String name;
    private String canonicalName;
    private double quantity;
    private String unit;
    private String expiryDate;

    public PantryItem(long id, String name, String canonicalName, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.canonicalName = canonicalName;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public PantryItem(String name, String canonicalName, double quantity, String unit, String expiryDate) {
        this(-1, name, canonicalName, quantity, unit, expiryDate);
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getCanonicalName() { return canonicalName; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiryDate() { return expiryDate; }
}
