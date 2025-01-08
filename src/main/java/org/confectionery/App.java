package org.confectionery;

import org.confectionery.Controller.ConfectioneryController;
import org.confectionery.DBRepositories.CakeDBRepository;
import org.confectionery.DBRepositories.DrinkDBRepository;
import org.confectionery.DBRepositories.OrderDBRepository;
import org.confectionery.DBRepositories.UserDBRepository;
import org.confectionery.Domain.*;
import org.confectionery.Exception.ValidationException;
import org.confectionery.FileRepositories.CakeFileRepository;
import org.confectionery.FileRepositories.DrinkFileRepository;
import org.confectionery.FileRepositories.OrderFileRepository;
import org.confectionery.FileRepositories.UserFileRepository;
import org.confectionery.Repository.InMemoryRepository;
import org.confectionery.Repository.Repository;
import org.confectionery.Service.*;
import org.confectionery.UI.ConfectioneryUI;

import java.util.Scanner;

public class App {

    private Repository<Cake> cakeRepository;
    private Repository<Drink> drinkRepository;
    private Repository<User> userRepository;
    private Repository<Order> orderRepository;
    private ConfectioneryService service;
    private ConfectioneryController controller;
    private ConfectioneryUI ui;
    private final Scanner scanner;

    public App() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        String storageType = selectStorageType();

        switch (storageType) {
            case "1":
                initializeInMemoryStorage();
                break;
            case "2":
                initializeFileStorage();
                break;
            case "3":
                initializeDatabaseStorage();
                break;
            case "4":
                System.out.println("Exiting the application. Goodbye!");
                return;
            default:
                throw new ValidationException("Invalid option. Please choose one of the provided options.");
        }
        IDGenerator.getInstance().reset();
        System.out.println("Would you like to populate the repositories?");
        System.out.println("Yes or No?");
        String option = scanner.nextLine();
        if(option.equals("Yes") || option.equals("yes")) {
            populateRepositories();
        }
        initializeService();
        initializeController();
        initializeUI();
        ui.start();
    }

    private String selectStorageType() {
        String type;
        while (true) {
            System.out.print("Welcome to the confectionery app!\n");
            System.out.print("Please enter the type of storage you would like the app to run on:\n");
            System.out.println("1. In memory storage");
            System.out.println("2. File storage");
            System.out.println("3. Database storage");
            System.out.println("4. Exit");
            type = scanner.nextLine();
            if (type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) {
                break;
            } else {
                System.out.println("Invalid option. Please select a valid option.");
            }
        }
        return type;
    }

    private void initializeInMemoryStorage() {
        System.out.println("Initializing in-memory storage...");
        cakeRepository = new InMemoryRepository<>();
        drinkRepository = new InMemoryRepository<>();
        userRepository = new InMemoryRepository<>();
        orderRepository = new InMemoryRepository<>();
    }

    private void initializeFileStorage() {
        System.out.println("Initializing file storage...");

        cakeRepository = new CakeFileRepository("src/main/java/org/confectionery/Files/cakes.csv");
        drinkRepository = new DrinkFileRepository("src/main/java/org/confectionery/Files/drinks.csv");
        userRepository = new UserFileRepository("src/main/java/org/confectionery/Files/users.csv");
        orderRepository = new OrderFileRepository("src/main/java/org/confectionery/Files/orders.csv", cakeRepository, drinkRepository);

        System.out.println("File storage initialized successfully.");
    }


    private void initializeDatabaseStorage() {
        String DB_URL = "jdbc:sqlite:src/main/java/org/confectionery/confectionery.db";
        String DB_USER = "user";
        String DB_PASSWORD = "password";
        System.out.println("Initializing database storage...");
        cakeRepository = new CakeDBRepository(DB_URL,DB_USER,DB_PASSWORD);
        drinkRepository = new DrinkDBRepository(DB_URL,DB_USER,DB_PASSWORD );
        userRepository = new UserDBRepository(DB_URL,DB_USER,DB_PASSWORD );
        orderRepository = new OrderDBRepository(DB_URL,DB_USER,DB_PASSWORD );
    }

    private void populateRepositories() {
        System.out.println("Populating repositories with sample data...");

        Date expirationDate1 = new Date(2025, Month.January, Day.Eleventh);
        Date expirationDate3 = new Date(2025, Month.January, Day.Eighteenth);
        Date expirationDate4 = new Date(2025, Month.January, Day.First);
        Date expirationDate2 = new Date(2025, Month.January, Day.Thirteenth);
        Date expirationDate5 = new Date(2025, Month.January, Day.Fourteenth);
        Date expirationDate6 = new Date(2025, Month.January, Day.Fourth);
        Date expirationDate7 = new Date(2025, Month.January, Day.TwentyFourth);
        Date expirationDate8 = new Date(2025, Month.January, Day.Second);
        Date expirationDate9 = new Date(2025, Month.January, Day.Thirteenth);
        Date expirationDate10 = new Date(2025, Month.January, Day.First);

        IDGenerator.getInstance().reset();
        drinkRepository.create(new Drink("Water", 10, 50, expirationDate6, 30, 0));
        drinkRepository.create(new Drink("Cappuccino", 15, 200, expirationDate7, 45, 0));
        drinkRepository.create(new Drink("Latte", 14, 250, expirationDate8, 68, 0));
        drinkRepository.create(new Drink( "Beer", 19, 200, expirationDate9, 10, 8));
        drinkRepository.create(new Drink( "Tequila", 18, 300, expirationDate10, 15, 6));
        drinkRepository.create(new Drink( "Mocha", 16, 250, expirationDate6, 46, 5));
        drinkRepository.create(new Drink( "Green Tea", 21, 150, expirationDate7, 17, 0));
        drinkRepository.create(new Drink( "Black Tea", 20, 150, expirationDate5, 12, 0));
        drinkRepository.create(new Drink( "Lemonade", 25, 350, expirationDate9, 17, 0));
        drinkRepository.create(new Drink( "Hot Chocolate", 20, 200, expirationDate10, 22, 0));

        IDGenerator.getInstance().reset();
        cakeRepository.create(new Cake("Tiramisu", 100, 50, expirationDate3, 140, 1000));
        cakeRepository.create(new Cake("Eclair", 130, 50, expirationDate4, 120, 1000));
        cakeRepository.create(new Cake( "Dubai Chocolate", 200, 100, expirationDate2, 99, 1200));
        cakeRepository.create(new Cake("Carrot Cake", 110, 55, expirationDate3, 68, 900));
        cakeRepository.create(new Cake( "Cheesecake", 200, 100, expirationDate1, 100, 500));
        cakeRepository.create(new Cake("Lemon Cake", 130, 65, expirationDate2, 75, 600));
        cakeRepository.create(new Cake("Apple Pie", 90, 45, expirationDate5, 60, 950));
        cakeRepository.create(new Cake("Vanilla Cake", 100, 50, expirationDate2, 108, 1000));
        cakeRepository.create(new Cake("Pineapple Upside Down Cake", 140, 70, expirationDate5, 95, 650));
        cakeRepository.create(new Cake("Strawberry Shortcake", 160, 80, expirationDate4, 90, 750));

        IDGenerator.getInstance().reset();
        userRepository.create(new Client("Andrei", "andrei@gmail.com", "andrei", "Bujoreni"));
        userRepository.create(new Client("Ioana", "ioana@gmail.com", "ioana", "Bujoreni"));
        userRepository.create(new Client("Maria", "maria@gmail.com", "maria", "Bujoreni"));
        userRepository.create(new Admin("Bali", "admin@gmail.com", "admin"));
    }

    private void initializeService() {
        System.out.println("Initializing service layer...");
        UserService userService = new UserService(userRepository);
        CakeService cakeService = new CakeService(cakeRepository);
        DrinkService drinkService = new DrinkService(drinkRepository);
        OrderService orderService = new OrderService(orderRepository);
        service = new ConfectioneryService(userService, drinkService, cakeService, orderService);
    }

    private void initializeController() {
        System.out.println("Initializing controller...");
        controller = new ConfectioneryController(service);
    }

    private void initializeUI() {
        System.out.println("Initializing UI...");
        ui = new ConfectioneryUI(controller, scanner);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}
