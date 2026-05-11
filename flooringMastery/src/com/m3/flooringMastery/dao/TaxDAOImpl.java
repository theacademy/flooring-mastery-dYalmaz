package com.m3.flooringMastery.dao;


import com.m3.flooringMastery.model.Tax;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

/**
 * TaxDAOImpl - Data Access Object for managing tax information by state.
 * This class handles loading and retrieving tax rates for different states.
 * It provides validation of state abbreviations and access to all available tax data.
 */
public class TaxDAOImpl implements TaxDAO {

     public static final String TAX_FILE = "Data/Taxes.txt";
     public static final String DELIMITER = ":;:";
    private final Path taxFile;
    Map<String, Tax> taxes = new HashMap<>();

    public TaxDAOImpl() {
        this(Path.of(TAX_FILE));
    }

    public TaxDAOImpl(String taxFile) {
        this(Path.of(taxFile));
    }

    public TaxDAOImpl(Path taxFile) {
        this.taxFile = taxFile;
    }


    @Override
    public BigDecimal getTaxRate(String stateAbbreviation)
            throws TaxPersistenceException {

        loadTaxes();

        Tax tax = taxes.get(stateAbbreviation.toUpperCase());

        if (tax != null) {
            return tax.getTaxRate();
        }

        return null;
    }

    @Override
    public boolean stateExists(String stateAbbreviation)
            throws TaxPersistenceException {

        loadTaxes();

        return taxes.containsKey(stateAbbreviation.toUpperCase());
    }

    @Override
    public List<Tax> getAllTaxes() throws TaxPersistenceException {
        loadTaxes();
        return new ArrayList<>(taxes.values());
    }

    private void loadTaxes() throws TaxPersistenceException {

        taxes.clear();

        Scanner scanner;

        try {
            scanner = new Scanner(
                    new java.io.BufferedReader(
                            new java.io.FileReader(taxFile.toFile())));
        } catch (java.io.FileNotFoundException e) {
            throw new TaxPersistenceException("Could not load tax data.", e);
        }

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // skip header (IMPORTANT if present)
        }

        while (scanner.hasNextLine()) {

            String currentLine = scanner.nextLine();
            String[] currentTokens = currentLine.split(DELIMITER);

            Tax tax = new Tax(currentTokens[0]);
            tax.setStateName(currentTokens[1]);
            tax.setTaxRate(new BigDecimal(currentTokens[2]));

            taxes.put(tax.getStateAbbreviation().toUpperCase(), tax);
        }

        scanner.close();
    }

}
