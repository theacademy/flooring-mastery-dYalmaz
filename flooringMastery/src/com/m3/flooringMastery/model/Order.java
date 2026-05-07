package com.m3.flooringMastery.model;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    int orderNumber;
    String customerName;
    Date orderDate;
    String state;
    BigDecimal taxRate;
    BigDecimal area;
    BigDecimal materialCost;
    BigDecimal laborCost;
    BigDecimal tax;
    BigDecimal total;
    String productType;
}
