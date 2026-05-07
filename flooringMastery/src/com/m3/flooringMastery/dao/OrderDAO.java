package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Order;

public interface OrderDAO {
    void getOrders(String date, int orderNumber) throws OrderPersistenceException;
    void addOrder(Order order) throws OrderPersistenceException;
    void editOrder(Order order) throws OrderPersistenceException;
    void removeOrder(String date, int orderNumber) throws OrderPersistenceException;
    void exportData() throws OrderPersistenceException;

}
