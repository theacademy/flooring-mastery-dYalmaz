package com.m3.flooringMastery.service;

import com.m3.flooringMastery.dao.OrderDAO;
import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public class OrderServiceLayerImpl implements OrderServiceLayer {
    OrderDAO dao;


    @Override
    public void createOrder(Order order) throws OrderPersistenceException, DuplicateIdException, OrderDataValidationException {




    }

    @Override
    public void editOrder(Order order) throws OrderPersistenceException {

    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException {

        return dao.getOrders(date);
    }

    @Override
    public Order removeOrder(int orderId, LocalDate date) throws OrderPersistenceException {
        return dao.removeOrder(date, orderId);

    }

    @Override
    public void exportAllData() throws OrderPersistenceException {

    }
}
