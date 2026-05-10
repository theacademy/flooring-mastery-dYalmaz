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

/**
 * OrderServiceLayer - Interface defining contract for order business logic operations.
 * Specifies methods for creating, editing, removing, and retrieving orders, as well as
 * accessing product and tax information needed for order processing.
 */
public interface OrderServiceLayer {
        void createOrder(Order order) throws OrderPersistenceException, DuplicateIdException, OrderDataValidationException, FileNotFoundException, TaxPersistenceException, ProductPersistenceException;
        void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException;
        List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException, FileNotFoundException;
        Order getOrderByDateAndNumber(LocalDate date, int orderNumber) throws OrderPersistenceException, FileNotFoundException;
        void removeOrder(int orderId, LocalDate date) throws OrderPersistenceException, FileNotFoundException;
        void exportAllData() throws OrderPersistenceException;
        List<Product> getAllProducts() throws ProductPersistenceException;
            List<String> getAllStates() throws TaxPersistenceException;
        BigDecimal getTaxRate(String state) throws TaxPersistenceException;
}
