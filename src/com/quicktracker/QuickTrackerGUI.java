package com.quicktracker;

import javax.swing.*;
import java.awt.*;

/*
 * Class: QuickTrackerGUI
 * Purpose:
 *  - Graphical front-end for QuickTracker Database Edition.
 *  - Allows user to connect to MySQL database and perform full CRUD.
 *  - Includes data validation and helpful user messages.
 */
public class QuickTrackerGUI extends JFrame {

    // === GUI Components ===
    private final JTextArea displayArea = new JTextArea(15, 60);
    private final JTextField idField = new JTextField(5);
    private final JTextField nameField = new JTextField(10);
    private final JTextField categoryField = new JTextField(10);
    private final JTextField supplierField = new JTextField(10);
    private final JTextField qtyField = new JTextField(5);
    private final JTextField priceField = new JTextField(7);
    private final JTextField descField = new JTextField(12);

    // === Database and Catalog managers ===
    private DatabaseManager db;
    private ItemCatalog catalog;

    // === Constructor ===
    public QuickTrackerGUI() {
        super("QuickTracker – Database Management System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        setSize(950,600);
        setLocationRelativeTo(null);

        // ===== Header =====
        JLabel title = new JLabel("📦 QuickTracker Database System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(128,0,128));
        add(title, BorderLayout.NORTH);

        // ===== Display Area =====
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // ===== Input Panel =====
        JPanel input = new JPanel(new GridLayout(2,7,5,5));
        input.add(new JLabel("ID:")); input.add(idField);
        input.add(new JLabel("Name:")); input.add(nameField);
        input.add(new JLabel("Category:")); input.add(categoryField);
        input.add(new JLabel("Supplier:")); input.add(supplierField);
        input.add(new JLabel("Qty:")); input.add(qtyField);
        input.add(new JLabel("Price:")); input.add(priceField);
        input.add(new JLabel("Desc:")); input.add(descField);

        // ===== Button Panel =====
        JPanel buttons = new JPanel(new GridLayout(2,4,8,8));
        JButton connectBtn = new JButton("Connect DB");
        JButton displayBtn = new JButton("Display");
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton removeBtn = new JButton("Remove");
        JButton totalBtn = new JButton("Total Value");
        JButton clearBtn = new JButton("Clear");
        JButton exitBtn = new JButton("Exit");

        buttons.add(connectBtn); buttons.add(displayBtn);
        buttons.add(addBtn); buttons.add(updateBtn);
        buttons.add(removeBtn); buttons.add(totalBtn);
        buttons.add(clearBtn); buttons.add(exitBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(input, BorderLayout.NORTH);
        bottom.add(buttons, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        // ===== Button Actions =====
        connectBtn.addActionListener(e -> connectDatabase());
        displayBtn.addActionListener(e -> displayItems());
        addBtn.addActionListener(e -> addItem());
        updateBtn.addActionListener(e -> updateItem());
        removeBtn.addActionListener(e -> removeItem());
        totalBtn.addActionListener(e -> totalValue());
        clearBtn.addActionListener(e -> displayArea.setText(""));
        exitBtn.addActionListener(e -> System.exit(0));

        // ===== Show GUI =====
        setVisible(true);
    }

    // === Connect to MySQL ===
    private void connectDatabase() {
        JTextField hostField = new JTextField("localhost:3306");
        JTextField dbField = new JTextField("quicktrackerdb");
        JTextField userField = new JTextField("root");
        JPasswordField passField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(4,2));
        panel.add(new JLabel("Host:")); panel.add(hostField);
        panel.add(new JLabel("Database:")); panel.add(dbField);
        panel.add(new JLabel("Username:")); panel.add(userField);
        panel.add(new JLabel("Password:")); panel.add(passField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Database Connection Info",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            db = new DatabaseManager();
            boolean connected = db.connect(
                    hostField.getText(),
                    dbField.getText(),
                    userField.getText(),
                    new String(passField.getPassword())
            );

            if (connected) {
                catalog = new ItemCatalog(db);
                displayArea.setText("✅ Connected to database successfully!\nClick 'Display' to view items.\n");
            } else {
                displayArea.setText("❌ Connection failed. Please check credentials.\n");
            }
        }
    }

    // === Display all items ===
    private void displayItems() {
        if (catalog == null) { warnNotConnected(); return; }
        displayArea.setText(catalog.displayAllItems());
    }

    // === Add new item ===
    private void addItem() {
        if (catalog == null) { warnNotConnected(); return; }

        // Check for empty fields
        if (nameField.getText().trim().isEmpty() ||
                categoryField.getText().trim().isEmpty() ||
                supplierField.getText().trim().isEmpty() ||
                qtyField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty() ||
                descField.getText().trim().isEmpty()) {
            displayArea.setText("⚠ Please fill in all fields (Name, Category, Supplier, Qty, Price, Description).");
            return;
        }

        try {
            int qty = InputValidator.parseInt(qtyField.getText());
            double price = InputValidator.parseDouble(priceField.getText());

            String msg = catalog.addItem(
                    nameField.getText(), categoryField.getText(),
                    supplierField.getText(), qty, price, descField.getText()
            );
            displayArea.setText(msg + "\n\n" + catalog.displayAllItems());
        } catch (IllegalArgumentException ex) {
            displayArea.setText("⚠ " + ex.getMessage());
        }
    }

    // === Update item ===
    private void updateItem() {
        if (catalog == null) { warnNotConnected(); return; }

        if (idField.getText().trim().isEmpty()) {
            displayArea.setText("⚠ Please enter an ID before updating an item.");
            return;
        }

        try {
            int id = InputValidator.parseInt(idField.getText());
            String newName = JOptionPane.showInputDialog(this, "Enter new name:");
            if (newName != null && !newName.isBlank()) {
                String msg = catalog.updateItem(id, newName);
                displayArea.setText(msg + "\n\n" + catalog.displayAllItems());
            } else {
                displayArea.setText("⚠ Update cancelled or invalid name input.");
            }
        } catch (Exception ex) {
            displayArea.setText("⚠ " + ex.getMessage());
        }
    }

    // === Remove item ===
    private void removeItem() {
        if (catalog == null) { warnNotConnected(); return; }

        if (idField.getText().trim().isEmpty()) {
            displayArea.setText("⚠ Please enter an ID before removing an item.");
            return;
        }

        try {
            int id = InputValidator.parseInt(idField.getText());
            String msg = catalog.removeItem(id);
            displayArea.setText(msg + "\n\n" + catalog.displayAllItems());
        } catch (Exception ex) {
            displayArea.setText("⚠ " + ex.getMessage());
        }
    }

    // === Calculate total inventory value ===
    private void totalValue() {
        if (catalog == null) { warnNotConnected(); return; }
        double total = catalog.calculateTotalValue();
        displayArea.setText("💰 Total Inventory Value: $" + String.format("%.2f", total));
    }

    // === Warn if database not connected ===
    private void warnNotConnected() {
        JOptionPane.showMessageDialog(this,
                "⚠ Please connect to the database first.",
                "Not Connected",
                JOptionPane.WARNING_MESSAGE);
    }
}
