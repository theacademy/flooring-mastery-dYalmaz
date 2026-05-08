package com.m3.flooringMastery.service;

import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.dao.ProductPersistenceException;
import com.m3.flooringMastery.dao.TaxPersistenceException;
import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.model.Product;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderServiceLayer {
        public void createOrder(Order order) throws OrderPersistenceException, DuplicateIdException, OrderDataValidationException, FileNotFoundException, TaxPersistenceException, ProductPersistenceException;
        public void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException;
        public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException, FileNotFoundException;
        public void removeOrder(int orderId, LocalDate date) throws OrderPersistenceException, FileNotFoundException;
        public void exportAllData() throws OrderPersistenceException;
        List<Product> getAllProducts() throws ProductPersistenceException;
        BigDecimal getTaxRate(String state) throws TaxPersistenceException;
}
