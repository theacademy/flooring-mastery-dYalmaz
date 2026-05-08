package com.m3.flooringMastery.controller;

import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.dao.ProductPersistenceException;
import com.m3.flooringMastery.dao.TaxPersistenceException;
import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.service.OrderServiceLayer;
import com.m3.flooringMastery.view.FlooringMasteryView;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class FlooringController {
    OrderServiceLayer orderService;
    FlooringMasteryView view;

    public FlooringController(OrderServiceLayer orderService, FlooringMasteryView view) {
        this.orderService = orderService;
        this.view = view;
    }

        public void run() {
            boolean keepGoing = true;
            int menuSelection;
            view.displayBanner();

            try {
                while (keepGoing) {
                    menuSelection = view.getMenuSelection();

                    switch (menuSelection) {
                        case 1:
                            displayOrders();
                            break;
                        case 2:
                            addOrder();
                            break;
                        case 3:
                            editOrder();
                            break;
                        case 4:
                            removeOrder();
                            break;
                        case 5:
                            exportAllData();
                            break;
                        case 6:
                            keepGoing = false;
                            break;
                        default:
                            view.displayUnknownCommandBanner();
                    }
                }
            } catch (OrderPersistenceException | FileNotFoundException | ProductPersistenceException e) {
                view.displayError(e.getMessage());
            }

        }

        public void displayOrders() throws OrderPersistenceException, FileNotFoundException {
            try {
                List<Order> orders = orderService.getOrdersByDate(view.getDateFromUser());

                if (orders.isEmpty()) {
                    view.displayMessage("No orders found for this date.");
                } else {
                    view.displayOrders(orders);
                }

            } catch (OrderPersistenceException e) {
                view.displayMessage(e.getMessage());
            }
        }

    public void addOrder() throws ProductPersistenceException {

        view.displayAddOrderBanner();

        boolean hasErrors;

        do {
            hasErrors = false;

            Order order = view.getOrderFromUser(orderService.getAllProducts());

            try {
                orderService.createOrder(order);

                view.displayAddSuccessBanner();

            } catch (TaxPersistenceException e) {
                hasErrors = true;
                view.displayError("State error: " + e.getMessage());

            } catch (ProductPersistenceException e) {
                hasErrors = true;
                view.displayError("Product error: " + e.getMessage());

            } catch (OrderPersistenceException | FileNotFoundException e) {
                hasErrors = true;
                view.displayError("Save error: " + e.getMessage());
            }

        } while (hasErrors);
    }

    public void editOrder() {

        try {
            // 1. Get date
            LocalDate date = view.getDateFromUser();

            // 2. Load orders
            List<Order> orders = orderService.getOrdersByDate(date);

            if (orders.isEmpty()) {
                view.displayError("No orders found for that date.");
                return;
            }

            // 3. Show orders
            view.displayOrders(orders);

            // 4. Get order number
            int orderNumber = view.getUserOrderNumber();

            // 5. Find original order
            Order original = orders.stream()
                    .filter(o -> o.getOrderNumber() == orderNumber)
                    .findFirst()
                    .orElse(null);

            if (original == null) {
                view.displayError("Order not found.");
                return;
            }

            // 6. Get edited version (still raw, NOT recalculated)
            Order updated = view.editOrderMenu(original, orderService.getAllProducts());

            // 7. Recalculate + validate via SERVICE (IMPORTANT)
            orderService.editOrder(updated);

            // 8. Now reload updated order (ensures display shows correct values)
            List<Order> refreshed = orderService.getOrdersByDate(date);

            Order saved = refreshed.stream()
                    .filter(o -> o.getOrderNumber() == orderNumber)
                    .findFirst()
                    .orElse(updated);

            // 9. Display FINAL summary (now fully calculated)
            view.displayOrderSummary(saved);

            view.displayMessage("Order updated successfully.");

        } catch (Exception e) {
            view.displayError("Edit error: " + e.getMessage());
        }
    }

    public void removeOrder() {

        try {
            // 1. Get date
            LocalDate date = view.getDateFromUser();

            // 2. Load orders
            List<Order> orders = orderService.getOrdersByDate(date);

            if (orders.isEmpty()) {
                view.displayError("No orders found for that date.");
                return;
            }

            // 3. Display orders
            view.displayOrders(orders);

            // 4. Ask for order number
            int orderNumber = view.getOrderNumberForRemoval();

            // 5. Find order
            Order orderToRemove = orders.stream()
                    .filter(o -> o.getOrderNumber() == orderNumber)
                    .findFirst()
                    .orElse(null);

            if (orderToRemove == null) {
                view.displayError("Order not found.");
                return;
            }

            // 6. Show summary before delete
            view.displayOrderSummary(orderToRemove);

            // 7. Confirm deletion
            boolean confirm = view.promptForSave(
                    "Are you sure you want to remove this order?"
            );

            if (!confirm) {
                view.displayMessage("Removal cancelled.");
                return;
            }

            // 8. Remove
            orderService.removeOrder(orderNumber, date);

            // 9. Success
            view.displayMessage("Order removed successfully.");

        } catch (Exception e) {
            view.displayError("Remove error: " + e.getMessage());
        }
    }

    public void exportAllData() {

        try {
            orderService.exportAllData();
            view.exportData();
        } catch (Exception e) {
            view.displayError("Export failed: " + e.getMessage());
        }
    }

}
