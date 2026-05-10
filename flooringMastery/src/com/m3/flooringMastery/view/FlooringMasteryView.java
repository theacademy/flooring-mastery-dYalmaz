package com.m3.flooringMastery.view;


import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * FlooringMasteryView - View component responsible for user interface and user interactions.
 * This class handles displaying menus, forms, and information to the user, and gathering input for orders.
 * It manages the interaction flow for displaying orders, collecting order data, editing orders, and displaying results.
 */
public class FlooringMasteryView {

    private final UserIO io;

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

    public Order getOrderFromUser(List<Product> products, List<String> validStates) {

        LocalDate date = io.readDate("Please enter the order date (MM/DD/YYYY): ");

        String customerName = io.readString("Please enter the customer's name: ");
        while (!nameValidation(customerName)) {
            io.displayMessage("Invalid name. Please try again.");
            customerName = io.readString("Please enter the customer's name: ");
        }

        String state = io.readString("Please enter the state abbreviation: ");

        while (state.trim().isEmpty() || !state.trim().matches("(?i)^[a-z]{2}$") || !validStates.contains(state.trim().toUpperCase())) {
            io.displayMessage("Please enter a valid 2-letter state code (e.g. CA, TX) that we operate in.");
            state = io.readString("Please enter the state abbreviation: ");
        }

        state = state.trim().toUpperCase();
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

        // view builds the object
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


    public Order editOrderMenu(Order originalOrder, List<Product> products, List<String> validStates) {

        Order updated = new Order();

        updated.setOrderNumber(originalOrder.getOrderNumber());
        updated.setOrderDate(originalOrder.getOrderDate());

        // ---------- CUSTOMER NAME ----------
        while (true) {
            String customerName = io.readString(
                    "Enter new customer name (" + originalOrder.getCustomerName() + "): "
            );

            if (customerName.isBlank()) {
                updated.setCustomerName(originalOrder.getCustomerName());
                break;
            }

            if (nameValidation(customerName)) {
                updated.setCustomerName(customerName);
                break;
            }

            io.displayMessage("Invalid name. Please try again.");
        }

        // ---------- STATE ----------
        while (true) {
            String state = io.readString(
                    "Enter new state (" + originalOrder.getState() + "): "
            );

            if (state.isBlank()) {
                updated.setState(originalOrder.getState());
                break;
            }

            String candidate = state.trim().toUpperCase();
            if (state.trim().matches("(?i)^[a-z]{2}$") && validStates.contains(candidate)) {
                updated.setState(candidate);
                break;
            }

            io.displayMessage("Please enter a valid 2-letter state code (e.g. CA, TX) that we operate in.");
        }

        // ---------- PRODUCT TYPE ----------
        while (true) {
            String productType = io.readString(
                    "Enter new product type (" + originalOrder.getProductType() + "): "
            );

            if (productType.isBlank()) {
                updated.setProductType(originalOrder.getProductType());
                break;
            }

            boolean validProduct = products.stream()
                    .anyMatch(p -> p.getProductType().equalsIgnoreCase(productType));

            if (validProduct) {
                updated.setProductType(productType);
                break;
            }

            io.displayMessage("Invalid product choice. Please try again.");
        }

        // ---------- AREA ----------
        while (true) {
            String areaInput = io.readString(
                    "Enter new area (" + originalOrder.getArea() + "): "
            );

            if (areaInput.isBlank()) {
                updated.setArea(originalOrder.getArea());
                break;
            }

            try {
                BigDecimal area = new BigDecimal(areaInput.trim());

                if (area.compareTo(BigDecimal.valueOf(100)) < 0) {
                    io.displayMessage("Area must be at least 100 sq ft.");
                    continue;
                }

                updated.setArea(area);
                break;

            } catch (NumberFormatException e) {
                io.displayMessage("Invalid number. Please try again.");
            }
        }

        // ---------- COPY NON-DEPENDENT DATA ----------
        updated.setTaxRate(originalOrder.getTaxRate());
        updated.setCostPerSquareFoot(originalOrder.getCostPerSquareFoot());
        updated.setLaborCostPerSquareFoot(originalOrder.getLaborCostPerSquareFoot());

        // IMPORTANT: derived fields cleared (service recalculates)
        updated.setMaterialCost(null);
        updated.setLaborCost(null);
        updated.setTax(null);
        updated.setTotal(null);


        return updated;
    }

    public int getOrderNumberForRemoval() {
        return io.readInt("Please enter the order number to remove: ");
    }

    public void displayOrders(List<Order> orders) {

        for (Order order : orders) {

            String orderInfo = String.format(
                    "Order #%d: %s, %s, %s, %.2f sq ft - Total: $%.2f",
                    order.getOrderNumber(),
                    order.getCustomerName(),
                    order.getState(),
                    order.getProductType(),
                    order.getArea(),
                    order.getTotal()
            );

            io.displayMessage(orderInfo);
        }

        io.readString("Please hit enter to continue.");
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

    public void displayBanner() {
        io.displayMessage("=== Welcome to the Flooring Mastery Program ===");
    }

    public void displayError(String msg) {
        io.displayMessage("=== ERROR ===");
        io.displayMessage(msg);
    }

    public void exportData() {
        io.displayMessage("=== Export All Data ===");
        io.displayMessage("All order data has been exported successfully.");
        io.readString("Please hit enter to continue.");
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

    public int getUserOrderNumber() {
        return io.readInt("Please enter the order number: ");
    }
}
