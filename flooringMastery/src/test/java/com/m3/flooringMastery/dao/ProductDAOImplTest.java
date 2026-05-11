package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.ProductDAO;
import com.m3.flooringMastery.dao.ProductDAOImpl;
import com.m3.flooringMastery.dao.ProductPersistenceException;
import com.m3.flooringMastery.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAOImplTest {
	private static final Path TARGET_PRODUCT_FILE = Path.of("flooringMasteryTestData", "Data", "Products.txt");
	private static final Path DAO_PRODUCT_FILE = Path.of("Data", "Products.txt");

	private ProductDAO dao() {
		return new ProductDAOImpl();
	}

	private void ensureProductFileIsAvailable() throws Exception {
		Path localSource = Path.of("Data", "Products.txt");
		Path sourceProductFile = Files.exists(localSource) ? localSource : TARGET_PRODUCT_FILE;

		assertTrue(Files.exists(sourceProductFile), "Could not locate a source Products.txt fixture.");

		Files.createDirectories(DAO_PRODUCT_FILE.getParent());
		if (!sourceProductFile.equals(DAO_PRODUCT_FILE)) {
			Files.copy(sourceProductFile, DAO_PRODUCT_FILE, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	@org.junit.jupiter.api.BeforeEach
	void setUp() throws Exception {
		ensureProductFileIsAvailable();
	}

	private Map<String, Product> productsByType(List<Product> products) {
		return products.stream().collect(Collectors.toMap(Product::getProductType, p -> p));
	}

	@Test
	void testGetAllProducts_ReturnsAllProducts() throws Exception {

		List<Product> products = dao().getAllProducts();

		assertNotNull(products);
		assertEquals(4, products.size());

		Map<String, Product> byType = productsByType(products);

		assertAll(
				() -> assertTrue(byType.containsKey("Carpet")),
				() -> assertTrue(byType.containsKey("Laminate")),
				() -> assertTrue(byType.containsKey("Tile")),
				() -> assertTrue(byType.containsKey("Wood")),
				() -> assertEquals(new BigDecimal("2.25"), byType.get("Carpet").getCostPerSquareFoot()),
				() -> assertEquals(new BigDecimal("2.10"), byType.get("Carpet").getLaborCostPerSquareFoot()),
				() -> assertEquals(new BigDecimal("1.75"), byType.get("Laminate").getCostPerSquareFoot()),
				() -> assertEquals(new BigDecimal("2.10"), byType.get("Laminate").getLaborCostPerSquareFoot()),
				() -> assertEquals(new BigDecimal("3.50"), byType.get("Tile").getCostPerSquareFoot()),
				() -> assertEquals(new BigDecimal("4.15"), byType.get("Tile").getLaborCostPerSquareFoot()),
				() -> assertEquals(new BigDecimal("5.15"), byType.get("Wood").getCostPerSquareFoot()),
				() -> assertEquals(new BigDecimal("4.75"), byType.get("Wood").getLaborCostPerSquareFoot())
		);
	}

	@Test
	void testGetCostPerSquareFoot_ReturnsExpectedValue() throws Exception {

		assertEquals(new BigDecimal("5.15"), dao().getCostPerSquareFoot("Wood"));
		assertEquals(new BigDecimal("2.25"), dao().getCostPerSquareFoot("Carpet"));
	}

	@Test
	void testGetLaborCostPerSquareFoot_ReturnsExpectedValue() throws Exception {

		assertEquals(new BigDecimal("4.75"), dao().getLaborCostPerSquareFoot("Wood"));
		assertEquals(new BigDecimal("4.15"), dao().getLaborCostPerSquareFoot("Tile"));
	}

	@Test
	void testGetProductType_ReturnsExpectedType() throws Exception {

		assertEquals("Wood", dao().getProductType("Wood"));
		assertEquals("Tile", dao().getProductType("Tile"));
	}

	@Test
	void testUnknownProductReturnsNullForCosts() throws Exception {

		assertNull(dao().getCostPerSquareFoot("Vinyl"));
		assertNull(dao().getLaborCostPerSquareFoot("Vinyl"));
	}

	@Test
	void testNullInputReturnsNullForCostLookups() throws Exception {

		assertNull(dao().getCostPerSquareFoot(null));
		assertNull(dao().getLaborCostPerSquareFoot(null));
	}

	@Test
	void testCaseMismatchIsTreatedAsUnknownProduct() throws Exception {

		assertNull(dao().getCostPerSquareFoot("wood"));
		assertNull(dao().getLaborCostPerSquareFoot("wood"));
		assertThrows(ProductPersistenceException.class, () -> dao().getProductType("wood"));
	}

	@Test
	void testUnknownProductTypeThrowsException() {

		assertThrows(ProductPersistenceException.class, () -> dao().getProductType("Vinyl"));
	}

	@Test
	void testMissingProductFileThrowsPersistenceException() throws Exception {

		Path tempProductFile = Files.createTempDirectory("product-dao-test").resolve("missing-products.txt");

		assertFalse(Files.exists(tempProductFile));
		ProductDAO missingFileDao = new ProductDAOImpl(tempProductFile);
		assertThrows(ProductPersistenceException.class, missingFileDao::getAllProducts);
	}
}
