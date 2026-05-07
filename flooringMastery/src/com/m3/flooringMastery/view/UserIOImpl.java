package com.m3.flooringMastery.view;

import java.util.Scanner;
import java.math.BigDecimal;

public class UserIOImpl implements UserIO{

        @Override
        public void displayMessage(String message) {
            System.out.println(message);
        }

        @Override
        public String readString(String prompt) {
            System.out.println(prompt);
            Scanner sc = new Scanner(System.in);
            return sc.nextLine();
        }

        @Override
        public int readInt(String prompt) {
            System.out.println(prompt);
            Scanner sc = new Scanner(System.in);
            return sc.nextInt();
        }

        @Override
        public int readInt(String prompt, int min, int max) {
            System.out.println(prompt);
            Scanner sc = new Scanner(System.in);
            int userInt = sc.nextInt();
            while(userInt<min || userInt>max){
                System.out.println(prompt);
                userInt = sc.nextInt();
            }
            return userInt;
        }

        @Override
        public java.math.BigDecimal readBigDecimal(String prompt) {
            System.out.println(prompt);
            Scanner sc = new Scanner(System.in);
            return sc.nextBigDecimal();
        }

        @Override
        public java.math.BigDecimal readBigDecimal(String prompt, java.math.BigDecimal min) {
            System.out.println(prompt);
            Scanner sc = new Scanner(System.in);
            BigDecimal userBD = sc.nextBigDecimal();
            while(userBD.compareTo(min)<0){
                System.out.println(prompt);
                userBD = sc.nextBigDecimal();
            }
            return userBD;
        }

        @Override
        public java.time.LocalDate readDate(String prompt) {
            System.out.println(prompt);
            Scanner sc = new Scanner(System.in);
            String userDate = sc.nextLine();
            return java.time.LocalDate.parse(userDate, java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }

}
