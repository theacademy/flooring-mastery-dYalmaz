package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Order;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OrderDAOImpl implements OrderDAO {

    String ordersDir;
    Map<LocalDate, List<Order>> orders = new HashMap<>();
    public static final String DELIMETER = "::";


    @Override
    public List<Order> getOrders(LocalDate date) throws OrderPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ordersDir + "Orders_" + date + ".txt")));
    }catch (FileNotFoundException e) {
            throw new OrderPersistenceException("Could not load order data into memory.", e);
        }

        String currentLine;
        Order currentOrder;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            orders.get(date).add(currentOrder);
        }
        scanner.close();
        return orders.get(date);
    }

    private Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMETER);

        String customerName = orderTokens[0];
        String state = orderTokens[1];
        String productType = orderTokens[2];
        BigDecimal area = new BigDecimal(orderTokens[3]);
        BigDecimal taxRate = new BigDecimal(orderTokens[4]);
        BigDecimal materialCost = new BigDecimal(orderTokens[5]);
        BigDecimal laborCost = new BigDecimal(orderTokens[6]);
        BigDecimal tax = new BigDecimal(orderTokens[7]);
        BigDecimal total = new BigDecimal(orderTokens[8]);
        Order orderFromFile = new Order();
        orderFromFile.setCustomerName(customerName);
        orderFromFile.setState(state);
        orderFromFile.setProductType(productType);
        orderFromFile.setArea(area);
        orderFromFile.setTaxRate(taxRate);
        orderFromFile.setMaterialCost(materialCost);
        orderFromFile.setLaborCost(laborCost);
        orderFromFile.setTax(tax);
        orderFromFile.setTotal(total);

        return orderFromFile;
    }

    @Override
    public void addOrder(Order order) throws OrderPersistenceException {
        getOrders(order.getOrderDate()).add(order);
    }

    @Override
    public void editOrder(Order order) throws OrderPersistenceException {
        List<Order> ordersByDate = getOrders(order.getOrderDate());
        for(int i=0; i<ordersByDate.size(); i++){
            if(ordersByDate.get(i).getOrderNumber()==order.getOrderNumber()){
                ordersByDate.set(i, order);
                break;
            }
        }
        orders.put(order.getOrderDate(), ordersByDate);
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws OrderPersistenceException {
        List<Order> ordersByDate = getOrders(date);
        for(int i=0; i<ordersByDate.size(); i++){
            if(ordersByDate.get(i).getOrderNumber()==orderNumber){
                Order removedOrder = ordersByDate.remove(i);
                orders.put(date, ordersByDate);
                return removedOrder;
            }
        }
        return null;
    }

    @Override
    public void exportData() {

    }
}
