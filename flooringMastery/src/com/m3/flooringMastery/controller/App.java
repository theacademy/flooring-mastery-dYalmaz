package com.m3.flooringMastery.controller;


import com.m3.flooringMastery.dao.OrderDAO;
import com.m3.flooringMastery.dao.OrderDAOImpl;
import com.m3.flooringMastery.dao.ProductDAO;
import com.m3.flooringMastery.dao.ProductDAOImpl;
import com.m3.flooringMastery.dao.TaxDAOImpl;
import com.m3.flooringMastery.service.OrderServiceLayer;
import com.m3.flooringMastery.view.FlooringMasteryView;
import com.m3.flooringMastery.view.UserIO;
import com.m3.flooringMastery.view.UserIOImpl;
import com.m3.flooringMastery.service.OrderServiceLayerImpl;


/**
 * App - Main entry point for the Flooring Mastery application.
 * This class initializes all dependencies and components including DAOs, service layer, views, and controller,
 * then starts the application flow.
 */
public class App {
    public static void main(String[] args) {
        UserIO io = new UserIOImpl();
        FlooringMasteryView view = new FlooringMasteryView(io);
        OrderDAO orderDAO = new OrderDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        com.m3.flooringMastery.dao.TaxDAO taxDAO = new TaxDAOImpl();

        OrderServiceLayer service = new OrderServiceLayerImpl(orderDAO, productDAO, taxDAO);
        com.m3.flooringMastery.controller.FlooringController controller = new com.m3.flooringMastery.controller.FlooringController(service, view);
        controller.run();
    }
}
