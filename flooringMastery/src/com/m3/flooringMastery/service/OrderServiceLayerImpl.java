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


        BigDecimal taxRate = taxDAO.getTaxRate(order.getState());

        if (taxRate == null) {
            throw new TaxPersistenceException("Invalid state: " + order.getState());
        }

        order.setTaxRate(taxRate);


        Product product = productDAO.getAllProducts().stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(order.getProductType()))
                .findFirst()
                .orElseThrow(() -> new ProductPersistenceException("Invalid product: " + order.getProductType()));


        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());


        order.calculateCosts(
                order.getCostPerSquareFoot(),
                order.getLaborCostPerSquareFoot()
        );

        // 5. Assign order number safely (per date)
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


}
