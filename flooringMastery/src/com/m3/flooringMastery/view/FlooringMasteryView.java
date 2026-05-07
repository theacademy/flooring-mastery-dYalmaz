package com.m3.flooringMastery.view;

import com.m3.flooringMastery.model.Order;

import java.math.BigDecimal;
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
        String customerName = io.readString("Please enter the customer's name: ");
        String state = io.readString("Please enter the state: ");
        String productType = io.readString("Please enter the product type: ");
        BigDecimal area = io.readBigDecimal("Please enter the area (in square feet): ");

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setState(state);
        order.setProductType(productType);
        order.setArea(area);

        return order;
    }

    public Order editOrderMenu(Order order) {
        return null;
    }

    public void removeOrder() {
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
        io.displayMessage("=== ERROR ===");
        io.displayMessage(msg);
    }

    public void exportData(List<Order> orders) {
    }

    public String getUserInput(String prompt) {
        return io.readString(prompt);
    }




    public void displayUnknownCommandBanner() {
        io.displayMessage("Unknown Command");
    }


    public void displayAddOrderBanner() {
        io.displayMessage("=== Add an Order ===");
    }

    public void displayAddSuccessBanner() {
        io.displayMessage("Order successfully added. Please hit enter to continue.");
    }
}
