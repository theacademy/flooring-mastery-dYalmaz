package com.m3.flooringMastery.controller;

import com.m3.flooringMastery.dao.OrderDAO;
import com.m3.flooringMastery.dao.OrderDAOImpl;
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
        OrderServiceLayer service = new OrderServiceLayerImpl(orderDAO);
        FlooringController controller = new FlooringController(service, view);
        controller.run();
    }
}
