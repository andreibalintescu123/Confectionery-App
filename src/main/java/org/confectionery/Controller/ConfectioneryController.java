package org.confectionery.Controller;

import org.confectionery.Domain.*;
import org.confectionery.Service.ConfectioneryService;

import java.util.List;

public class ConfectioneryController {
    private final ConfectioneryService confectioneryService;

    public ConfectioneryController(ConfectioneryService confectioneryService) {
        this.confectioneryService = confectioneryService;
    }

    public Admin createAdmin(String name, String email, String password) {
        return confectioneryService.createAdmin(name, email, password);
    }

    public Client createClient(String name, String email, String password, String address) {
        return confectioneryService.createClient(name, email, password, address);
    }

    public User loginUser(String email, String password) {
        return confectioneryService.loginUser(email, password);
    }

    public List<User> getUsers() {
        return confectioneryService.getUsers();
    }

    public void updateStatusOn(Integer id) {
        confectioneryService.statusOn(id);
    }

    public void updateStatusOff(Integer id) {
        confectioneryService.statusOff(id);
    }

    public void deleteClient(Integer id) {
        confectioneryService.deleteClient(id);
    }

    public Client getClient(Integer id) {
       return confectioneryService.getClient(id);
    }

    public List<Product> getProducts() {
        return confectioneryService.getProducts();
    }

    public void deleteProduct(Integer id) {
        confectioneryService.deleteProduct(id);
    }

    public void updateClient(Integer id, String updatedName, String updatedEmail, String updatedPassword, String updatedAddress) {
        confectioneryService.updateClient(id, updatedName, updatedEmail, updatedPassword,updatedAddress);
    }

    public Drink createDrink(String name, double price, double weight, Date expirationDate, int points, double alcoholPercentage) {
        return confectioneryService.createDrink(name,price,weight,expirationDate,points,alcoholPercentage);
    }

    public Cake createCake(String name, double price, double weight, Date expirationDate, int points, int calories) {
        return confectioneryService.createCake(name,price,weight,expirationDate,points,calories);
    }

    public Product getProduct(Integer id) {
        return confectioneryService.getProduct(id);
    }

    public void updateDrink(Integer id, String name, double price, double weight, int points, double alcoholPercentage) {
        confectioneryService.updateDrink(id,name,price,weight,points,alcoholPercentage);
    }

    public void updateCake(Integer id, String name, double price, double weight, int points, int calories) {
        confectioneryService.updateCake(id,name,price,weight,points,calories);
    }

    public List<Drink> viewAlcoholicDrinks() {
        return confectioneryService.getAlcoholicDrinks();
    }

    public List<Product> menuSortedByPrice() {
        return confectioneryService.productsSortedByPrice();
    }

    public List<Product> menuSortedByPoints() {
        return confectioneryService.productsSortedByPoints();
    }

    public List<Product> getAvailableProducts(Date expirationDate) {
        return confectioneryService.productsAfterDate(expirationDate);
    }

    public Order placeOrder(List<Integer> cakeIds, List<Integer> drinkIds, Integer clientId) {
       return confectioneryService.placeOrder(cakeIds, drinkIds,  clientId);

    }

    public void generateInvoice(Integer clientId) {
        confectioneryService.generateInvoice(clientId);
    }

    public void viewClientsAndOrders() {
        confectioneryService.viewClientsAndOrders();
    }

    public void getWinner() {
        confectioneryService.viewClientWithMostPoints();
    }

    public void getYearlyBalance(Integer year) {
        confectioneryService.getYearlyBalance(year);
    }

    public void getMonthlyBalance(Month month) {
        confectioneryService.getMonthlyBalance(month);
    }

    public void getTotalBalance() {
        confectioneryService.getTotalBalance();
    }

    public Order getOrder(Integer id) {
        return confectioneryService.getOrder(id);
    }

    public void deleteOrder(Integer id) {
        confectioneryService.deleteOrder(id);
    }
}
