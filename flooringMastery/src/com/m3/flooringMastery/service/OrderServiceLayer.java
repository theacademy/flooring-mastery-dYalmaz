package com.m3.flooringMastery.service;

import com.m3.flooringMastery.model.Order;

import java.time.LocalDate;

public interface OrderServiceLayer {
        public void createOrder(Order order) throws OrderPersistenceException, DuplicateIdException;
        public void editOrder(Order order) throws OrderPersistenceException;
        public void getOrdersByDate(LocalDate date) throws OrderPersistenceException;
        public void removeOrder(int orderId, LocalDate date) throws OrderPersistenceException;
        public void exportAllData() throws OrderPersistenceException;
}
