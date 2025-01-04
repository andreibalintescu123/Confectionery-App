package org.confectionery.Service;

import org.confectionery.Domain.*;
import org.confectionery.Exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConfectioneryService {
    private final UserService userService;
    private final DrinkService drinkService;
    private final CakeService cakeService;

    public ConfectioneryService(UserService userService, DrinkService drinkService, CakeService cakeService) {
        this.userService = userService;
        this.drinkService = drinkService;
        this.cakeService = cakeService;
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

    public Drink createDrink(String name, double price, double weight, ExpirationDate expirationDate, int points, double alcoholPercentage) {
        Drink drink = new Drink(name, price, weight, expirationDate, points, alcoholPercentage);
        if (drinkService.getAllDrinks().stream().anyMatch(d -> d.getName().equals(name)))
            return null;
        else
        {   drinkService.addDrink(drink);
            return drink;
        }

    }

    public Cake createCake(String name, double price, double weight, ExpirationDate expirationDate, int points, int calories) {
        Cake cake = new Cake(name, price, weight, expirationDate, points, calories);
        if (cakeService.getAllCakes().stream().anyMatch(c -> c.getName().equals(name)))
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
}
