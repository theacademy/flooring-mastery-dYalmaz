package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDAOImpl implements OrderDAO {

    public static final String ORDER_DIR = "flooringMastery/Orders/";
    Map<LocalDate, List<Order>> orders = new HashMap<>();
    public static final String DELIMETER = ":;:";
        public static final String BACKUP_DIR = "flooringMastery/Backup/";


    @Override
    public List<Order> getOrders(LocalDate date) throws OrderPersistenceException {

        loadOrders();

        return new ArrayList<>(
                orders.getOrDefault(date, new ArrayList<>())
        );
    }

    private Order unmarshallOrder(String line) {

        String[] tokens = line.split(DELIMETER);

        if (tokens.length < 12) {
            throw new IllegalArgumentException(
                    "Corrupt order line: " + line
            );
        }

        Order order = new Order();

        order.setOrderNumber(Integer.parseInt(tokens[0]));
        order.setCustomerName(tokens[1]);
        order.setState(tokens[2]);
        order.setTaxRate(new BigDecimal(tokens[3]));
        order.setProductType(tokens[4]);
        order.setArea(new BigDecimal(tokens[5]));
        order.setCostPerSquareFoot(new BigDecimal(tokens[6]));
        order.setLaborCostPerSquareFoot(new BigDecimal(tokens[7]));
        order.setMaterialCost(new BigDecimal(tokens[8]));
        order.setLaborCost(new BigDecimal(tokens[9]));
        order.setTax(new BigDecimal(tokens[10]));
        order.setTotal(new BigDecimal(tokens[11]));

        return order;
    }

    private String marshallOrder(Order order) {

        return order.getOrderNumber() + DELIMETER +
                order.getCustomerName() + DELIMETER +
                order.getState() + DELIMETER +
                order.getTaxRate() + DELIMETER +
                order.getProductType() + DELIMETER +
                order.getArea() + DELIMETER +
                order.getCostPerSquareFoot() + DELIMETER +
                order.getLaborCostPerSquareFoot() + DELIMETER +
                order.getMaterialCost() + DELIMETER +
                order.getLaborCost() + DELIMETER +
                order.getTax() + DELIMETER +
                order.getTotal();
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
            throws OrderPersistenceException, FileNotFoundException {

        loadOrders();

        List<Order> list = orders.get(date);

        if (list == null) {
            return;
        }

        list.removeIf(o -> o.getOrderNumber() == orderNumber);

        // If no orders left → delete file
        if (list.isEmpty()) {

            orders.remove(date);

            String fileName = ORDER_DIR + "Orders_" +
                    date.format(DateTimeFormatter.ofPattern("MMddyyyy")) +
                    ".txt";

            File file = new File(fileName);

            if (file.exists()) {
                file.delete();
            }

        } else {

            // otherwise rewrite updated file
            writeOrdersForDate(date, list);
        }
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
                    "OrderNumber,CustomerName,State,TaxRate,ProductType," +
                            "Area,CostPerSquareFoot,LaborCostPerSquareFoot," +
                            "MaterialCost,LaborCost,Tax,Total,OrderDate"
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

        File directory = new File(ORDER_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (Map.Entry<LocalDate, List<Order>> entry : orders.entrySet()) {

            LocalDate date = entry.getKey();

            String fileName = "Orders_" +
                    date.format(DateTimeFormatter.ofPattern("MMddyyyy")) +
                    ".txt";

            File file = new File(directory, fileName);

            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

                out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

                for (Order order : entry.getValue()) {

                    out.println(marshallOrder(order));
                }

            } catch (IOException e) {
                throw new OrderPersistenceException(
                        "Could not save order data: " + e.getMessage(), e
                );
            }
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

            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                    + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,"
                    + "LaborCost,Tax,Total");

            for (Order order : orders) {
                out.println(marshallOrder(order));
            }

        } catch (IOException e) {
            throw new OrderPersistenceException("Could not write file: " + fileName, e);
        }
    }

}
