package com.m3.flooringMastery.dao;


import com.m3.flooringMastery.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

/**
 * ProductDAOImpl - Data Access Object for managing product data.
 * This class handles loading and retrieving product information from a data file.
 * It provides access to product types and their associated costs (material and labor) per square foot.
 */
public class ProductDAOImpl implements ProductDAO {

    public static final String PRODUCT_FILE = "Data/Products.txt";
    public static final String DELIMITER = ":;:";
    private final Path productFile;
    Map<String, Product> products = new HashMap<>();

    public ProductDAOImpl() {
        this(Path.of(PRODUCT_FILE));
    }

    public ProductDAOImpl(Path productFile) {
        this.productFile = productFile;
    }

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
            throw new ProductPersistenceException("No such product type.");
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
            scanner = new Scanner(new BufferedReader(new FileReader(productFile.toFile())));
        } catch (FileNotFoundException e) {
            throw new ProductPersistenceException("Could not load product data into memory.");
        }

        products.clear();

        // skip header
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {

            String currentLine = scanner.nextLine();
            String[] currentTokens = currentLine.split(DELIMITER);

            String productType = currentTokens[0];
            BigDecimal costPerSquareFoot = new BigDecimal(currentTokens[1]);
            BigDecimal laborCostPerSquareFoot = new BigDecimal(currentTokens[2]);

            Product productFromFile = new Product(
                    productType,
                    costPerSquareFoot,
                    laborCostPerSquareFoot
            );

            products.put(productType, productFromFile);
        }

        scanner.close();
    }

}
