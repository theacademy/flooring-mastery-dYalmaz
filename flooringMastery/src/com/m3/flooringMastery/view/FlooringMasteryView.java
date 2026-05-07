package com.m3.flooringMastery.view;

import com.m3.flooringMastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int getMenuSelection() {
        io.displayMessage("1. Display Orders");
        io.displayMessage("2. Add an Order");
        io.displayMessage("3. Edit an Order");
        io.displayMessage("4. Remove an Order");
        io.displayMessage("5. Export All Data");
        io.displayMessage("6. Quit");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public void displayUserMenu() {
        System.out.println("1. Display Orders");
        System.out.println("2. Add an Order");
        System.out.println("3. Edit an Order");
        System.out.println("4. Remove an Order");
        System.out.println("5. Export All Data");
        System.out.println("6. Quit");
    }

    public LocalDate getDateFromUser() {
        return io.readDate("Please enter a date (MM/DD/YYYY): ");
    }

    public Order getOrderFromUser() {
        return null;
    }

    public Order editOrderMenu(Order order) {
        return null;
    }

    public void removeOrder(Order order) {
    }

    public void displayOrders(List<Order> orders) {
    }

    public void displayOrder(Order order) {
    }

    public void displayOrderSummary(Order order) {
    }

    public boolean promptForSave(String message) {
        return false;
    }

    public void displayProducts(List<String> products) {
    }

    public void displayBanner() {
    }

    public void displayError(String msg) {
    }

    public void exportData(List<Order> orders) {
    }

    public String getUserInput(String prompt) {
        return io.readString(prompt);
    }




    public void displayUnknownCommandBanner() {
        io.displayMessage("Unknown Command");
    }




    }
