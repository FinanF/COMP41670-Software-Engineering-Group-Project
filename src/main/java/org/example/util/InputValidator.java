package org.example.util;

import java.util.Scanner;

/**
 * Utility class for reading and validating user input from console.
 * Provides safe methods to read different data types with validation.
 */
public class InputValidator {

    private final Scanner scanner;

    public InputValidator() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Read a double value from user input with validation.
     * Keeps prompting until valid input is received.
     *
     * @param prompt The prompt to display
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return Valid double value within range
     */
    public double readDouble(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();  // Clear newline

                if (value < min || value > max) {
                    System.out.printf("Error: Value must be between %.2f and %.2f%n", min, max);
                    continue;
                }

                return value;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid decimal number");
                scanner.nextLine();  // Clear invalid input
            }
        }
    }

    /**
     * Read an integer value from user input with validation.
     */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();  // Clear newline

                if (value < min || value > max) {
                    System.out.printf("Error: Value must be between %d and %d%n", min, max);
                    continue;
                }

                return value;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer");
                scanner.nextLine();  // Clear invalid input
            }
        }
    }

    /**
     * Read a yes/no response from user.
     *
     * @param prompt The prompt to display
     * @return true if user enters 'y' or 'yes', false for 'n' or 'no'
     */
    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String response = scanner.nextLine().toLowerCase().trim();

            if (response.equals("y") || response.equals("yes")) {
                return true;
            } else if (response.equals("n") || response.equals("no")) {
                return false;
            } else {
                System.out.println("Error: Please enter 'y' or 'n'");
            }
        }
    }

    /**
     * Read a string input from user.
     */
    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("Error: Input cannot be empty");
                continue;
            }

            return value;
        }
    }

    /**
     * Close the scanner and free resources.
     */
    public void close() {
        scanner.close();
    }
}