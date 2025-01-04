package org.confectionery.UI;

import org.confectionery.Controller.ConfectioneryController;
import org.confectionery.Domain.Admin;
import org.confectionery.Domain.Client;
import org.confectionery.Domain.User;
import org.confectionery.Exception.*;

import java.util.List;
import java.util.Scanner;

public class ConfectioneryUI {
    private final ConfectioneryController controller;
    private final Scanner scanner;
    private User loggedUser = null;

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
                        2. Login
                        0. Exit
                        Please select an option:
                        """);

                String option = scanner.nextLine();


                if (!isValidOption(option, "1", "2", "3", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }

                switch (option) {
                    case "1":
                        createAccount();
                        break;
                    case "2":
                        if (loginUser()) {
                            if (loggedUser instanceof Admin) {
                                statusOn(loggedUser.getID());
                                adminMenu();
                            } else clientMenu();
                        }
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

    private void statusOn(Integer id) {
        controller.updateStatusOn(id);
    }

    private void statusOff(Integer id) {
        controller.updateStatusOff(id);
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
                    case "1":
                        break;
                    case "2":
                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                    case "5":
                        viewProfile();
                        break;
                    case "6":
                        break;
                    case "7":
                        break;
                    case "0":
                        loggedUser = null;
                        clientRunning = false;
                        break;
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
                    case "1":
                        balanceInformationMenu();
                        break;
                    case "2":
                        clientManagementMenu();
                        break;
                    case "3":
                        productManagementMenu();
                        break;
                    case "4":
                        viewProfile();
                        break;
                    case "5":
                        break;
                    case "0":
                        statusOff(loggedUser.getID());
                        loggedUser = null;
                        adminRunning = false;
                        break;
                    default:
                        throw new ValidationException("Invalid option. Please choose one of the provided options.");
                }
            } catch (ValidationException | BusinessLogicException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void viewProfile() {
        System.out.println("Here are details about your profile:");
        System.out.println(loggedUser.toString());
    }

    private void clientManagementMenu() {
        boolean clientManagementRunning = true;
        while (clientManagementRunning) {
            try {
                System.out.print("""
                        Client Management Menu:
                        1. View All Users
                        2. View Client with the most points
                        3. View Clients with their Orders
                        4. Delete Client
                        0. Exit Menu
                        """);
                String option = scanner.nextLine();
                if (!isValidOption(option, "1", "2", "3", "4", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1":
                        viewUsers();
                        break;
                    case "2":
                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                    case "0":
                        clientManagementRunning = false;
                    default:
                        throw new ValidationException("Invalid option. Please choose one of the provided options.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void productManagementMenu() {
        boolean productManagementRunning = true;
        while (productManagementRunning) {
            try {
                System.out.print("""
                        Product Management Menu:
                        1. Create Product
                        2. Update Product
                        3. Delete Product
                        0. Exit Menu
                        """);
                String option = scanner.nextLine();
                if (!isValidOption(option, "1", "2", "3", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1":
                        break;
                    case "2":
                        break;
                    case "3":
                        break;
                    case "0":
                        productManagementRunning = false;
                    default:
                        throw new ValidationException("Invalid option. Please choose one of the provided options.");
                }
            } catch (BusinessLogicException | ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void balanceInformationMenu() {
        boolean balanceInformationRunning = true;
        while (balanceInformationRunning) {
            try {
                System.out.print("""
                        Balance Information Menu:
                        1. View Total Balance
                        2. View Monthly Balance
                        3. View Yearly Balance
                        0. Exit Menu
                        """);
                String option = scanner.nextLine();
                if (!isValidOption(option, "1", "2", "3", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1":
                        break;
                    case "2":
                        break;
                    case "3":
                        break;
                    case "0":
                        balanceInformationRunning = false;
                }

            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void createAccount() {
        System.out.println("Choose your role: Admin or Client.");
        String role = scanner.nextLine();
        if (role.equals("Admin")) {
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            System.out.println("Enter your email:");
            String email;
            while (true) {
                try {
                    email = scanner.nextLine();
                    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                    if (email.matches(emailRegex))
                        break;
                    else
                        throw new InvalidFormatException("Invalid email format. Please try again.");
                } catch (InvalidFormatException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Enter your email:");
                }
            }
            System.out.println("Enter your password:");
            String password = scanner.nextLine();
            try {
                Admin admin = controller.createAdmin(name, email, password);
                if (admin != null)
                    System.out.println("Admin with id " + admin.getID() + " created successfully!");
                else throw new FailedAccountCreationException("Account creation failed.");
            } catch (FailedAccountCreationException e) {
                System.out.println(e.getMessage());
            }
        } else if (role.equals("Client")) {
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            System.out.println("Enter your address:");
            String address = scanner.nextLine();
            System.out.println("Enter your email:");
            String email;
            while (true) {
                try {
                    email = scanner.nextLine();
                    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                    if (email.matches(emailRegex))
                        break;
                    else
                        throw new InvalidFormatException("Invalid email format. Please try again.");
                } catch (InvalidFormatException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Enter your email:");
                }
            }
            System.out.println("Enter your password:");
            String password = scanner.nextLine();
            try {
                Client client = controller.createClient(name, email, password, address);
                if (client != null)
                    System.out.println("Client with id " + client.getID() + " created successfully!");
                else throw new FailedAccountCreationException("Account creation failed.");
            } catch (FailedAccountCreationException e) {
                System.out.println(e.getMessage());
            }
        } else System.out.println("Choose one of the provided roles!");
    }

    private boolean loginUser() {
        System.out.println("Welcome to the Login Page!\n");
        System.out.println("Please enter your email:");
        String email;
        while (true) {
            try {
                email = scanner.nextLine();
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                if (email.matches(emailRegex))
                    break;
                else
                    throw new InvalidFormatException("Invalid email format. Please try again.");
            } catch (InvalidFormatException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter your email:");
            }
        }
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        try {
            this.loggedUser = controller.loginUser(email, password);
            if (loggedUser != null) {
                System.out.println("Welcome back, " + loggedUser.getName() + "!");
                return true;
            } else throw new FailedLoginAttempt("Failed to log in.");
        } catch (FailedLoginAttempt e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void viewUsers() {
        for (User user : controller.getUsers()) {
            System.out.println(user.toString());
        }
    }
}
