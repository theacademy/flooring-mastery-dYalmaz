package com.m3.flooringMastery.view;

import java.math.BigDecimal;

public interface UserIO {

    void displayMessage(String message);

    String readString(String prompt);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    BigDecimal readBigDecimal(String prompt);
    BigDecimal readBigDecimal(String prompt, BigDecimal min);

}
