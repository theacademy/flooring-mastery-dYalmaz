package com.m3.flooringMastery.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.math.BigDecimal;

public class UserIOImpl implements UserIO{

private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("M/d/yyyy");

private final Scanner sc = new Scanner(System.in);


    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    @Override
    public int readInt(String prompt) {

        while (true) {
            try {
                System.out.println(prompt);
                return Integer.parseInt(sc.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from the menu.");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {

        while (true) {
            try {
                System.out.println(prompt);
                int value = Integer.parseInt(sc.nextLine().trim());

                if (value >= min && value <= max) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // ignore
            }

            System.out.println("Invalid choice. Enter a number between " + min + " and " + max);
        }
    }

    @Override
    public java.math.BigDecimal readBigDecimal(String prompt) {
        System.out.println(prompt);
        return new BigDecimal(sc.nextLine());
    }

    @Override
    public java.math.BigDecimal readBigDecimal(String prompt, java.math.BigDecimal min) {
        System.out.println(prompt);
        BigDecimal userBD = new BigDecimal(sc.nextLine());
        while(userBD.compareTo(min)<0){
            System.out.println(prompt);
            userBD = new BigDecimal(sc.nextLine());
        }
        return userBD;
    }



    @Override
    public LocalDate readDate(String prompt) {

        while (true) {
            try {
                System.out.println(prompt);
                String userDate = sc.nextLine();

                return LocalDate.parse(userDate, DATE_FORMATTER);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please use M/d/yyyy (e.g. 6/1/2013)");
            }
        }
    }

}
