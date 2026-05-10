package com.m3.flooringMastery.dao;

/**
 * OrderDAOImpl - Data Access Object for managing order persistence.
 * This class handles all file I/O operations for orders including loading, saving, editing, and removing orders.
 * It uses a delimiter-based file format to persist order data organized by date, and supports data export functionality.
 */

import com.m3.flooringMastery.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDAOImpl implements OrderDAO {

    String ORDER_DIR = "flooringMastery/Orders/";
    Map<LocalDate, List<Order>> orders = new HashMap<>();
    public static final String DELIMETER = ":;:";
        public static final String BACKUP_DIR = "flooringMastery/Backup/";

    public OrderDAOImpl(String s) {
        ORDER_DIR = s;
    }

    public OrderDAOImpl() {
    }


    @Override
    public List<Order> getOrders(LocalDate date) throws OrderPersistenceException {
        if (orders.isEmpty()) {
            loadOrders();
        }

        return new ArrayList<>(orders.getOrDefault(date, new ArrayList<>()));
    }

    private String safeDecimal(BigDecimal val) {
        return val == null ? "0.00" : val.toString();
    }

    private String safeInt(Integer val) {
        return val == null ? "0" : val.toString();
    }

    private BigDecimal safeBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value);
    }

    private Order unmarshallOrder(String line) {

        String[] tokens = line.split(DELIMETER, -1); // IMPORTANT FIX

        if (tokens.length < 12) {
            throw new IllegalArgumentException("Corrupt order line: " + line);
        }

        Order order = new Order();

        order.setOrderNumber(Integer.parseInt(tokens[0]));
        order.setCustomerName(tokens[1]);
        order.setState(tokens[2]);
        order.setTaxRate(safeBigDecimal(tokens[3]));
        order.setProductType(tokens[4]);
        order.setArea(safeBigDecimal(tokens[5]));
        order.setCostPerSquareFoot(safeBigDecimal(tokens[6]));
        order.setLaborCostPerSquareFoot(safeBigDecimal(tokens[7]));
        order.setMaterialCost(safeBigDecimal(tokens[8]));
        order.setLaborCost(safeBigDecimal(tokens[9]));
        order.setTax(safeBigDecimal(tokens[10]));
        order.setTotal(safeBigDecimal(tokens[11]));

        return order;
    }

    private String safe(Object o) {
        if (o == null) {
            return "0"; // or throw depending on your rules
        }
        return o.toString();
    }


    private String marshallOrder(Order order) {

        return safe(order.getOrderNumber()) + DELIMETER +
                safe(order.getCustomerName()) + DELIMETER +
                safe(order.getState()) + DELIMETER +
                safe(order.getTaxRate()) + DELIMETER +
                safe(order.getProductType()) + DELIMETER +
                safe(order.getArea()) + DELIMETER +
                safe(order.getCostPerSquareFoot()) + DELIMETER +
                safe(order.getLaborCostPerSquareFoot()) + DELIMETER +
                safe(order.getMaterialCost()) + DELIMETER +
                safe(order.getLaborCost()) + DELIMETER +
                safe(order.getTax()) + DELIMETER +
                safe(order.getTotal());
    }

    @Override
    public Order addOrder(Order order) throws OrderPersistenceException {

        LocalDate date = order.getOrderDate();

        // Load only that date's file
        List<Order> ordersForDate = loadOrdersForDate(date);

        // assign order number safely
        int maxId = ordersForDate.stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0);

        order.setOrderNumber(maxId + 1);

        ordersForDate.add(order);

        orders.put(date, ordersForDate);

        writeOrdersForDate(date, ordersForDate);

        return order;
    }

    @Override
    public void editOrder(Order order) throws OrderPersistenceException, FileNotFoundException {

        orders.clear();
        loadOrders();

        List<Order> list = orders.get(order.getOrderDate());

        if (list == null) return;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getOrderNumber() == order.getOrderNumber()) {
                list.set(i, order);
                break;
            }
        }

        writeOrders();
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber)
            throws OrderPersistenceException {

        orders.clear();
        loadOrders();

        List<Order> list = orders.get(date);

        if (list == null) return;

        boolean removed = list.removeIf(o -> o.getOrderNumber() == orderNumber);

        if (!removed) return;

        if (list.isEmpty()) {
            orders.remove(date);
            deleteOrdersFile(date);
            return;
        }

        writeOrders();
    }

    @Override
    public void exportData() throws OrderPersistenceException {
        loadOrders();
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
        File exportFile = new File(backupDir, "DataExport.txt");
        try (PrintWriter out = new PrintWriter(new FileWriter(exportFile))) {
            out.println(
                    "OrderNumber:;:CustomerName:;:State:;:TaxRate:;:ProductType:;:Area:;:" +
                            "CostPerSquareFoot:;:LaborCostPerSquareFoot:;:MaterialCost:;:LaborCost:;:Tax:;:Total"
            );
            for (Map.Entry<LocalDate, List<Order>> entry : orders.entrySet()) {
                LocalDate date = entry.getKey();
                for (Order order : entry.getValue()) {

                    out.println(
                            marshallOrder(order)
                                    + DELIMETER
                                    + date
                    );
                }
            }
        } catch (IOException e) {

            throw new OrderPersistenceException(
                    "Could not export data.", e
            );
        }
    }

    private void loadOrders() throws OrderPersistenceException {

        orders.clear();

        File directory = new File(ORDER_DIR);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new OrderPersistenceException("Orders directory not found.");
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) return;

        for (File file : files) {

            String fileName = file.getName();

            String datePart = fileName
                    .replace("Orders_", "")
                    .replace(".txt", "");

            LocalDate orderDate = LocalDate.parse(datePart,
                    DateTimeFormatter.ofPattern("MMddyyyy"));

            try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {

                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // skip header
                }

                while (scanner.hasNextLine()) {

                    String line = scanner.nextLine();
                    Order order = unmarshallOrder(line);

                    order.setOrderDate(orderDate);

                    orders.computeIfAbsent(orderDate, k -> new ArrayList<>())
                            .add(order);
                }

            } catch (FileNotFoundException e) {
                throw new OrderPersistenceException("Could not load file: " + file.getName(), e);
            }
        }
    }

    private List<Order> loadOrdersForDate(LocalDate date) throws OrderPersistenceException {

        List<Order> list = new ArrayList<>();

        String fileName = ORDER_DIR + "Orders_" +
                date.format(DateTimeFormatter.ofPattern("MMddyyyy")) +
                ".txt";

        File file = new File(fileName);

        if (!file.exists()) {
            return list; // no file yet → empty list
        }

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {

            if (scanner.hasNextLine()) {
                scanner.nextLine(); // skip header
            }

            while (scanner.hasNextLine()) {
                try {
                    Order order = unmarshallOrder(scanner.nextLine());
                    order.setOrderDate(date);
                    list.add(order);
                } catch (Exception e) {
                    System.out.println("Skipping bad line in file " + fileName);
                }
            }

        } catch (FileNotFoundException e) {
            throw new OrderPersistenceException("Could not load file: " + fileName, e);
        }

        return list;
    }

    public void writeOrders() throws OrderPersistenceException {

        for (Map.Entry<LocalDate, List<Order>> entry : orders.entrySet()) {
            writeOrdersForDate(entry.getKey(), entry.getValue());
        }
    }

    private void writeOrdersForDate(LocalDate date, List<Order> orders)
            throws OrderPersistenceException {

        File directory = new File(ORDER_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "Orders_" +
                date.format(DateTimeFormatter.ofPattern("MMddyyyy")) +
                ".txt";

        File file = new File(directory, fileName);

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

            out.println(
                    "OrderNumber:;:CustomerName:;:State:;:TaxRate:;:ProductType:;:Area:;:" +
                            "CostPerSquareFoot:;:LaborCostPerSquareFoot:;:MaterialCost:;:LaborCost:;:Tax:;:Total"
            );

            for (Order order : orders) {
                out.println(marshallOrder(order));
            }

        } catch (IOException e) {
            throw new OrderPersistenceException("Could not write file: " + fileName, e);
        }
    }

    private void deleteOrdersFile(LocalDate date) throws OrderPersistenceException {
        File file = new File(
                ORDER_DIR,
                "Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt"
        );

        if (file.exists() && !file.delete()) {
            throw new OrderPersistenceException("Could not delete file: " + file.getName());
        }
    }

}
