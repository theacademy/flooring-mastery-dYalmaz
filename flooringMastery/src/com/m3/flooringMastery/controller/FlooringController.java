package com.m3.flooringMastery.controller;

import com.m3.flooringMastery.service.OrderServiceLayer;
import com.m3.flooringMastery.service.OrderServiceLayerImpl;
import com.m3.flooringMastery.view.FlooringMasteryView;

public class FlooringController {
    OrderServiceLayerImpl orderService;
    FlooringMasteryView view;

        public void run() {
            boolean keepGoing = true;
            int menuSelection = 0;


            while (keepGoing) {
                displayMenu();
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
        }

        public void displayMenu() {
            System.out.println("1. Display Orders");
            System.out.println("2. Add an Order");
            System.out.println("3. Edit an Order");
            System.out.println("4. Remove an Order");
            System.out.println("5. Export All Data");
            System.out.println("6. Quit");
        }

        public void displayOrders() {
        }

        public void addOrder() {
        }

        public void editOrder() {
        }

        public void removeOrder() {
        }

        public void exportAllData() {
        }

}
