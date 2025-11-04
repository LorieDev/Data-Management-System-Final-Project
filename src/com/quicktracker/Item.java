package com.quicktracker;

/*
Class: Item
Purpose:
- Represents one inventory record from the database.
*/
public class Item {
    private int id;
    private String name;
    private String category;
    private String supplier;
    private int quantity;
    private double price;
    private String description;

    public Item(int id, String name, String category, String supplier, int quantity, double price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.supplier = supplier;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSupplier() { return supplier; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | %s | Qty: %d | $%.2f | %s",
                id, name, category, supplier, quantity, price, description);
    }
}
