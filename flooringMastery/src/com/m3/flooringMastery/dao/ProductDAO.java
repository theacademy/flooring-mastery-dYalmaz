package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDAO {

        public BigDecimal getCostPerSquareFoot(String productType);
        public BigDecimal getLaborCostPerSquareFoot(String productType);
        public String getProductType(String productType);
        public List<Product> getAllProducts();

}
