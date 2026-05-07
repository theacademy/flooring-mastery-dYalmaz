package com.m3.flooringMastery.service;

import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public class OrderServiceLayerImpl implements OrderServiceLayer {

    public OrderServiceLayerImpl() {
    }

    @Override
    public void createOrder(Order order) throws OrderPersistenceException, DuplicateIdException {

    }

    @Override
    public void editOrder(Order order) throws OrderPersistenceException {

    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException {

        return null;
    }

    @Override
    public void removeOrder(int orderId, LocalDate date) throws OrderPersistenceException {

    }

    @Override
    public void exportAllData() throws OrderPersistenceException {

    }
}
