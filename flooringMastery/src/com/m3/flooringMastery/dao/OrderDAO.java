package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Order;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public interface OrderDAO {
    List<Order> getOrders(LocalDate date) throws OrderPersistenceException, FileNotFoundException;
    void addOrder(Order order) throws OrderPersistenceException;
    void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException;
    Order removeOrder(LocalDate date, int orderNumber) throws OrderPersistenceException, FileNotFoundException;
    void exportData() throws OrderPersistenceException;

}
