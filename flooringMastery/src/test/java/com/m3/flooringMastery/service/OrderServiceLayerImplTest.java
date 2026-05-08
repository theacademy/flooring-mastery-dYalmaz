package test.java.com.m3.flooringMastery.service;

import com.m3.flooringMastery.service.OrderServiceLayer;
import com.m3.flooringMastery.service.OrderServiceLayerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.m3.flooringMastery.dao.*;
import com.m3.flooringMastery.model.Order;
import test.java.com.m3.flooringMastery.dao.OrderDAOSTUBImpl;
import test.java.com.m3.flooringMastery.dao.ProductDAOSTUBImpl;
import test.java.com.m3.flooringMastery.dao.TaxDAOSTUBImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class OrderServiceLayerImplTest {

    private OrderServiceLayer service;

    @BeforeEach
    void setUp() {
        OrderDAO orderDAO = new OrderDAOSTUBImpl();
        ProductDAO productDAO = new ProductDAOSTUBImpl();
        TaxDAO taxDAO = new TaxDAOSTUBImpl();

        service = new OrderServiceLayerImpl(orderDAO, productDAO, taxDAO);
    }

    // --------------------------------------------------
    // 1. CREATE ORDER - HAPPY PATH
    // --------------------------------------------------
    @Test
    void testCreateOrder_Valid() throws Exception {

        Order order = new Order();
        order.setOrderDate(LocalDate.of(2026, 6, 6));
        order.setCustomerName("Cengiz");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));

        service.createOrder(order);

        assertTrue(order.getOrderNumber() > 0);
        assertEquals(new BigDecimal("100"), order.getArea());
        assertNotNull(order.getTaxRate());
        assertNotNull(order.getTotal());
    }

    // --------------------------------------------------
    // 2. INVALID STATE
    // --------------------------------------------------
    @Test
    void testCreateOrder_InvalidState() {

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setCustomerName("Test");
        order.setState("XX");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));

        assertThrows(TaxPersistenceException.class, () -> {
            service.createOrder(order);
        });
    }

    // --------------------------------------------------
    // 3. INVALID PRODUCT
    // --------------------------------------------------
    @Test
    void testCreateOrder_InvalidProduct() {

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setCustomerName("Test");
        order.setState("CA");
        order.setProductType("INVALID_PRODUCT");
        order.setArea(new BigDecimal("100"));

        assertThrows(ProductPersistenceException.class, () -> {
            service.createOrder(order);
        });
    }

    // --------------------------------------------------
    // 4. EDIT ORDER - SHOULD RECALCULATE
    // --------------------------------------------------
    @Test
    void testEditOrder_Recalculates() throws Exception {

        Order order = new Order();
        order.setOrderDate(LocalDate.of(2026, 6, 6));
        order.setCustomerName("Cengiz");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));

        service.createOrder(order);

        order.setCustomerName("Updated");
        order.setArea(new BigDecimal("150"));

        service.editOrder(order);

        assertEquals("Updated", order.getCustomerName());
        assertEquals(new BigDecimal("150"), order.getArea());
        assertNotNull(order.getTotal());
    }

    // --------------------------------------------------
    // 5. GET ORDERS BY DATE
    // --------------------------------------------------
    @Test
    void testGetOrdersByDate() throws Exception {

        LocalDate date = LocalDate.of(2026, 6, 6);

        Order order = new Order();
        order.setOrderDate(date);
        order.setCustomerName("Test");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("120"));

        service.createOrder(order);

        List<Order> orders = service.getOrdersByDate(date);

        assertFalse(orders.isEmpty());
    }

    // --------------------------------------------------
    // 6. REMOVE ORDER
    // --------------------------------------------------
    @Test
    void testRemoveOrder() throws Exception {

        Order order = new Order();
        order.setOrderDate(LocalDate.of(2026, 6, 6));
        order.setCustomerName("Test");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("120"));

        service.createOrder(order);

        int id = order.getOrderNumber();

        service.removeOrder(id, order.getOrderDate());

        // just ensure no exception + method runs
        assertTrue(true);
    }

    // --------------------------------------------------
    // 7. EDGE CASE - SMALL AREA SHOULD STILL WORK IF VALID
    // --------------------------------------------------
    @Test
    void testCreateOrder_MinAreaBoundary() throws Exception {

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setCustomerName("Edge");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100")); // boundary

        service.createOrder(order);

        assertEquals(new BigDecimal("100"), order.getArea());
        assertNotNull(order.getTotal());
    }
}