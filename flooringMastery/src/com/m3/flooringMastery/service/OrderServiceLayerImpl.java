package com.m3.flooringMastery.service;

import com.m3.flooringMastery.dao.*;
import com.m3.flooringMastery.model.Order;
import com.m3.flooringMastery.model.Product;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderServiceLayerImpl - Service layer implementation for order operations.
 * This class contains business logic for creating, editing, removing, and retrieving orders.
 * It handles validation, cost calculations, tax rate lookups, and product verification.
 * It acts as an intermediary between the controller and the data access layer.
 */
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


        // Display the order summary to the user for confirmation before saving

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
    public Order getOrderByDateAndNumber(LocalDate date, int orderNumber)
            throws OrderPersistenceException, FileNotFoundException {

        return getOrdersByDate(date).stream()
                .filter(order -> order.getOrderNumber() == orderNumber)
                .findFirst()
                .orElse(null);
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
    public List<String> getAllStates() throws TaxPersistenceException {
        return taxDAO.getAllTaxes().stream()
                .map(t -> t.getStateAbbreviation().toUpperCase())
                .collect(Collectors.toList());
    }

    @Override
    public Order prepareOrder(Order order) throws TaxPersistenceException, ProductPersistenceException, OrderPersistenceException {
        // Validate and enrich the order (calculations) without persisting or assigning an order number
        validateOrder(order);
        finalizeOrder(order);
        return order;
    }


    private void finalizeOrder(Order order)
            throws TaxPersistenceException, ProductPersistenceException {

        order.setState(order.getState().toUpperCase());

        // Validate state exists before retrieving tax rate
        if (!taxDAO.stateExists(order.getState())) {
            throw new TaxPersistenceException("We do not operate in state: " + order.getState());

        }
        BigDecimal taxRate = taxDAO.getTaxRate(order.getState());

        // Validate product type and retrieve pricing directly
        productDAO.getProductType(order.getProductType()); // throws if invalid
        BigDecimal costPerSqFt = productDAO.getCostPerSquareFoot(order.getProductType());
        BigDecimal laborCostPerSqFt = productDAO.getLaborCostPerSquareFoot(order.getProductType());

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
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

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
