package com.m3.flooringMastery.dao;

/**
 * ProductDAO - Interface defining contract for product data access operations.
 * Specifies methods for retrieving product information including costs and all available products.
 */

import com.m3.flooringMastery.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDAO {

        public BigDecimal getCostPerSquareFoot(String productType) throws ProductPersistenceException;
        public BigDecimal getLaborCostPerSquareFoot(String productType) throws ProductPersistenceException;
        public String getProductType(String productType) throws ProductPersistenceException;
        public List<Product> getAllProducts() throws ProductPersistenceException;

}
