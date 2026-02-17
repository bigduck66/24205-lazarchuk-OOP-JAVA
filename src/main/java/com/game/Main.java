package com.game;

import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int hiddenNumber = (int)(Math.random() * 9000) + 1000;
        int estimatedNumber = 0;
        System.out.println(hiddenNumber);
        CheckingMatches checker = new CheckingMatches();

        while (hiddenNumber != estimatedNumber) {
            estimatedNumber = scanner.nextInt();
            if (estimatedNumber < 1000 || estimatedNumber > 9999){
                System.out.println("only 4-digit numbers");
            }
            checker.count(hiddenNumber, estimatedNumber);
            System.out.println("Bulls " + checker.getBulls());
            System.out.println("Cows " + checker.getCows());
        }
        scanner.close();
        System.out.println("win");
    }
}