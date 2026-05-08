package com.m3.flooringMastery.service;

import com.m3.flooringMastery.dao.OrderDAO;
import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.model.Order;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceLayerImpl implements OrderServiceLayer {
    OrderDAO dao;

    public OrderServiceLayerImpl(OrderDAO dao) {
        this.dao = dao;
    }

    @Override
    public void createOrder(Order order) throws OrderPersistenceException, DuplicateIdException, OrderDataValidationException, FileNotFoundException {
        if(dao.getOrders(order.getOrderDate()).contains(order)){
            throw new DuplicateIdException("ERROR: Order ID already exists.");
        }
        if(order.getCustomerName()==null || order.getCustomerName().trim().isEmpty() || order.getState()==null || order.getState().trim().isEmpty() || order.getProductType()==null || order.getProductType().trim().isEmpty()){
            throw new OrderDataValidationException("ERROR: All fields [Customer Name, State, Product Type] are required.");
        }
        dao.addOrder(order);

    }

    @Override
    public void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException {
            dao.editOrder(order);
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException, FileNotFoundException {

        return dao.getOrders(date);
    }

    @Override
    public Order removeOrder(int orderId, LocalDate date) throws OrderPersistenceException, FileNotFoundException {
        return dao.removeOrder(date, orderId);

    }

    @Override
    public void exportAllData() throws OrderPersistenceException {
        dao.exportData();
    }
}
