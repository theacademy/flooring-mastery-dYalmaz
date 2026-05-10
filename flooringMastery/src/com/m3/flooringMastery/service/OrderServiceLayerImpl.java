package com.m3.flooringMastery.service;

/**
 * OrderServiceLayerImpl - Service layer implementation for order operations.
 * This class contains business logic for creating, editing, removing, and retrieving orders.
 * It handles validation, cost calculations, tax rate lookups, and product verification.
 * It acts as an intermediary between the controller and the data access layer.
 */

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
    public void createOrder(Order order)
            throws TaxPersistenceException, ProductPersistenceException, OrderPersistenceException, FileNotFoundException {

        validateOrder(order);
        finalizeOrder(order);

        List<Order> existingOrders = orderDAO.getOrders(order.getOrderDate());

        int nextId = existingOrders.stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0) + 1;

        order.setOrderNumber(nextId);

        orderDAO.addOrder(order);
    }

    @Override
    public void editOrder(Order order)
            throws OrderPersistenceException, FileNotFoundException {

        try {
            validateOrder(order);
            finalizeOrder(order);

            orderDAO.editOrder(order);

        } catch (TaxPersistenceException | ProductPersistenceException e) {
            throw new OrderPersistenceException("Edit failed: " + e.getMessage(), e);
        }
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


    private void finalizeOrder(Order order)
            throws TaxPersistenceException, ProductPersistenceException {

        order.setState(order.getState().toUpperCase());

        BigDecimal taxRate = taxDAO.getTaxRate(order.getState());
        if (taxRate == null) {
            throw new TaxPersistenceException("We do not operate in state: " + order.getState());
        }

        Product product = productDAO.getAllProducts().stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(order.getProductType()))
                .findFirst()
                .orElseThrow(() ->
                        new ProductPersistenceException("Invalid product: " + order.getProductType())
                );

        BigDecimal costPerSqFt = product.getCostPerSquareFoot();
        BigDecimal laborCostPerSqFt = product.getLaborCostPerSquareFoot();

        if (costPerSqFt == null || laborCostPerSqFt == null) {
            throw new ProductPersistenceException("Product pricing data is missing");
        }

        order.setTaxRate(taxRate);
        order.setCostPerSquareFoot(costPerSqFt);
        order.setLaborCostPerSquareFoot(laborCostPerSqFt);

        calculateAndSetCosts(order);
    }

    private void calculateAndSetCosts(Order order) {

        BigDecimal area = order.getArea();
        BigDecimal materialCost = area.multiply(order.getCostPerSquareFoot());
        BigDecimal laborCost = area.multiply(order.getLaborCostPerSquareFoot());

        BigDecimal taxRate = order.getTaxRate();

        BigDecimal tax = materialCost.add(laborCost)
                .multiply(taxRate)
                .divide(new BigDecimal("100"));

        BigDecimal total = materialCost.add(laborCost).add(tax);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
    }

    private void validateOrder(Order order) throws OrderPersistenceException {

        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
            throw new OrderPersistenceException("Customer name cannot be blank");
        }

        if (order.getArea() == null || order.getArea().compareTo(new BigDecimal("100")) < 0) {
            throw new OrderPersistenceException("Area must be at least 100 sq ft");
        }

        if (order.getState() == null || order.getState().trim().isEmpty()) {
            throw new OrderPersistenceException("State is required");
        }

        if (order.getProductType() == null || order.getProductType().trim().isEmpty()) {
            throw new OrderPersistenceException("Product type is required");
        }
    }




}
