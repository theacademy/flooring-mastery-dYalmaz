package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDAOImpl implements OrderDAO {

    public static final String ordersDir = "flooringMastery/Data/Orders/";
    Map<LocalDate, List<Order>> orders = new HashMap<>();
    public static final String DELIMETER = ",";


    @Override
    public List<Order> getOrders(LocalDate date)
            throws OrderPersistenceException, FileNotFoundException {

        loadOrders();

        List<Order> ordersByDate = orders.get(date);

        if (ordersByDate == null || ordersByDate.isEmpty()) {
            throw new OrderPersistenceException(
                    "No orders found for date: " + date);
        }

        return ordersByDate;
    }

    private Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMETER);

        int orderNumber = Integer.parseInt(orderTokens[0]);
        String customerName = orderTokens[1];
        String state = orderTokens[2];
        BigDecimal taxRate = new BigDecimal(orderTokens[3]);
        String productType = orderTokens[4];
        BigDecimal area = new BigDecimal(orderTokens[5]);
        BigDecimal materialCost = new BigDecimal(orderTokens[6]);
        BigDecimal laborCost = new BigDecimal(orderTokens[7]);
        BigDecimal tax = new BigDecimal(orderTokens[8]);
        BigDecimal total = new BigDecimal(orderTokens[9]);
        Order orderFromFile = new Order();
        orderFromFile.setOrderNumber(orderNumber);
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

    private String marshallOrder(Order order){
        String orderAsText = order.getCustomerName() + DELIMETER;
        orderAsText += order.getState() + DELIMETER;
        orderAsText += order.getProductType() + DELIMETER;
        orderAsText += order.getArea() + DELIMETER;
        orderAsText += order.getTaxRate() + DELIMETER;
        orderAsText += order.getMaterialCost() + DELIMETER;
        orderAsText += order.getLaborCost() + DELIMETER;
        orderAsText += order.getTax() + DELIMETER;
        orderAsText += order.getTotal();
        return orderAsText;
    }



    @Override
    public void addOrder(Order order) throws OrderPersistenceException {

        orders.computeIfAbsent(
                        order.getOrderDate(),
                        k -> new ArrayList<>())
                .add(order);
    }

    @Override
    public void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException {

        List<Order> ordersByDate = getOrders(order.getOrderDate());

        for (int i = 0; i < ordersByDate.size(); i++) {

            if (ordersByDate.get(i).getOrderNumber()
                    == order.getOrderNumber()) {

                ordersByDate.set(i, order);
                return;
            }
        }

        throw new OrderPersistenceException(
                "Order not found.");
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws OrderPersistenceException, FileNotFoundException {
        List<Order> ordersByDate = getOrders(date);
        for(int i=0; i<ordersByDate.size(); i++){
            if(ordersByDate.get(i).getOrderNumber()==orderNumber){
                return ordersByDate.remove(i);
            }
        }
        return null;
    }

    @Override
    public void exportData() {

    }

    private void loadOrders() throws OrderPersistenceException, FileNotFoundException {

        orders.clear();
        File directory = new File(ordersDir);

        // Check if directory exists
        if (!directory.exists() || !directory.isDirectory()) {
            throw new OrderPersistenceException("Orders directory not found.");
        }

        // Get all txt files
        File[] orderFiles = directory.listFiles((dir, name) ->
                name.endsWith(".txt"));

        if (orderFiles == null) {
            throw new OrderPersistenceException("No order files found.");
        }

        // Read each file
        for (File file : orderFiles) {

            String fileName = file.getName();

            // Orders_06012013.txt
            String datePortion =
                    fileName.replace("Orders_", "")
                            .replace(".txt", "");

            LocalDate orderDate = LocalDate.parse(
                    datePortion,
                    DateTimeFormatter.ofPattern("MMddyyyy"));

            try (Scanner scanner = new Scanner(
                    new BufferedReader(new FileReader(file)))) {

                // skip header
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                while (scanner.hasNextLine()) {

                    String currentLine = scanner.nextLine();

                    Order currentOrder = unmarshallOrder(currentLine);

                    currentOrder.setOrderDate(orderDate);

                    orders.computeIfAbsent(orderDate,
                                    k -> new ArrayList<>())
                            .add(currentOrder);
                }
            }
        }
    }

     private void writeOrders() throws OrderPersistenceException, FileNotFoundException {
         PrintWriter out;
            try {
                out = new PrintWriter(ordersDir);
            } catch (java.io.FileNotFoundException e) {
                throw new OrderPersistenceException("Could not save order data.", e);
            }
            List<Order> ordersByDate = getOrders(LocalDate.now());
            for (Order currentOrder : ordersByDate) {
                String orderAsText = marshallOrder(currentOrder);
                out.println(orderAsText);
                out.flush();
            }
     }

}
