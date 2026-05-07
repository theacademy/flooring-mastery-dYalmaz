package com.m3.flooringMastery.dao;

import com.m3.flooringMastery.model.Product;
import com.m3.flooringMastery.model.Tax;

import java.math.BigDecimal;
import java.util.*;

public class TaxDAOImpl implements TaxDAO {

     public static final String TAX_FILE = "Data/Taxes.txt";
     public static final String DELIMITER = ",";
     Map<Tax, BigDecimal> taxes = new HashMap<>();
     Tax tax;


    @Override
    public BigDecimal getTaxRate(String stateAbbreviation) throws TaxPersistenceException {
        loadTaxes();
        for (Tax tax : taxes.keySet()) {
            if (tax.getStateAbbreviation().equalsIgnoreCase(stateAbbreviation)) {
                return tax.getTaxRate();
            }
        }
        return null;
    }

    @Override
    public boolean stateExists(String stateAbbreviation) throws TaxPersistenceException {
        loadTaxes();
        for (Tax tax : taxes.keySet()) {
            if (tax.getStateAbbreviation().equalsIgnoreCase(stateAbbreviation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Tax> getAllTaxes() throws TaxPersistenceException {
        loadTaxes();
        return new ArrayList<>(taxes.keySet());
    }

    private void loadTaxes() throws TaxPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new java.io.BufferedReader(new java.io.FileReader(TAX_FILE)));
        } catch (java.io.FileNotFoundException e) {
            throw new TaxPersistenceException("Could not load tax data.", e);
        }
            String currentLine;
            String[] currentTokens;

            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentTokens = currentLine.split(DELIMITER);

                tax = new Tax(currentTokens[0]);
                tax.setStateName(currentTokens[1]);
                tax.setTaxRate(new BigDecimal(currentTokens[2]));
                taxes.put(tax,tax.getTaxRate());
            }
            scanner.close();
    }
}
