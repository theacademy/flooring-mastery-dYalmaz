package com.m3.flooringMastery.service;

import com.m3.flooringMastery.dao.*;
import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.model.Product;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceLayerImpl implements OrderServiceLayer {
    OrderDAO orderDAO;
    ProductDAO productDAO;
    TaxDAO taxDAO;

    public OrderServiceLayerImpl(OrderDAO orderDAO, ProductDAO productDAO, TaxDAO taxDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.taxDAO = taxDAO;
    }

    @Override
    public Order createOrder(Order order)
            throws TaxPersistenceException, ProductPersistenceException, OrderPersistenceException, FileNotFoundException {

        // 1. Validate state + tax
        BigDecimal taxRate = taxDAO.getTaxRate(order.getState().toUpperCase());

        if (taxRate == null) {
            throw new TaxPersistenceException("We do not operate in state: " + order.getState());
        }

        order.setTaxRate(taxRate);

        // 2. Validate product
        Product product = productDAO.getAllProducts().stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(order.getProductType()))
                .findFirst()
                .orElseThrow(() ->
                        new ProductPersistenceException("Invalid product: " + order.getProductType())
                );

        // 3. Set pricing
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());

        // 4. Calculate costs (SAFE: now all inputs guaranteed non-null)
        order.calculateCosts(
                order.getCostPerSquareFoot(),
                order.getLaborCostPerSquareFoot()
        );

        // 5. Generate order ID per date
        List<Order> existingOrders = orderDAO.getOrders(order.getOrderDate());

        int nextId = existingOrders.stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0) + 1;

        order.setOrderNumber(nextId);

        // 6. Save
        return orderDAO.addOrder(order);
    }

    @Override
    public void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException {
            orderDAO.editOrder(order);
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException, FileNotFoundException {

        return orderDAO.getOrders(date);
    }

    @Override
    public void removeOrder(int orderId, LocalDate date) throws OrderPersistenceException, FileNotFoundException {
        orderDAO.removeOrder(date, orderId);

    }

    @Override
    public void exportAllData() throws OrderPersistenceException {
        orderDAO.exportData();
    }

    @Override
    public List<Product> getAllProducts() throws ProductPersistenceException {
        return productDAO.getAllProducts();
    }

    @Override
    public BigDecimal getTaxRate(String state) throws TaxPersistenceException {
        return taxDAO.getTaxRate(state);
    }

    @Override
    public void recalculateOrder(Order updatedOrder) {
        try {
            BigDecimal taxRate = taxDAO.getTaxRate(updatedOrder.getState());
            updatedOrder.setTaxRate(taxRate);

            Product product = productDAO.getAllProducts().stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(updatedOrder.getProductType()))
                    .findFirst()
                    .orElseThrow(() -> new ProductPersistenceException("Invalid product: " + updatedOrder.getProductType()));

            updatedOrder.setCostPerSquareFoot(product.getCostPerSquareFoot());
            updatedOrder.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());

            updatedOrder.calculateCosts(
                    updatedOrder.getCostPerSquareFoot(),
                    updatedOrder.getLaborCostPerSquareFoot()
            );

        } catch (TaxPersistenceException | ProductPersistenceException e) {
            // Log the error and rethrow as a runtime exception
            System.err.println("Error recalculating order: " + e.getMessage());
            throw new RuntimeException("Failed to recalculate order costs", e);
        }
    }


}
