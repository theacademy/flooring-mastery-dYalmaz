package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.OrderDAO;
import com.m3.flooringMastery.dao.OrderDAOImpl;
import com.m3.flooringMastery.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOImplTest {

    private Path testDir;
    private OrderDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        testDir = Files.createTempDirectory("order-dao-test-");
        dao = new OrderDAOImpl(testDir.toString() + java.io.File.separator);
    }

    private Order buildOrder(LocalDate date, String customerName) {
        Order order = new Order();
        order.setOrderDate(date);
        order.setCustomerName(customerName);
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("120"));
        order.setTaxRate(new BigDecimal("8.25"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(new BigDecimal("618.00"));
        order.setLaborCost(new BigDecimal("570.00"));
        order.setTax(new BigDecimal("97.83"));
        order.setTotal(new BigDecimal("1285.83"));
        return order;
    }

    // --------------------------------------------------
    // ADD ORDER
    // --------------------------------------------------
    @Test
    void testAddOrder_CreatesAndSavesOrder() throws Exception {

        LocalDate date = LocalDate.of(2026, 6, 6);
        Order order = buildOrder(date, "Test User");

        Order added = dao.addOrder(order);

        assertNotNull(added);
        assertTrue(added.getOrderNumber() > 0);

        OrderDAO reloadedDao = new OrderDAOImpl(testDir.toString() + java.io.File.separator);
        List<Order> orders = reloadedDao.getOrders(date);

        assertEquals(1, orders.size());
        assertEquals("Test User", orders.get(0).getCustomerName());
        assertEquals(added.getOrderNumber(), orders.get(0).getOrderNumber());
    }

    // --------------------------------------------------
    // GET ORDERS
    // --------------------------------------------------
    @Test
    void testGetOrders_ReturnsEmptyListInitially() throws Exception {

        LocalDate date = LocalDate.of(2026, 6, 6);

        List<Order> orders = dao.getOrders(date);

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    // --------------------------------------------------
    // REMOVE ORDER
    // --------------------------------------------------
    @Test
    void testRemoveOrder_RemovesOrder() throws Exception {

        LocalDate date = LocalDate.of(2026, 6, 6);
        Order order = buildOrder(date, "Remove Me");
        order.setArea(new BigDecimal("150"));

        Order added = dao.addOrder(order);

        dao.removeOrder(date, added.getOrderNumber());

        OrderDAO reloadedDao = new OrderDAOImpl(testDir.toString() + java.io.File.separator);
        List<Order> updated = reloadedDao.getOrders(date);

        assertTrue(updated.isEmpty());
    }

    // --------------------------------------------------
    // EDIT ORDER
    // --------------------------------------------------
    @Test
    void testEditOrder_UpdatesOrder() throws Exception {

        LocalDate date = LocalDate.of(2026, 6, 6);
        Order order = buildOrder(date, "Old Name");
        order.setArea(new BigDecimal("150"));

        Order added = dao.addOrder(order);

        added.setCustomerName("New Name");

        dao.editOrder(added);

        OrderDAO reloadedDao = new OrderDAOImpl(testDir.toString() + java.io.File.separator);
        List<Order> updated = reloadedDao.getOrders(date);

        assertTrue(
                updated.stream()
                        .anyMatch(o -> o.getCustomerName().equals("New Name"))
        );
    }
}