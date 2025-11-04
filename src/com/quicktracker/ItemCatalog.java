package com.quicktracker;

import java.util.List;

/*
Class: ItemCatalog
Purpose:
Acts as a bridge between GUI and DatabaseManager.
*/

public class ItemCatalog {
    private final DatabaseManager db;

    public ItemCatalog(DatabaseManager db) {
        this.db = db;
    }

    public String displayAllItems() {
        StringBuilder sb = new StringBuilder();
        List<Item> items = db.getAllItems();
        if (items.isEmpty()) return "⚠ No items found.";
        for (Item i : items) sb.append(i).append("\n");
        return sb.toString();
    }

    public String addItem(String name, String category, String supplier, int quantity, double price, String description) {
        return db.addItem(name, category, supplier, quantity, price, description)
                ? "✅ Item added successfully."
                : "⚠ Failed to add item.";
    }

    public String updateItem(int id, String newName) {
        return db.updateItem(id, newName)
                ? "✅ Item updated successfully."
                : "⚠ Item not found.";
    }

    public String removeItem(int id) {
        return db.removeItem(id)
                ? "✅ Item removed successfully."
                : "⚠ Item not found.";
    }

    public double calculateTotalValue() {
        return db.calculateTotalValue();
    }
}
