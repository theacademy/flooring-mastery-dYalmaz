package com.m3.flooringMastery.controller;


import com.m3.flooringMastery.dao.*;
import com.m3.flooringMastery.service.OrderServiceLayer;
import com.m3.flooringMastery.view.FlooringMasteryView;
import com.m3.flooringMastery.view.UserIO;
import com.m3.flooringMastery.view.UserIOImpl;
import com.m3.flooringMastery.service.OrderServiceLayerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * App - Main entry point for the Flooring Mastery application.
 * This class initializes all dependencies and components including DAOs, service layer, views, and controller,
 * then starts the application flow.
 */
public class App {
    public static void main(String[] args) {
//        UserIO io = new UserIOImpl();
//        FlooringMasteryView view = new FlooringMasteryView(io);
//        OrderDAO orderDAO = new OrderDAOImpl();
//        ProductDAO productDAO = new ProductDAOImpl();
//        TaxDAO taxDAO = new TaxDAOImpl();
//        OrderServiceLayer service = new OrderServiceLayerImpl(orderDAO, productDAO, taxDAO);

        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        FlooringController controller = ctx.getBean("controller", FlooringController.class);
        controller.run();
    }
}
