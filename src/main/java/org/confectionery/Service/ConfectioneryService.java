package org.confectionery.Service;

import org.confectionery.Domain.Cake;
import org.confectionery.Domain.Drink;
import org.confectionery.Domain.Order;
import org.confectionery.Domain.User;
import org.confectionery.Repository.Repository;

public class ConfectioneryService {
    private final Repository<Cake> cakes;
    private final Repository<Drink> drinks;
    private final Repository<Order> orders;
    private final Repository<User> users;

    public ConfectioneryService(Repository<Cake> cakes, Repository<Drink> drinks, Repository<Order> orders, Repository<User> users) {
        this.cakes = cakes;
        this.drinks = drinks;
        this.orders = orders;
        this.users = users;
    }
}
