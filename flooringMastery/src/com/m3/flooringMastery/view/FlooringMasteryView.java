package com.m3.flooringMastery.view;

import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public LocalDate getDateFromUser() {
        return io.readDate("Please enter a date (MM/DD/YYYY): ");
    }

    public Order getOrderFromUser(List<Product> products) {

        LocalDate date = io.readDate("Please enter the order date (MM/DD/YYYY): ");

        String customerName = io.readString("Please enter the customer's name: ");
        while (!nameValidation(customerName)) {
            io.displayMessage("Invalid name. Please try again.");
            customerName = io.readString("Please enter the customer's name: ");
        }

        String state = io.readString("Please enter the state abbreviation: ");

        while (state.trim().isEmpty() || state.length() != 2) {
            io.displayMessage("Please enter a valid 2-letter state code (e.g. CA, TX).");
            state = io.readString("Please enter the state abbreviation: ");
        }

        state = state.toUpperCase();
        showProducts(products);

        Product selectedProduct = null;

        while (selectedProduct == null) {
            String productChoice = io.readString("Please select a product: ");

            selectedProduct = products.stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(productChoice))
                    .findFirst()
                    .orElse(null);

            if (selectedProduct == null) {
                io.displayMessage("Invalid product choice. Please try again.");
            }
        }

        BigDecimal area = io.readBigDecimal("Please enter the area (in sq ft): ");
        while (area.compareTo(BigDecimal.valueOf(100)) < 0) {
            io.displayMessage("Area must be at least 100 sq ft.");
            area = io.readBigDecimal("Please enter the area (in sq ft): ");
        }

        // ✔ VIEW ONLY builds the object (NO CALCULATIONS)
        Order order = new Order();
        order.setOrderDate(date);
        order.setCustomerName(customerName);
        order.setState(state);
        order.setProductType(selectedProduct.getProductType());
        order.setArea(area);

        return order;
    }

    private boolean nameValidation(String name) {
        // Regex to allow letters, numbers, spaces, and common punctuation
        String regex = "^[a-zA-Z0-9.,\\s]+$";
        return name.matches(regex);
    }

    public void showProducts(List<Product> products) {

        displayMessage("Available Products:");

        for (Product product : products) {
            displayMessage("- " + product.getProductType()
                    + " (Price: £" + product.getCostPerSquareFoot()
                    + "/sq ft, Labor: $" + product.getLaborCostPerSquareFoot()
                    + "/sq ft)");
        }
    }


    public Order editOrderMenu(Order order) {
        String customerName = io.readString("Enter new customer name (" + order.getCustomerName() + "): ");
        String state = io.readString("Enter new state (" + order.getState() + "): ");
        String productType = io.readString("Enter new product type (" + order.getProductType() + "): ");
        BigDecimal area = io.readBigDecimal("Enter new area (" + order.getArea() + "): ");

        if (!customerName.trim().isEmpty()) {
            order.setCustomerName(customerName);
        }
        if (!state.trim().isEmpty()) {
            order.setState(state);
        }
        if (!productType.trim().isEmpty()) {
            order.setProductType(productType);
        }
        if (area.compareTo(BigDecimal.ZERO) > 0) {
            order.setArea(area);
        }

        return order;
    }

    public void removeOrder() {
        int orderNumber = io.readInt("Please enter the order number to remove: ");
        // Implement logic to remove the order based on the order number
    }

    public void displayOrders(List<Order> orders) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        for (Order order : orders) {

            String orderInfo = String.format(
                    "Order #%d: %s, %s, %s, %.2f sq ft (%s)",
                    order.getOrderNumber(),
                    order.getCustomerName(),
                    order.getState(),
                    order.getProductType(),
                    order.getArea(),
                    order.getOrderDate().format(formatter)
            );

            io.displayMessage(orderInfo);
        }

        io.readString("Please hit enter to continue.");
    }

    public void displayOrder(Order order) {
        if(order != null) {
            String orderInfo = String.format("Order #%d: %s, %s, %s, %.2f sq ft",
                    order.getOrderNumber(),
                    order.getCustomerName(),
                    order.getState(),
                    order.getProductType(),
                    order.getArea());
            io.displayMessage(orderInfo);
        } else {
            io.displayMessage("Order not found.");
        }
    }

    public void displayOrderSummary(Order order) {
        if (order != null) {
            String orderInfo = String.format("Order #%d: %s, %s, %s, %.2f sq ft\nMaterial Cost: $%.2f\nLabor Cost: $%.2f\nTax: $%.2f\nTotal: $%.2f",
                    order.getOrderNumber(),
                    order.getCustomerName(),
                    order.getState(),
                    order.getProductType(),
                    order.getArea(),
                    order.getMaterialCost(),
                    order.getLaborCost(),
                    order.getTax(),
                    order.getTotal());
            io.displayMessage(orderInfo);
        } else {
            io.displayMessage("Order not found.");
        }
    }

    public boolean promptForSave(String message) {
        String response = io.readString(message + " (Y/N): ");
        return response.equalsIgnoreCase("Y");
    }

    public void displayProducts(List<String> products) {
        io.displayMessage("Available Products:");
        for (String product : products) {
            io.displayMessage("- " + product);
        }
    }

    public void displayBanner() {
        io.displayMessage("=== Welcome to the Flooring Mastery Program ===");
    }

    public void displayError(String msg) {
        io.displayMessage("=== ERROR ===");
        io.displayMessage(msg);
    }

    public void exportData(List<Order> orders) {
        io.displayMessage("Exporting data...");
        // Implement export logic here
        io.displayMessage("Data exported successfully. Please hit enter to continue.");
        io.readString("");
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

    public void displayMessage(String s) {
        io.displayMessage(s);
    }
}
