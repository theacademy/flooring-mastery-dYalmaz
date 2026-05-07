package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAOImpl implements OrderDAO {

    String ordersDir;
    Map<LocalDate, List<Order>> orders = new HashMap<>();


    @Override
    public Order getOrders(String date, int orderNumber) {
        return null;
    }

    @Override
    public void addOrder(Order order) {

    }

    @Override
    public void editOrder(Order order) {

    }

    @Override
    public void removeOrder(String date, int orderNumber) {

    }

    @Override
    public void exportData() {

    }
}
