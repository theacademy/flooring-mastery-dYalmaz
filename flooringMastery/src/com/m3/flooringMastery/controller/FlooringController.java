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

/**
 * FlooringController - Main application controller that orchestrates the flooring mastery program.
 * This class manages the flow of the application by handling menu selections and delegating operations
 * to the service layer. It coordinates displaying orders, adding, editing, removing orders, and exporting data.
 */
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
            LocalDate date = view.getDateFromUser();
            List<Order> orders = orderService.getOrdersByDate(date);

            if (orders.isEmpty()) {
                view.displayError("No orders found for that date.");
                return;
            }

            view.displayOrders(orders);
            int orderNumber = view.getUserOrderNumber();

            Order original = orderService.getOrderByDateAndNumber(date, orderNumber);

            if (original == null) {
                view.displayError("Order not found.");
                return;
            }

            Order updated = view.editOrderMenu(original, orderService.getAllProducts());
            orderService.editOrder(updated);

            view.displayOrderSummary(updated);

            view.displayMessage("Order updated successfully.");

        } catch (OrderPersistenceException | FileNotFoundException | ProductPersistenceException e) {
            view.displayError("Edit error: " + e.getMessage());
        }
    }

    public void removeOrder() {

        try {
            LocalDate date = view.getDateFromUser();
            List<Order> orders = orderService.getOrdersByDate(date);

            if (orders.isEmpty()) {
                view.displayError("No orders found for that date.");
                return;
            }

            view.displayOrders(orders);
            int orderNumber = view.getOrderNumberForRemoval();

            Order orderToRemove = orderService.getOrderByDateAndNumber(date, orderNumber);

            if (orderToRemove == null) {
                view.displayError("Order not found.");
                return;
            }

            view.displayOrderSummary(orderToRemove);
            boolean confirm = view.promptForSave(
                    "Are you sure you want to remove this order?"
            );

            if (!confirm) {
                view.displayMessage("Removal cancelled.");
                return;
            }

            orderService.removeOrder(orderNumber, date);

            view.displayMessage("Order removed successfully.");

        } catch (OrderPersistenceException | FileNotFoundException e) {
            view.displayError("Remove error: " + e.getMessage());
        }
    }

    public void exportAllData() {

        try {
            orderService.exportAllData();
            view.exportData();
        } catch (OrderPersistenceException e) {
            view.displayError("Export failed: " + e.getMessage());
        }
    }

}
