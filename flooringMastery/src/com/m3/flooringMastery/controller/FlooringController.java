package com.m3.flooringMastery.controller;

import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.service.OrderServiceLayer;
import com.m3.flooringMastery.service.OrderServiceLayerImpl;
import com.m3.flooringMastery.view.FlooringMasteryView;

import java.io.FileNotFoundException;
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
            int menuSelection = 0;

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
            } catch (OrderPersistenceException | FileNotFoundException e) {
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

        public void addOrder() throws OrderPersistenceException {
            view.displayAddOrderBanner();
            boolean hasErrors= false;
            do {
                Order order = view.getOrderFromUser();
                try {
                    orderService.createOrder(order);
                    view.displayAddSuccessBanner();
                    hasErrors = false;
                } catch (OrderPersistenceException | FileNotFoundException e) {
                    hasErrors = true;
                    view.displayError(e.getMessage());
                }
            } while (hasErrors);

        }

        public void editOrder() throws OrderPersistenceException {


        }

        public void removeOrder() throws OrderPersistenceException {
            view.removeOrder();

        }

        public void exportAllData() {
        }

}
