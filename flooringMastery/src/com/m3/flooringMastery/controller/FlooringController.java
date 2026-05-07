package com.m3.flooringMastery.controller;

import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.service.OrderServiceLayerImpl;
import com.m3.flooringMastery.view.FlooringMasteryView;

import java.util.List;

public class FlooringController {
    OrderServiceLayerImpl orderService;
    FlooringMasteryView view;

        public void run() {
            boolean keepGoing = true;
            int menuSelection = 0;

            try {
                while (keepGoing) {
                    view.displayUserMenu();
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
            } catch (OrderPersistenceException e) {
                view.displayError(e.getMessage());
            }

        }

        public void displayOrders() throws OrderPersistenceException {
            List<Order> orders = orderService.getOrdersByDate(view.getDateFromUser());
            view.displayOrders(orders);
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
                } catch (OrderPersistenceException e) {
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
