package test.java.com.m3.flooringMastery.dao;


import com.m3.flooringMastery.dao.OrderDAO;
import com.m3.flooringMastery.dao.OrderPersistenceException;
import com.m3.flooringMastery.model.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAOSTUBImpl implements OrderDAO {

    private Map<LocalDate, List<Order>> orders = new HashMap<>();

    @Override
    public Order addOrder(Order order) {
        orders.computeIfAbsent(order.getOrderDate(), k -> new ArrayList<>());

        List<Order> list = orders.get(order.getOrderDate());

        int maxId = list.stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0);

        order.setOrderNumber(maxId + 1);
        list.add(order);

        return order;
    }

    @Override
    public void removeOrder(LocalDate date, int id) {
        List<Order> list = orders.get(date);
        if (list != null) {
            list.removeIf(o -> o.getOrderNumber() == id);
        }
    }

    @Override
    public void exportData() throws OrderPersistenceException {

    }

    @Override
    public List<Order> getOrders(LocalDate date) {
        return new ArrayList<>(
                orders.getOrDefault(date, new ArrayList<>())
        );
    }

    @Override
    public void editOrder(Order order) {
        List<Order> list = orders.get(order.getOrderDate());
        if (list == null) return;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getOrderNumber() == order.getOrderNumber()) {
                list.set(i, order);
                break;
            }
        }
    }
}