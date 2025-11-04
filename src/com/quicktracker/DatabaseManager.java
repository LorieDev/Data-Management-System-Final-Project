package com.quicktracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Class: DatabaseManager
Purpose:
- Handles all database connections and CRUD operations.
- Works with MySQL backend.
*/

public class DatabaseManager {
    private Connection connection;

    // Connect to MySQL database
    public boolean connect(String host, String database, String user, String password) {
        try {
            String url = "jdbc:mysql://" + host + "/" + database;
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {
            System.out.println("⚠ Database connection failed: " + e.getMessage());
            return false;
        }
    }

    // Retrieve all items
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                items.add(new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("supplier"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.out.println("⚠ Error fetching items: " + e.getMessage());
        }
        return items;
    }

    // Add a new item
    public boolean addItem(String name, String category, String supplier, int quantity, double price, String description) {
        String query = "INSERT INTO items (name, category, supplier, quantity, price, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setString(3, supplier);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, price);
            stmt.setString(6, description);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("⚠ Error adding item: " + e.getMessage());
            return false;
        }
    }

    // Update an item
    public boolean updateItem(int id, String newName) {
        String query = "UPDATE items SET name=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("⚠ Error updating item: " + e.getMessage());
            return false;
        }
    }

    // Remove an item
    public boolean removeItem(int id) {
        String query = "DELETE FROM items WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("⚠ Error removing item: " + e.getMessage());
            return false;
        }
    }

    // Calculate total inventory value
    public double calculateTotalValue() {
        String query = "SELECT SUM(quantity * price) AS total FROM items";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("⚠ Error calculating total: " + e.getMessage());
        }
        return 0.0;
    }
}
