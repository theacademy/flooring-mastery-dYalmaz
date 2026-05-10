package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.ProductDAO;
import com.m3.flooringMastery.dao.ProductPersistenceException;
import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductDAOSTUBImpl implements ProductDAO {

    @Override
    public BigDecimal getCostPerSquareFoot(String productType) throws ProductPersistenceException {
        if (productType == null) return null;
        if (!productType.equals("Wood")) return null;
        return new BigDecimal("5.15");
    }

    @Override
    public BigDecimal getLaborCostPerSquareFoot(String productType) throws ProductPersistenceException {
        if (productType == null) return null;
        if (!productType.equals("Wood")) return null;
        return new BigDecimal("4.75");
    }

    @Override
    public String getProductType(String productType) throws ProductPersistenceException {
        if (productType == null) {
            throw new ProductPersistenceException("Product type required");
        }
        // Mimic real DAO: case-sensitive exact match for this stub
        if (!productType.equals("Wood")) {
            throw new ProductPersistenceException("Invalid product: " + productType);
        }
        return "Wood";
    }

    @Override
    public List<Product> getAllProducts() throws ProductPersistenceException {

        Product product = new Product(
                "Wood",
                new BigDecimal("5.15"),
                new BigDecimal("4.75")
        );

        return List.of(product);
    }
}