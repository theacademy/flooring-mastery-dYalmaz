package test.java.com.m3.flooringMastery.dao;

import com.m3.flooringMastery.dao.ProductDAO;
import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductDAOSTUBImpl implements ProductDAO {

    @Override
    public BigDecimal getCostPerSquareFoot(String productType) {
        return new BigDecimal("5.15");
    }

    @Override
    public BigDecimal getLaborCostPerSquareFoot(String productType) {
        return new BigDecimal("4.75");
    }

    @Override
    public String getProductType(String productType) {
        return "Wood";
    }

    @Override
    public List<Product> getAllProducts() {

        Product product = new Product(
                "Wood",
                new BigDecimal("5.15"),
                new BigDecimal("4.75")
        );

        return List.of(product);
    }
}