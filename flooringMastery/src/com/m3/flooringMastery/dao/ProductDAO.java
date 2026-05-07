package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDAO {

        public BigDecimal getCostPerSquareFoot(String productType) throws ProductPersistenceException;
        public BigDecimal getLaborCostPerSquareFoot(String productType) throws ProductPersistenceException;
        public String getProductType(String productType) throws ProductPersistenceException;
        public List<Product> getAllProducts() throws ProductPersistenceException;

}
