package com.m3.flooringMastery.service;

import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.model.Order;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public interface OrderServiceLayer {
        public void createOrder(Order order) throws OrderPersistenceException, DuplicateIdException, OrderDataValidationException, FileNotFoundException;
        public void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException;
        public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException, FileNotFoundException;
        public Order removeOrder(int orderId, LocalDate date) throws OrderPersistenceException, FileNotFoundException;
        public void exportAllData() throws OrderPersistenceException;
}
