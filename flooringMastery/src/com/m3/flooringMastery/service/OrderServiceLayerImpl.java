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
    public void createOrder(Order order)
            throws TaxPersistenceException, ProductPersistenceException, OrderPersistenceException, FileNotFoundException {

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
            finalizeOrder(order);   // ALWAYS recompute everything
            orderDAO.editOrder(order);

        } catch (TaxPersistenceException | ProductPersistenceException e) {
            throw new RuntimeException("Edit failed: " + e.getMessage(), e);
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

        // tax
        BigDecimal taxRate = taxDAO.getTaxRate(order.getState().toUpperCase());

        if (taxRate == null) {
            throw new TaxPersistenceException("We do not operate in state: " + order.getState());
        }

        order.setTaxRate(taxRate);

        // product
        Product product = productDAO.getAllProducts().stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(order.getProductType()))
                .findFirst()
                .orElseThrow(() ->
                        new ProductPersistenceException("Invalid product: " + order.getProductType())
                );

        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());

        // calculation (GUARANTEED SAFE NOW)
        order.calculateCosts(
                order.getCostPerSquareFoot(),
                order.getLaborCostPerSquareFoot()
        );
    }


}
