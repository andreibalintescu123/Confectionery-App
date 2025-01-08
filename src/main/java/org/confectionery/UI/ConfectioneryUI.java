package org.confectionery.UI;

import org.confectionery.Controller.ConfectioneryController;
import org.confectionery.Domain.*;
import org.confectionery.Exception.*;

import java.time.LocalDate;
import java.util.ArrayList;
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


                if (!isValidOption(option, "1", "2", "0")) {
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
                        4. Delete Order
                        5. Generate Invoice
                        6. View Profile
                        7. View Drinks With Alcohol
                        8. View Products available at given date
                        0. Logout
                        Please select an option:
                        """);

                String option = scanner.nextLine().trim();

                if (!isValidOption(option, "1", "2", "3", "4", "5", "6", "7", "8", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }


                switch (option) {
                    case "1":
                        menuSortedByPoints();
                        break;
                    case "2":
                        menuSortedByPrice();
                        break;
                    case "3":
                        placeOrder();
                        break;
                    case "4":
                        deleteOrder();
                        break;
                    case "5":
                        generateInvoice();
                        break;
                    case "6":
                        viewProfile();
                        break;
                    case "7":
                        viewAlcoholicDrinks();
                        break;
                    case "8":
                        viewAvailableProducts();
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

    private void deleteOrder() {
        System.out.println("Enter the id of the order you would like to delete:");
        Integer id = Integer.parseInt(scanner.nextLine());
        if (controller.getOrder(id) != null) {
            controller.deleteOrder(id);
            System.out.println("Order with id " + id + " deleted successfully.");
        } else {
            System.out.println("Failed to delete order.");
        }

    }

    private void generateInvoice() {
        System.out.println("Generating invoice...\n");
        Integer clientId = loggedUser.getID();
        controller.generateInvoice(clientId);
    }
    private void placeOrder() {
        System.out.println("==== Place Order ====");
        System.out.println("You will first select cakes, followed by drinks.");
        System.out.println("Enter the IDs of the products you would like to order, one at a time.");
        System.out.println("Type 'done' when you have finished with a category or 'cancel' to abort the order.");

        // Separate lists for cakes and drinks
        List<Integer> cakeIds = new ArrayList<>();
        List<Integer> drinkIds = new ArrayList<>();

        // Input cakes
        System.out.println("\n--- Select Cakes ---");
        collectProductIDs(cakeIds);

        // Input drinks
        System.out.println("\n--- Select Drinks ---");
        collectProductIDs(drinkIds);

        // If both lists are empty, abort the order
        if (cakeIds.isEmpty() && drinkIds.isEmpty()) {
            System.out.println("No products selected. Aborting order.");
            return;
        }

        Integer clientId = loggedUser.getID(); // Get logged user ID

        try {
            // Call the controller to place the order
            Order order = controller.placeOrder(cakeIds, drinkIds, clientId);
            if (order != null) {
                System.out.println("Order with id " + order.getID() + " has been placed successfully!");
            } else {
                throw new ValidationException("Order could not be placed.");
            }
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Helper method to collect product IDs for a category.
     *
     * @param productIds List to store the product IDs.
     */
    private void collectProductIDs(List<Integer> productIds) {
        while (true) {
            System.out.print("Enter product ID: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break; // Move to the next category
            } else if (input.equalsIgnoreCase("cancel")) {
                System.out.println("Order cancelled.");
                productIds.clear(); // Clear the current category list
                return; // Exit the method
            } else {
                try {
                    int productId = Integer.parseInt(input);
                    productIds.add(productId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid product ID.");
                }
            }
        }
    }



    private void viewAvailableProducts() {
        System.out.println("Enter a valid date in the form Year-Month( January, February...)-Day(Tenth,Eleventh...)");
        String expirationDate = scanner.nextLine();
        Date date = Date.parse(expirationDate);
        List<Product> products = controller.getAvailableProducts(date);
        if(!products.isEmpty())
        for (Product product : products) {
            System.out.println(product);
        }
        else{
            System.out.println("No products available for the specified date.");
        }
    }

    private void viewAlcoholicDrinks() {
        List<Drink> drinks = controller.viewAlcoholicDrinks();
        for (Drink drink : drinks) {
            System.out.println(drink);
        }
    }

    private void menuSortedByPrice() {
        List<Product> products = controller.menuSortedByPrice();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void menuSortedByPoints() {
        List<Product> products = controller.menuSortedByPoints();
        for (Product product : products) {
            System.out.println(product);
        }
    }


    private void adminMenu() {
        boolean adminRunning = true;
        while (adminRunning) {
            try {
                System.out.print("""
                        Admin Menu:
                        1. Balance Information
                        2. User Management
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
                        userManagementMenu();
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

    private void userManagementMenu() {
        boolean clientManagementRunning = true;
        while (clientManagementRunning) {
            try {
                System.out.print("""
                        Client Management Menu:
                        1. View All Users
                        2. View All Clients
                        3. Get Client by id
                        4. Update Client
                        5. Delete Client
                        6. View Client with the most points
                        7. View Clients with their Orders
                        
                        0. Exit Menu
                        """);
                String option = scanner.nextLine();
                if (!isValidOption(option, "1", "2", "3", "4", "5", "6", "7", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1":
                        viewUsers();
                        break;
                    case "2":
                        viewClients();
                        break;
                    case "3":
                        getClient();
                        break;
                    case "4":
                        updateClient();
                        break;
                    case "5":
                        deleteClient();
                        break;
                    case "6":
                        viewWinner();
                        break;
                    case "7":
                        viewClientsAndOrders();
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

    private void viewClientsAndOrders() {
        System.out.println("Here are all clients and their respective orders:");
        controller.viewClientsAndOrders();
    }

    private void viewWinner() {
        System.out.println("Client with the most points is:");
        controller.getWinner();
    }

    private void deleteClient() {
        System.out.println("Enter the id of the client you would like to delete:");
        Integer id = Integer.parseInt(scanner.nextLine());
        controller.deleteClient(id);
    }

    /// NU UITA CA TREBUIE SA VALIDEZI EMAIL-UL CA SA NU IL INLOCUIESTI CU UNUL DEJA EXISTENT
    private void updateClient() {
        System.out.println("Enter the id of the client you would like to update:");
        Integer id = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the updated name or press enter to avoid changes:");
        String updatedName = scanner.nextLine();
        System.out.println("Enter the new email or press enter to avoid changes:");
        String updatedEmail = scanner.nextLine();
        System.out.println("Enter the new password or press enter to avoid changes:");
        String updatedPassword = scanner.nextLine();
        System.out.println("Enter the new address or press enter to avoid changes:");
        String updatedAddress = scanner.nextLine();
        try {
            controller.updateClient(id, updatedName, updatedEmail, updatedPassword, updatedAddress);
            System.out.println("Client with id " + id + " updated successfully.");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    private void getClient() {
        System.out.println("Enter the id of the client you would like to check:");
        Integer id = Integer.parseInt(scanner.nextLine());
        Client client = controller.getClient(id);
        System.out.println(client.toString());
    }

    private void viewUsers() {
        System.out.println("These are the users registered in the system:");
        for (User user : controller.getUsers()) {
            System.out.println(user.toString());
        }
    }

    private void viewClients() {
        List<Client> clients = new ArrayList<>();
        List<User> users = controller.getUsers();
        for (User user : users) {
            if (user instanceof Client) {
                clients.add((Client) user);
            }
        }
        if (!clients.isEmpty()) {
            System.out.println("These are the clients registered in the system:");
            for (User user : controller.getUsers()) {
                if (user instanceof Client) {
                    System.out.println(user);
                }
            }
        } else {
            System.out.println("There are no clients registered in the system.");
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
                        4. View Products
                        5. Get Product by id
                        0. Exit Menu
                        """);
                String option = scanner.nextLine();
                if (!isValidOption(option, "1", "2", "3", "4", "5", "0")) {
                    throw new ValidationException("Invalid option. Please select a valid option.");
                }
                switch (option) {
                    case "1":
                        createProduct();
                        break;
                    case "2":
                        updateProduct();
                        break;
                    case "3":
                        deleteProduct();
                        break;
                    case "4":
                        viewProducts();
                        break;
                    case "5":
                        getProduct();
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

    private void createProduct() {
        System.out.println("What product would you like to create: Drink or Cake.");
        String type = scanner.nextLine();
        if (type.equals("Drink")) {
            System.out.print("Enter drink name: ");
            String name = scanner.nextLine();
            System.out.print("Enter drink price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter drink weight: ");
            double weight = scanner.nextDouble();
            System.out.print("Enter drink points: ");
            int points = scanner.nextInt();
            System.out.print("Enter drink alcohol percentage: ");
            double alcoholPercentage = scanner.nextDouble();
            // Calculate expiration date (2 weeks from today)
            LocalDate today = LocalDate.now();
            LocalDate expirationDateLocal = today.plusWeeks(2);

            // Map LocalDate components to ExpirationDate
            int year = expirationDateLocal.getYear();
            Month month = Month.values()[expirationDateLocal.getMonthValue() - 1];
            Day day = Day.values()[expirationDateLocal.getDayOfMonth() - 1];

            Date expirationDate = new Date(year, month, day);
            scanner.nextLine();
            try {
                Drink drink = controller.createDrink(name, price, weight, expirationDate, points, alcoholPercentage);
                if (drink != null) {
                    System.out.println("Drink with id " + drink.getID() + " created successfully.");
                } else throw new FailedEntityCreation("Failed to create drink.");
            } catch (FailedEntityCreation e) {
                System.out.println(e.getMessage());
            }


        } else if (type.equals("Cake")) {
            System.out.print("Enter cake name: ");
            String name = scanner.nextLine();
            System.out.print("Enter cake price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter cake weight: ");
            double weight = scanner.nextDouble();
            System.out.print("Enter cake points: ");
            int points = scanner.nextInt();
            System.out.print("Enter cake calories: ");
            int calories = scanner.nextInt();
            // Calculate expiration date (1 week from today)
            LocalDate today = LocalDate.now();
            LocalDate expirationDateLocal = today.plusWeeks(1);

            // Map LocalDate components to ExpirationDate
            int year = expirationDateLocal.getYear();
            Month month = Month.values()[expirationDateLocal.getMonthValue() - 1];
            Day day = Day.values()[expirationDateLocal.getDayOfMonth() - 1];
            Date expirationDate = new Date(year, month, day);
            scanner.nextLine();
            try {
                Cake cake = controller.createCake(name, price, weight, expirationDate, points, calories);
                if (cake != null) {
                    System.out.println("Cake with id " + cake.getID() + " created successfully.");
                } else throw new FailedEntityCreation("Failed to create cake.");
            } catch (FailedEntityCreation e) {
                System.out.println(e.getMessage());
            }


        } else {
            System.out.println("Invalid option. Please choose one of the provided options.");
        }
    }

    /// GRIJA LA UPDATE SA VERIFICI DACA TORTUL ARE NUME UNIC
    private void updateProduct() {
        System.out.println("Enter the id of the product you would like to update:");
        Integer id = Integer.parseInt(scanner.nextLine());
        Product product = controller.getProduct(id);
        if (product instanceof Drink) {
            System.out.print("Enter drink name: ");
            String name = scanner.nextLine();
            System.out.print("Enter drink price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter drink weight: ");
            double weight = scanner.nextDouble();
            System.out.print("Enter drink points: ");
            int points = scanner.nextInt();
            System.out.print("Enter drink alcohol percentage: ");
            double alcoholPercentage = scanner.nextDouble();
            try {
                controller.updateDrink(id, name, price, weight, points, alcoholPercentage);
                System.out.println("Drink with id " + id + " updated successfully.");
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else if (product instanceof Cake) {
            System.out.print("Enter cake name: ");
            String name = scanner.nextLine();
            System.out.print("Enter cake price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter cake weight: ");
            double weight = scanner.nextDouble();
            System.out.print("Enter cake points: ");
            int points = scanner.nextInt();
            System.out.print("Enter cake calories: ");
            int calories = scanner.nextInt();
            try {
                controller.updateCake(id, name, price, weight, points, calories);
                System.out.println("Cake with id " + id + " updated successfully.");
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void deleteProduct() {
        System.out.println("Enter the id of the product you would like to delete:");
        Integer id = Integer.parseInt(scanner.nextLine());
        if (controller.getProduct(id) != null) {
            controller.deleteProduct(id);
            System.out.println("Product with id " + id + " deleted successfully.");
        } else {
            System.out.println("Failed to delete product.");
        }


    }

    private void viewProducts() {
        List<Product> products = controller.getProducts();
        for (Product product : products) {
            System.out.println(product.toString());
        }
    }

    private void getProduct() {
        System.out.println("Enter the id of the product you would like to check:");
        Integer id = Integer.parseInt(scanner.nextLine());
        Product product = controller.getProduct(id);
        if (product != null) {
            System.out.println(product);
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
                        totalBalance();
                        break;
                    case "2":
                        monthlyBalance();
                        break;
                    case "3":
                        yearlyBalance();
                        break;
                    case "0":
                        balanceInformationRunning = false;
                }

            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void yearlyBalance() {
        System.out.println("Enter the year that you would like to check:");
        Integer year = Integer.parseInt(scanner.nextLine());
        System.out.println("Yearly balance details:");
        controller.getYearlyBalance(year);
    }

    private void monthlyBalance() {
        System.out.println("Enter the month that you would like to check:");
        String month = scanner.nextLine();
        Month actualMonth = Month.valueOf(month);
        System.out.println("Monthly balance details:");
        controller.getMonthlyBalance(actualMonth);
    }

    private void totalBalance() {
        System.out.println("Total Balance:");
        controller.getTotalBalance();
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
                else throw new FailedEntityCreation("Account creation failed.");
            } catch (FailedEntityCreation e) {
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
                else throw new FailedEntityCreation("Account creation failed.");
            } catch (FailedEntityCreation e) {
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

}
