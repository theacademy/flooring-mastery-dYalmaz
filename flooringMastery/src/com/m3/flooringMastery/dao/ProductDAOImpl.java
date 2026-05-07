package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAOImpl implements ProductDAO {

    String PRODUCT_FILE;
    Map<String, Product> products = new HashMap<>();

    @Override
    public BigDecimal getCostPerSquareFoot(String productType) {
        return null;
    }

    @Override
    public BigDecimal getLaborCostPerSquareFoot(String productType) {
        return null;
    }

    @Override
    public String getProductType(String productType) {
        return "";
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }
}
