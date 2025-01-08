package org.confectionery.Test;

import org.confectionery.Domain.*;
import org.confectionery.Repository.InMemoryRepository;
import org.confectionery.Repository.Repository;
import org.confectionery.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ApplicationTest {
    private Repository<Cake> cakeRepository;
    private Repository<Drink> drinkRepository;
    private Repository<User> userRepository;
    private Repository<Order> orderRepository;
    private ConfectioneryService service;
        @BeforeEach
        public void setUp (){
            IDGenerator.getInstance().reset();
            //Initialize in memory repositories
            cakeRepository = new InMemoryRepository<>();
            drinkRepository= new InMemoryRepository<>();
            userRepository= new InMemoryRepository<>();
            orderRepository = new InMemoryRepository<>();
            //Populate InMemoryRepos
            IDGenerator.getInstance().reset();
            Cake cake1 = new Cake("Tiramisu", 100, 50, new Date(2025, Month.December, Day.Eighteenth), 140, 1000);
            Cake cake2 = new Cake("Eclair", 130, 50, new Date(2025, Month.December, Day.First), 120, 1000);
            Cake cake3 = new Cake("Cheesecake", 100, 30, new Date(2025, Month.June, Day.Fifteenth), 150, 500);
            cakeRepository.create(cake1);
            cakeRepository.create(cake2);
            cakeRepository.create(cake3);
            IDGenerator.getInstance().reset();
            Drink drink1 = new Drink("Water", 10, 50, new Date(2025, Month.April, Day.Fifteenth), 30, 0);
            Drink drink2 = new Drink("Cappuccino", 15, 200, new Date(2025, Month.May, Day.TwentyFourth), 45, 0);
            Drink drink3 = new Drink("Lemonade", 10, 100, new Date(2025, Month.August, Day.Tenth), 20, 0);
            drinkRepository.create(drink1);
            drinkRepository.create(drink2);
            drinkRepository.create(drink3);
            IDGenerator.getInstance().reset();
            User user1 = new Client("Andrei", "andrei@gmail.com", "andrei", "Bujoreni");
            User user2 = new Client("Ioana", "ioana@gmail.com", "ioana", "Bujoreni");
            User user3 = new Client("Maria", "maria@gmail.com", "maria", "Bujoreni");
            User user4 = new Admin("Bali", "admin@gmail.com", "admin");
            userRepository.create(user1);
            userRepository.create(user2);
            userRepository.create(user3);
            userRepository.create(user4);

            //Initialize services:
            UserService userService = new UserService(userRepository);
            CakeService cakeService = new CakeService(cakeRepository);
            DrinkService drinkService = new DrinkService(drinkRepository);
            OrderService orderService = new OrderService(orderRepository);
            service = new ConfectioneryService(userService, drinkService, cakeService, orderService);
        }

        @Test
        void testMaxCakeId(){
            Integer id = cakeRepository.getMaxID();
            assert(id == 3);
        }
        @Test
        void testMaxDrinkId(){
            Integer id = drinkRepository.getMaxID();
            assert(id == 3);
        }
        @Test
        void testMaxUserId(){
            Integer id = userRepository.getMaxID();
            assert(id == 4);
        }

        @Test
        void testDeleteCake(){
            cakeRepository.delete(3);
            assertNull(cakeRepository.get(3), "Cake was not deleted!") ;
        }

        @Test void testDeleteDrink(){
            drinkRepository.delete(3);
            assertNull(drinkRepository.get(3), "Drink was not deleted!") ;
        }

        @Test void testDeleteUser(){
            userRepository.delete(3);
            assertNull(userRepository.get(3), "User was not deleted!") ;
        }

        @Test void testUpdateCake(){
            Cake cake = cakeRepository.get(3);
            cake.setName("Lava Cake");
            cakeRepository.update(cake);
            Cake updatedCake = cakeRepository.get(3);
            assert updatedCake.getName().equals("Lava Cake");
        }

        @Test void testUpdateDrink(){
            Drink drink = drinkRepository.get(3);
            drink.setName("Citronella");
            drink.setAlcoholPercentage(4.5);
            drinkRepository.update(drink);
            Drink updatedDrink = drinkRepository.get(3);
            assert updatedDrink.getName().equals("Citronella");
            assert updatedDrink.getAlcoholPercentage() == 4.5;

        }

        @Test void testUpdateUser(){
            User user = userRepository.get(3);
            user.setName("Bob");
            user.setEmail("bob@gmail.com");
            userRepository.update(user);
            User updatedUser = userRepository.get(3);
            assert updatedUser.getName().equals("Bob");
            assert updatedUser.getEmail().equals("bob@gmail.com");
        }

        @Test void checkNoOrders(){
            System.out.println("Client 3 should not have any orders.");
            service.generateInvoice(3);
        }

        @Test void checkOrders(){
            List<Integer> cakeIds = new ArrayList<>();
            List<Integer> drinkIds = new ArrayList<>();
            cakeIds.add(3);
            drinkIds.add(3);
            service.placeOrder(cakeIds,drinkIds,3);
            System.out.println("Client 3 should have an order for a Lemonade and a CheeseCake.");
            service.generateInvoice(3);
        }

        @Test void testEmptyOrderRepo(){
            Integer id = orderRepository.getMaxID();
            assert id == 0;
        }

        @Test void testCreateOrder(){
            List<Integer> cakeIds = new ArrayList<>();
            List<Integer> drinkIds = new ArrayList<>();
            cakeIds.add(3);
            drinkIds.add(3);
            service.placeOrder(cakeIds,drinkIds,3);
            assertNotNull(orderRepository.get(1), "Failed to create order.");
        }

        @Test void testDeleteOrder(){
            List<Integer> cakeIds = new ArrayList<>();
            List<Integer> drinkIds = new ArrayList<>();
            cakeIds.add(3);
            drinkIds.add(3);
            service.placeOrder(cakeIds,drinkIds,3);
            service.deleteOrder(1);
            assertNull(orderRepository.get(1), "Failed to delete order.");
        }

        @Test void testGetAllCakes(){
            List<Cake> cakes = cakeRepository.getAll();
            assert cakes.size() == 3;
        }
        @Test void testGetAllDrinks(){
            List<Drink> drinks = drinkRepository.getAll();
            assert drinks.size() == 3;
        }
        @Test void testGetAllOrders(){
            List<Integer> cakeIds = new ArrayList<>();
            List<Integer> drinkIds = new ArrayList<>();
            cakeIds.add(3);
            drinkIds.add(3);
            service.placeOrder(cakeIds,drinkIds,1);
            service.placeOrder(cakeIds,drinkIds,3);
            service.placeOrder(cakeIds,drinkIds,2);
            List<Order> orders = orderRepository.getAll();
            assert orders.size() == 3;
        }
        @Test void testGetAllUsers(){
            List<User> users = userRepository.getAll();
            assert users.size() == 4;
        }

        @Test void testGetNoAlcoholicDrinks(){
            List<Drink> drinks = service.getAlcoholicDrinks();
            assert drinks.isEmpty();

        }

        @Test void testGetAlcoholicDrinks(){
            Date expirationDate10 = new Date(2027, Month.January, Day.First);
            drinkRepository.create(new Drink( "Tequila", 18, 300, expirationDate10, 15, 6));
            List<Drink> drinks = service.getAlcoholicDrinks();
            assert drinks.size() == 1;
        }

        @Test void testClientWithMostPoints(){
            List<Integer> cakeIds = new ArrayList<>();
            List<Integer> drinkIds = new ArrayList<>();
            cakeIds.add(3);
            drinkIds.add(3);
            service.placeOrder(cakeIds,drinkIds,3);
            service.placeOrder(cakeIds,drinkIds,3);
            System.out.println("Client with most points should be the one with id 3.");
            service.viewClientWithMostPoints();
        }

        @Test void testMonthlyBalance(){
            List<Integer> cakeIds = new ArrayList<>();
            List<Integer> drinkIds = new ArrayList<>();
            cakeIds.add(3);
            drinkIds.add(3);
            service.placeOrder(cakeIds,drinkIds,3);
            service.placeOrder(cakeIds,drinkIds,3);
            System.out.println("Monthly balance for January should be 220 lei.");
            service.getMonthlyBalance(Month.January);
        }
}


