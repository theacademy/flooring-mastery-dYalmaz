package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Order;

public interface OrderDAO {
    Order getOrders(String date, int orderNumber);
    void addOrder(Order order);
    void editOrder(Order order);
    void removeOrder(String date, int orderNumber);
    void exportData();

}
