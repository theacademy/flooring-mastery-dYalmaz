package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.OrderDAO;
import com.m3.flooringMastery.dao.OrderDAOImpl;
import com.m3.flooringMastery.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOImplTest {

    private final String TEST_DIR = "flooringMastery/Orders/";

    private OrderDAO dao;
    // Copy fresh test data every time (CRITICAL)

    @BeforeEach
    void setUp() throws Exception {

        File orderDir = new File(TEST_DIR);
        orderDir.mkdirs();

        // CLEAN ONLY order output files
        for (File f : orderDir.listFiles()) {
            if (f != null) f.delete();
        }

        // COPY from stable test data location
        copyFile(
                "flooringMastery/test-data/TestProducts.txt",
                "flooringMastery/TestProducts.txt"
        );

        copyFile(
                "flooringMastery/test-data/TestTaxes.txt",
                "flooringMastery/TestTaxes.txt"
        );

        dao = new OrderDAOImpl(TEST_DIR);
    }

    private void copyFile(String source, String dest) throws Exception {
        java.nio.file.Files.copy(
                java.nio.file.Paths.get(source),
                java.nio.file.Paths.get(dest),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );
    }

    // --------------------------------------------------
    // ADD ORDER
    // --------------------------------------------------
    @Test
    void testAddOrder_CreatesAndSavesOrder() throws Exception {

        Order order = new Order();
        order.setOrderDate(LocalDate.of(2026, 6, 6));
        order.setCustomerName("Test User");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("120"));

        Order added = dao.addOrder(order);

        assertNotNull(added);
        assertTrue(added.getOrderNumber() > 0);

        List<Order> orders = dao.getOrders(order.getOrderDate());

        assertEquals(1, orders.size());
        assertEquals("Test User", orders.get(0).getCustomerName());
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

        Order order = new Order();
        order.setOrderDate(date);
        order.setCustomerName("Remove Me");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("150"));

        Order added = dao.addOrder(order);

        dao.removeOrder(date, added.getOrderNumber());

        List<Order> updated = dao.getOrders(date);

        assertTrue(
                updated.stream()
                        .noneMatch(o -> o.getOrderNumber() == added.getOrderNumber())
        );
    }

    // --------------------------------------------------
    // EDIT ORDER
    // --------------------------------------------------
    @Test
    void testEditOrder_UpdatesOrder() throws Exception {

        LocalDate date = LocalDate.of(2026, 6, 6);

        Order order = new Order();
        order.setOrderDate(date);
        order.setCustomerName("Old Name");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("150"));

        Order added = dao.addOrder(order);

        added.setCustomerName("New Name");

        dao.editOrder(added);

        List<Order> updated = dao.getOrders(date);

        assertTrue(
                updated.stream()
                        .anyMatch(o -> o.getCustomerName().equals("New Name"))
        );
    }
}