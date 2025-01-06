package org.confectionery.Service;

import org.confectionery.Domain.*;
import org.confectionery.Exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConfectioneryService {
    private final UserService userService;
    private final DrinkService drinkService;
    private final CakeService cakeService;
    private final OrderService orderService;

    public ConfectioneryService(UserService userService, DrinkService drinkService, CakeService cakeService, OrderService orderService) {
        this.userService = userService;
        this.drinkService = drinkService;
        this.cakeService = cakeService;
        this.orderService = orderService;
    }

    public Admin createAdmin(String name, String email, String password) {
        return userService.registerAdmin(name, email, password);
    }

    public Client createClient(String name, String email, String password, String address) {
        return userService.registerClient(name, email, password, address);
    }

    public User loginUser(String email, String password) {
        return userService.login(email, password);
    }

    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    public void statusOn(Integer id) {
        userService.setStatusOn(id);
    }

    public void statusOff(Integer id) {
        userService.setStatusOff(id);
    }

    public void deleteClient(Integer id) {
        userService.deleteUser(id);
    }

    public Client getClient(Integer id) {
        return userService.getClient(id);
    }

    public List<Product> getProducts() {
        // Retrieve all drinks and cakes
        List<Drink> drinks = drinkService.getAllDrinks();
        List<Cake> cakes = cakeService.getAllCakes();

        // Combine drinks and cakes into a single list of products
        List<Product> products = new ArrayList<>();
        products.addAll(drinks);
        products.addAll(cakes);

        // Sort the combined list by the ID of each product
        return products.stream()
                .sorted(Comparator.comparing(Product::getID))
                .collect(Collectors.toList());
    }

    public void deleteProduct(Integer id) {
        try{
            if(drinkService.findDrinkById(id) != null) {
                drinkService.deleteDrink(id);
            }
            else if(cakeService.findCakeById(id) != null) {
                cakeService.deleteCake(id);
            }
            else {
                if(drinkService.findDrinkById(id) == null && cakeService.findCakeById(id) == null) throw new EntityNotFoundException("Entity not found.");
            }
        }catch(EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void updateClient(Integer id, String updatedName, String updatedEmail, String updatedPassword, String updatedAddress) {
        userService.updateClient(id, updatedName, updatedEmail, updatedPassword, updatedAddress);
    }

    public Drink createDrink(String name, double price, double weight, Date expirationDate, int points, double alcoholPercentage) {
        Drink drink = new Drink(name, price, weight, expirationDate, points, alcoholPercentage);
        if (drinkService.getAllDrinks().stream().anyMatch(d -> d.getName().equals(name)))
            return null;
        else
        {   drinkService.addDrink(drink);
            return drink;
        }

    }

    public Cake createCake(String name, double price, double weight, Date expirationDate, int points, int calories) {
        Cake cake = new Cake(name, price, weight, expirationDate, points, calories); // AICI ISI IA ID-UL
        if (cakeService.getAllCakes().stream().anyMatch(c -> c.getName().equals(name))) // INAINTE SA FIE INCREMENTAT DE TOT NR DE CAKE-URI DIN BAZA DE DATE
            return null;
        else
        {
            cakeService.addCake(cake);
            return cake;
        }
    }

    public Product getProduct(Integer id) {
        try{
            if(cakeService.findCakeById(id) == null && drinkService.findDrinkById(id) == null) {
                throw new EntityNotFoundException("Entity not found.");
            }
            else if(cakeService.findCakeById(id) != null){
                return cakeService.findCakeById(id);
            }
            else if(drinkService.findDrinkById(id) != null) return drinkService.findDrinkById(id);
        }catch(EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void updateDrink(Integer id, String name, double price, double weight, int points, double alcoholPercentage) {
        try{
            if(drinkService.findDrinkById(id) != null) {
                Drink drink = drinkService.findDrinkById(id);
                drink.setName(name);
                drink.setPrice(price);
                drink.setWeight(weight);
                drink.setPoints(points);
                drink.setAlcoholPercentage(alcoholPercentage);
                drinkService.updateDrink(drink);
            }
            else throw new EntityNotFoundException("Entity not found.");
        }catch(EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void updateCake(Integer id, String name, double price, double weight, int points, int calories) {
        try{
            if(cakeService.findCakeById(id) != null) {
                Cake cake = cakeService.findCakeById(id);
                cake.setName(name);
                cake.setPrice(price);
                cake.setWeight(weight);
                cake.setPoints(points);
                cake.setCalories(calories);
                cakeService.updateCake(cake);
            }
            else throw new EntityNotFoundException("Entity not found.");
        }catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Drink> getAlcoholicDrinks() {
        List<Drink> drinks = drinkService.getAllDrinks();
        List<Drink> alcoholicDrinks = new ArrayList<>();
        for(Drink drink : drinks){
            if(drink.getAlcoholPercentage() > 0)
                alcoholicDrinks.add(drink);
        }
        return alcoholicDrinks;
    }

    public List<Product> productsSortedByPrice() {
        // Retrieve all drinks and cakes
        List<Drink> drinks = drinkService.getAllDrinks();
        List<Cake> cakes = cakeService.getAllCakes();

        // Combine drinks and cakes into a single list of products
        List<Product> products = new ArrayList<>();
        products.addAll(drinks);
        products.addAll(cakes);

        // Sort the combined list by the ID of each product
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }

    public List<Product> productsSortedByPoints() {
        // Retrieve all drinks and cakes
        List<Drink> drinks = drinkService.getAllDrinks();
        List<Cake> cakes = cakeService.getAllCakes();

        // Combine drinks and cakes into a single list of products
        List<Product> products = new ArrayList<>();
        products.addAll(drinks);
        products.addAll(cakes);

        // Sort the combined list by the ID of each product
        return products.stream()
                .sorted(Comparator.comparing(Product::getPoints).reversed())
                .collect(Collectors.toList());
    }

    public List<Product> productsBeforeDate(Date date) {
        // Retrieve all drinks and cakes
        List<Drink> drinks = drinkService.getAllDrinks();
        List<Cake> cakes = cakeService.getAllCakes();

        // Combine drinks and cakes into a single list of products
        List<Product> products = new ArrayList<>();
        products.addAll(drinks);
        products.addAll(cakes);

        // Filter the combined list for products with expiration dates after the given date
        return products.stream()
                .filter(product -> product.getExpirationDate().compareTo(date) > 0)
                .collect(Collectors.toList());
    }

    public Order placeOrder(List<Integer> productIds, Integer clientId) {
        List<Product> products = new ArrayList<>();
        for (Integer productId : productIds) {
            if(getProduct(productId) != null) {
                products.add(getProduct(productId));
            }
        }
        Date date = Date.convertLocalDateToDate(LocalDate.now());
        Order order = new Order(products, date);
        order.setClientID(clientId);
        orderService.createOrder(order);
        return order;
    }

    /**
     * Prints the invoice for all the orders placed by the client.
     * It shows the products ordered, their prices, and points, as well as the total for each order.
     * If there are multiple orders, it also shows the grand total and total points.
     */
    public void generateInvoice(Integer clientId) {
        List<Order> clientOrders = orderService.findOrdersByClientId(clientId);

        if (clientOrders.isEmpty()) {
            System.out.println("Client ID: " + clientId + " has no orders yet.\n");
            return;
        }

        System.out.println("--------------------------------------------------");
        System.out.println("                 INVOICE                          ");
        System.out.println("--------------------------------------------------");
        System.out.println("Client ID: " + clientId);
        System.out.println("Number of Orders: " + clientOrders.size());
        System.out.println("--------------------------------------------------");

        float grandTotal = 0;
        int grandTotalPoints = 0;

        for (Order order : clientOrders) {
            System.out.println("Order ID: " + order.getID());
            System.out.println("Date: " + order.getDate());
            System.out.println("--------------------------------------------------");
            System.out.printf("%-30s %-10s %-10s%n", "Product Name", "Price (lei)", "Points");

            for (Product product : order.getProducts()) {
                System.out.printf("%-30s %-10.2f %-10d%n", product.getName(), product.getPrice(), product.getPoints());
            }


            System.out.println("--------------------------------------------------");
            System.out.printf("Order Total: %-32.2f Points: %d%n", order.getTotal(), order.getTotalPoints());
            System.out.println("--------------------------------------------------");


            grandTotal += order.getTotal();
            grandTotalPoints += order.getTotalPoints();
        }

        System.out.println("==================================================");
        System.out.printf("GRAND TOTAL: %-32.2f Points: %d%n", grandTotal, grandTotalPoints);
        System.out.println("==================================================\n");
    }

    public void viewClientsAndOrders() {
        List<User> users = userService.getAllUsers();
        List<Client> clients = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Client) {
                clients.add((Client) user);
            }
        }

        if (clients.isEmpty()) {
            System.out.println("No clients available.");
            return;
        }
        for (Client client : clients) {
            System.out.println("Client ID: " + client.getID());
            System.out.println("Name: " + client.getName());
            List<Order> clientOrders = orderService.findOrdersByClientId(client.getID());

            if (clientOrders.isEmpty()) {
                System.out.println("No orders for this client.");
            } else {
                for (Order order : clientOrders) {
                    System.out.println("Order ID: " + order.getID());
                    System.out.println("Date: " + order.getDate() + "\n");
                    System.out.println("Products:");
                    for (Product product : order.getProducts()) {
                        System.out.printf("  - %s (%.2f lei, %d points)%n",
                                product.getName(),
                                product.getPrice(),
                                product.getPoints());
                    }
                    System.out.printf("Order Total: %.2f lei | Points: %d%n",
                            order.getTotal(),
                            order.getTotalPoints());
                }
            }
            System.out.println();
        }
    }


    public void viewClientWithMostPoints() {
        List<User> users = userService.getAllUsers();
        List<Client> clients = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Client) {
                clients.add((Client) user);
            }
        }

        if (clients.isEmpty()) {
            System.out.println("No clients available.");
            return;
        }

        Client clientWithMostPoints = null;
        int maxPoints = 0;

        for (Client client : clients) {
            int totalPoints = orderService.findOrdersByClientId(client.getID())
                    .stream()
                    .mapToInt(Order::getTotalPoints)
                    .sum();
            if (totalPoints > maxPoints) {
                maxPoints = totalPoints;
                clientWithMostPoints = client;
            }
        }
        if (clientWithMostPoints != null) {
            System.out.println("Client ID: " + clientWithMostPoints.getID());
            System.out.println("Name: " + clientWithMostPoints.getName());
            System.out.println("Total Points: " + maxPoints);
        } else {
            System.out.println("No points available for any client.");
        }
    }

}
