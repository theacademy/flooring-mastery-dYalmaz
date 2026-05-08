package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Product;
import com.m3.flooringMastery.model.Tax;

import java.math.BigDecimal;
import java.util.*;

public class TaxDAOImpl implements TaxDAO {

     public static final String TAX_FILE = "flooringMastery/Data/Taxes.txt";
     public static final String DELIMITER = ":;:";
    Map<String, Tax> taxes = new HashMap<>();
     Tax tax;


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
                            new java.io.FileReader(TAX_FILE)));
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
