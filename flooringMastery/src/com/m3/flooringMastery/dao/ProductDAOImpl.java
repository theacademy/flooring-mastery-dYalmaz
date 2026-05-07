package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductDAOImpl implements ProductDAO {

    public static final String PRODUCT_FILE = "Data/Products.txt";
    public static final String DELIMITER = ",";
    Map<String, Product> products = new HashMap<>();

    @Override
    public BigDecimal getCostPerSquareFoot(String productType) throws ProductPersistenceException {
        loadProducts();
        if (products.containsKey(productType)) {
            return products.get(productType).getCostPerSquareFoot();
        } else {
            return null;
        }

    }

    @Override
    public BigDecimal getLaborCostPerSquareFoot(String productType) throws ProductPersistenceException {
        loadProducts();
        if (products.containsKey(productType)) {
            return products.get(productType).getLaborCostPerSquareFoot();
        } else {
            return null;
        }
    }

    @Override
    public String getProductType(String productType) throws ProductPersistenceException {
        loadProducts();
        if (products.containsKey(productType)) {
            return products.get(productType).getProductType();
        } else {
            throw new ProductPersistenceException("No such product type.", new FileNotFoundException());
        }
    }

    @Override
    public List<Product> getAllProducts() throws ProductPersistenceException {
        loadProducts();
        return new ArrayList<>(products.values());

    }

    private void loadProducts() throws ProductPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new ProductPersistenceException("Could not load product data into memory.", e);
        }

        String currentLine;
        String[] currentTokens;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);

            Product productFromFile = new Product(currentTokens[0]);
            productFromFile.setCostPerSquareFoot(new BigDecimal(currentTokens[1]));
            productFromFile.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[2]));

            products.put(productFromFile.getProductType(), productFromFile);
        }
        scanner.close();
    }

}
