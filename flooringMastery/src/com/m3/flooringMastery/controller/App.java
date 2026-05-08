package com.m3.flooringMastery.controller;

import com.m3.flooringMastery.dao.*;
import com.m3.flooringMastery.service.OrderServiceLayer;
import com.m3.flooringMastery.view.FlooringMasteryView;
import com.m3.flooringMastery.view.UserIO;
import com.m3.flooringMastery.view.UserIOImpl;
import com.m3.flooringMastery.service.OrderServiceLayerImpl;

public class App {
    public static void main(String[] args) {
        UserIO io = new UserIOImpl();
        FlooringMasteryView view = new FlooringMasteryView(io);
        OrderDAO orderDAO = new OrderDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        TaxDAO taxDAO = new TaxDAOImpl();
        OrderServiceLayer service = new OrderServiceLayerImpl(orderDAO, productDAO, taxDAO);
        FlooringController controller = new FlooringController(service, view);
        controller.run();
    }
}
