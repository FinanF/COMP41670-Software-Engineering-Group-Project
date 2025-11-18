package org.example.util;

import java.util.Scanner;

/**
 * Utility class for reading and validating user input from console.
 * Provides safe methods to read different data types with validation.
 * User can type 'quit' or 'q' at any prompt to exit.
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
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Check for quit
            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                System.exit(0);  // ← Just exit!
            }

            try {
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.printf("Error: Value must be between %.2f and %.2f%n", min, max);
                    continue;
                }
                return value;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid number (or 'quit' to exit)");
                scanner.nextLine();  // Clear invalid input
            }
        }
    }

    /**
     * Read an integer value from user input with validation.
     */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Check for quit
            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                System.exit(0);  // ← Exit immediately
            }

            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.printf("Error: Value must be between %d and %d%n", min, max);
                    continue;
                }
                return value;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer (or 'quit' to exit)");
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

            // Check for quit
            switch (response) {
                case "quit", "q" -> System.exit(0);  // ← Exit immediately
                case "y", "yes" -> {
                    return true;
                }
                case "n", "no" -> {
                    return false;
                }
                default -> System.out.println("Error: Please enter 'y' or 'n'");
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

            // Check for quit
            if (value.equalsIgnoreCase("quit") || value.equalsIgnoreCase("q")) {
                System.exit(0);  // ← Exit immediately
            }

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