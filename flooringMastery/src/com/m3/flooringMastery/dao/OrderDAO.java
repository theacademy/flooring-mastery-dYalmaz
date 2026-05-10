package com.m3.flooringMastery.dao;

/**
 * OrderDAO - Interface defining contract for order data persistence operations.
 * Specifies methods for retrieving, adding, editing, and removing orders, and exporting order data.
 */

import com.m3.flooringMastery.model.Order;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public interface OrderDAO {
    List<Order> getOrders(LocalDate date) throws OrderPersistenceException, FileNotFoundException;
    Order addOrder(Order order) throws OrderPersistenceException, FileNotFoundException;
    void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException;
    void removeOrder(LocalDate date, int orderNumber) throws OrderPersistenceException, FileNotFoundException;
    void exportData() throws OrderPersistenceException;

}
