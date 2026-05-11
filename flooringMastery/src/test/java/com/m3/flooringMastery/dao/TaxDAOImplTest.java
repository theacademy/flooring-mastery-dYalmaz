package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.TaxDAO;
import com.m3.flooringMastery.dao.TaxDAOImpl;
import com.m3.flooringMastery.dao.TaxPersistenceException;
import com.m3.flooringMastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TaxDAOImplTest {

	private static final Path TARGET_TAX_FILE = Path.of("flooringMasteryTestData", "Data", "Taxes.txt");

	private TaxDAO dao;

	@BeforeEach
	void setUp() throws Exception {
		Files.createDirectories(TARGET_TAX_FILE.getParent());
		Files.copy(Path.of("Data", "Taxes.txt"), TARGET_TAX_FILE, StandardCopyOption.REPLACE_EXISTING);
		dao = new TaxDAOImpl();
	}

	private Map<String, Tax> taxesByState(List<Tax> taxes) {
		return taxes.stream().collect(Collectors.toMap(Tax::getStateAbbreviation, t -> t));
	}

	@Test
	void testGetAllTaxes_ReturnsAllTaxes() throws Exception {

		List<Tax> taxes = dao.getAllTaxes();

		assertNotNull(taxes);
		assertEquals(4, taxes.size());

		Map<String, Tax> byState = taxesByState(taxes);

		assertAll(
				() -> assertTrue(byState.containsKey("TX")),
				() -> assertTrue(byState.containsKey("WA")),
				() -> assertTrue(byState.containsKey("KY")),
				() -> assertTrue(byState.containsKey("CA")),
				() -> assertEquals("Texas", byState.get("TX").getStateName()),
				() -> assertEquals(new BigDecimal("4.45"), byState.get("TX").getTaxRate()),
				() -> assertEquals("Washington", byState.get("WA").getStateName()),
				() -> assertEquals(new BigDecimal("9.25"), byState.get("WA").getTaxRate()),
				() -> assertEquals("Kentucky", byState.get("KY").getStateName()),
				() -> assertEquals(new BigDecimal("6.00"), byState.get("KY").getTaxRate()),
				() -> assertEquals("Calfornia", byState.get("CA").getStateName()),
				() -> assertEquals(new BigDecimal("25.00"), byState.get("CA").getTaxRate())
		);
	}

	@Test
	void testGetTaxRate_ReturnsExpectedRate() throws Exception {

		assertEquals(new BigDecimal("4.45"), dao.getTaxRate("TX"));
		assertEquals(new BigDecimal("9.25"), dao.getTaxRate("wa"));
		assertEquals(new BigDecimal("25.00"), dao.getTaxRate("Ca"));
	}

	@Test
	void testStateExists_ReturnsTrueForKnownStates() throws Exception {

		assertTrue(dao.stateExists("TX"));
		assertTrue(dao.stateExists("wa"));
		assertTrue(dao.stateExists("ky"));
		assertTrue(dao.stateExists("Ca"));
	}

	@Test
	void testUnknownStateReturnsNullOrFalse() throws Exception {

		assertNull(dao.getTaxRate("NV"));
		assertFalse(dao.stateExists("NV"));
	}

	@Test
	void testNullInputThrowsNullPointerException() {

		assertThrows(NullPointerException.class, () -> dao.getTaxRate(null));
		assertThrows(NullPointerException.class, () -> dao.stateExists(null));
	}

	@Test
	void testMissingTaxFileThrowsPersistenceException() throws Exception {

		Path missingTaxFile = Files.createTempDirectory("tax-dao-test").resolve("missing-taxes.txt");

		assertFalse(Files.exists(missingTaxFile));
		TaxDAO missingFileDao = new TaxDAOImpl(missingTaxFile);
		assertThrows(TaxPersistenceException.class, missingFileDao::getAllTaxes);
	}
}
