package org.confectionery;

import org.confectionery.Controller.ConfectioneryController;
import org.confectionery.Domain.*;
import org.confectionery.Exception.ValidationException;
import org.confectionery.Repository.InMemoryRepository;
import org.confectionery.Repository.Repository;
import org.confectionery.Service.ConfectioneryService;
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
                //initializeFileStorage();
                break;
            case "3":
                //initializeDatabaseStorage();
                break;
            case "4":
                System.out.println("Exiting the application. Goodbye!");
                return;
            default:
                throw new ValidationException("Invalid option. Please restart the application.");
        }

        populateRepositories();
        initializeService();
        initializeController();
        initializeUI();
        // Start the UI for user interaction
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

//    private void initializeFileStorage() {
//        System.out.println("Initializing file storage...");
//        cakeRepository = new FileRepository<>("cakes.csv", Cake.class);
//        drinkRepository = new FileRepository<>("drinks.csv", Drink.class);
//        userRepository = new FileRepository<>("users.csv", User.class);
//        orderRepository = new FileRepository<>("orders.csv", Order.class);
//    }
//
//    private void initializeDatabaseStorage() {
//        System.out.println("Initializing database storage...");
//        cakeRepository = new DBRepository<>("cakes", Cake.class);
//        drinkRepository = new DBRepository<>("drinks", Drink.class);
//        userRepository = new DBRepository<>("users", User.class);
//        orderRepository = new DBRepository<>("orders", Order.class);
//    }

    private void populateRepositories() {
        System.out.println("Populating repositories with sample data...");

        ExpirationDate expirationDate1 = new ExpirationDate(2026, Month.February, Day.Eleventh);
        ExpirationDate expirationDate3 = new ExpirationDate(2024, Month.December, Day.Eighteenth);
        ExpirationDate expirationDate4 = new ExpirationDate(2024, Month.December, Day.First);
        ExpirationDate expirationDate2 = new ExpirationDate(2024, Month.November, Day.Thirteenth);
        ExpirationDate expirationDate5 = new ExpirationDate(2024, Month.December, Day.Fourteenth);
        ExpirationDate expirationDate6 = new ExpirationDate(2025, Month.April, Day.Fifteenth);
        ExpirationDate expirationDate7 = new ExpirationDate(2024, Month.May, Day.TwentyFourth);
        ExpirationDate expirationDate8 = new ExpirationDate(2026, Month.July, Day.TwentyFirst);
        ExpirationDate expirationDate9 = new ExpirationDate(2023, Month.March, Day.Thirteenth);
        ExpirationDate expirationDate10 = new ExpirationDate(2027, Month.January, Day.First);

        drinkRepository.create(new Drink(11, "Water", 10, 50, expirationDate6, 30, 0));
        drinkRepository.create(new Drink(12, "Cappuccino", 15, 200, expirationDate7, 45, 0));
        drinkRepository.create(new Drink(13, "Latte", 14, 250, expirationDate8, 68, 0));
        drinkRepository.create(new Drink(14, "Beer", 19, 200, expirationDate9, 10, 8));
        drinkRepository.create(new Drink(15, "Tequila", 18, 300, expirationDate10, 15, 6));
        drinkRepository.create(new Drink(16, "Mocha", 16, 250, expirationDate6, 46, 5));
        drinkRepository.create(new Drink(17, "Green Tea", 21, 150, expirationDate7, 17, 0));
        drinkRepository.create(new Drink(18, "Black Tea", 20, 150, expirationDate5, 12, 0));
        drinkRepository.create(new Drink(19, "Lemonade", 25, 350, expirationDate9, 17, 0));
        drinkRepository.create(new Drink(20, "Hot Chocolate", 20, 200, expirationDate10, 22, 0));

        cakeRepository.create(new Cake(1, "Tiramisu", 100, 50, expirationDate3, 140, 1000));
        cakeRepository.create(new Cake(2, "Eclair", 130, 50, expirationDate4, 120, 1000));
        cakeRepository.create(new Cake(3, "Dubai Chocolate", 200, 100, expirationDate2, 99, 1200));
        cakeRepository.create(new Cake(4, "Carrot Cake", 110, 55, expirationDate3, 68, 900));
        cakeRepository.create(new Cake(5, "Cheesecake", 200, 100, expirationDate1, 100, 500));
        cakeRepository.create(new Cake(6, "Lemon Cake", 130, 65, expirationDate2, 75, 600));
        cakeRepository.create(new Cake(7, "Apple Pie", 90, 45, expirationDate5, 60, 950));
        cakeRepository.create(new Cake(8, "Vanilla Cake", 100, 50, expirationDate2, 108, 1000));
        cakeRepository.create(new Cake(9, "Pineapple Upside Down Cake", 140, 70, expirationDate5, 95, 650));
        cakeRepository.create(new Cake(10, "Strawberry Shortcake", 160, 80, expirationDate4, 90, 750));

        userRepository.create(new Client("Andrei", "Bujoreni", 123));
        userRepository.create(new Client("Maria", "Bujoreni", 312));
        userRepository.create(new Client("Ioana", "Bujoreni", 231));
        userRepository.create(new Admin("admin", "admin@gmail.com", 333, "Bali", "Bujoreni"));
    }

    private void initializeService() {
        System.out.println("Initializing service layer...");
        service = new ConfectioneryService(cakeRepository, drinkRepository, orderRepository, userRepository);
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
