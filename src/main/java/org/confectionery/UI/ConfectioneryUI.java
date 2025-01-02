package org.confectionery.UI;

import org.confectionery.Controller.ConfectioneryController;
import org.confectionery.Exception.BusinessLogicException;
import org.confectionery.Exception.ValidationException;

import java.util.Scanner;

public class ConfectioneryUI {
    private final ConfectioneryController controller;
    private final Scanner scanner;

    public ConfectioneryUI(ConfectioneryController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    private boolean isValidOption(String option, String... validOptions) {
        for (String validOption : validOptions) {
            if (option.equals(validOption)) {
                return true;
            }
        }
        return false;
    }

    public void start() {
        boolean running = true;

        while (running) {
            try {
                System.out.print("""
                    Welcome to the Confectionery App!
                    1. Create account
                    2. Login as Administrator
                    3. Login as Client
                    0. Exit
                    Please select an option:
                    """);

                String option = scanner.nextLine();


                if (!isValidOption(option, "1", "2", "3", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }

                switch (option) {
                    case "1":
                        break;
                    case "2":
                        adminMenu();
                        break;
                    case "3":
                        clientMenu();
                        break;
                    case "0":
                        System.out.println("Thank you for using Confectionery!");
                        running = false;
                        break;
                    default:
                        throw new ValidationException("Invalid option. Please select a valid option.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void clientMenu() {
        boolean clientRunning = true;
        while (clientRunning) {
            try {
                System.out.print("""
                    Client Menu:
                    1. View Menu order by Points
                    2. View Menu order by Price
                    3. Place Order
                    4. Generate Invoice
                    5. View Profile
                    6. View Drinks With Alcohol
                    7. View Products available until December 2024
                    0. Logout
                    Please select an option:
                    """);

                String option = scanner.nextLine().trim();

                if (!isValidOption(option, "1", "2", "3", "4", "5", "6", "7", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }


                switch (option) {
                    case "1": break;
                    case "2": break;
                    case "3": break;
                    case "4": break;
                    case "5": break;
                    case "6": break;
                    case "7": break;
                    case "0": clientRunning = false;
                    default:
                        throw new ValidationException("Invalid option. Please choose one of the provided options.");

                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private void adminMenu() {
        boolean adminRunning = true;
        while (adminRunning) {
            try {
                System.out.print("""
                    Admin Menu:
                    1. Balance Information
                    2. Client Management
                    3. Product Management
                    4. View Profile
                    5. Change Password
                    0. Logout
                    Please select an option:
                    """);

                String option = scanner.nextLine().trim();


                if (!isValidOption(option, "1", "2", "3", "4", "5", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }

                switch (option) {
                    case "1": balanceInformationMenu(); break;
                    case "2": clientManagementMenu(); break;
                    case "3": productManagementMenu(); break;
                    case "4": break;
                    case "5": break;
                    case "0": adminRunning = false;
                    default:
                        throw new ValidationException("Invalid option. Please choose one of the provided options.");
                }
            } catch (ValidationException | BusinessLogicException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void clientManagementMenu() {
        boolean clientManagementRunning = true;
        while (clientManagementRunning) {
            try {
                System.out.print("""
                        Client Management Menu:
                        1. View Clients by Address
                        2. View Client with the most points
                        3. View Clients with their Orders
                        4. Delete Client
                        0. Exit Menu
                        """);
                String option = scanner.nextLine().trim();
                if (!isValidOption(option, "1", "2", "3", "4", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1": break;
                    case "2": break;
                    case "3": break;
                    case "4": break;
                    case "0": clientManagementRunning = false;
                    default:
                        throw new ValidationException("Invalid option. Please choose one of the provided options.");
                }
            }
            catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void productManagementMenu() {
        boolean productManagementRunning = true;
        while (productManagementRunning) {
            try{
                System.out.print("""
                        Product Management Menu:
                        1. Create Product
                        2. Update Product
                        3. Delete Product
                        0. Exit Menu
                        """);
                String option = scanner.nextLine().trim();
                if (!isValidOption(option, "1", "2", "3", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1": break;
                    case "2": break;
                    case "3": break;
                    case "0": productManagementRunning = false;
                    default:
                        throw new ValidationException("Invalid option. Please choose one of the provided options.");
                }
            }
            catch (BusinessLogicException | ValidationException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void balanceInformationMenu(){
        boolean balanceInformationRunning = true;
        while (balanceInformationRunning) {
            try{
                System.out.print("""
                        Balance Information Menu:
                        1. View Total Balance
                        2. View Monthly Balance
                        3. View Yearly Balance
                        0. Exit Menu
                        """);
                String option = scanner.nextLine().trim();
                if (!isValidOption(option, "1", "2", "3", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1": break;
                    case "2": break;
                    case "3": break;
                    case "0": balanceInformationRunning = false;
                }

            }catch (ValidationException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

}
