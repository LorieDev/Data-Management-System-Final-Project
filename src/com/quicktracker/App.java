package com.quicktracker;

/*
Name: Lorvezline Donassaint
Course: CEN3024C
Date: 2025-11-04

Class: App
Purpose:
- Main entry point for QuickTracker Database Edition.
- Launches the GUI.
*/

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(QuickTrackerGUI::new);
    }
}
