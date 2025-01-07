package org.confectionery.FileRepositories;

import org.confectionery.Domain.*;
import org.confectionery.Repository.FileRepository;
import org.confectionery.Repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class OrderFileRepository extends FileRepository<Order> {
    private final Repository<Cake> cakeRepository;
    private final Repository<Drink> drinkRepository;

    /**
     * Constructs an OrderFileRepository with the specified file path
     * and references to cake and drink repositories.
     *
     * @param filePath  the path to the file used for storage
     * @param cakeRepository  the repository for managing cakes
     * @param drinkRepository the repository for managing drinks
     */
    public OrderFileRepository(String filePath, Repository<Cake> cakeRepository, Repository<Drink> drinkRepository) {
        super(filePath);
        this.cakeRepository = cakeRepository;
        this.drinkRepository = drinkRepository;
    }

    @Override
    protected String serialize(Order order) {
        StringBuilder productsString = new StringBuilder();
        for (Product product : order.getProducts()) {
            String type = product instanceof Cake ? "Cake" : "Drink"; // 'C' for Cake, 'D' for Drink
            productsString.append(type).append("-").append(product.getID()).append(";");
        }
        if (!productsString.isEmpty()) {
            productsString.setLength(productsString.length() - 1); // Remove trailing ";"
        }

        return order.getID() + "," + productsString + "," + order.getDate().toString() + "," + order.getClientID();
    }

    @Override
    protected Order deserialize(String data) {
        try {
            String[] parts = data.split(",");
            int orderID = Integer.parseInt(parts[0]);

            List<Product> products = new ArrayList<>();
            if (!parts[1].isEmpty()) {
                String[] productData = parts[1].split(";");
                for (String productInfo : productData) {
                    String[] typeAndId = productInfo.split("-");
                    String type = typeAndId[0];
                    int id = Integer.parseInt(typeAndId[1]);

                    Product product = null;
                    if (type.equals("Cake")) {
                        product = cakeRepository.get(id);
                    } else if (type.equals("Drink")) {
                        product = drinkRepository.get(id);
                    }
                    if (product != null) {
                        products.add(product);
                    }
                }
            }

            Date date = Date.parse(parts[2]);
            int clientID = Integer.parseInt(parts[3]);

            Order order = new Order(orderID,products, date);
            order.setClientID(clientID);

            return order;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing Order: " + data, e);
        }
    }

    @Override
    public Integer getMaxID() {
        List<Order> allOrders = getAll();

        // Use a stream to find the maximum ID among all cakes
        return allOrders.stream()
                .mapToInt(HasID::getID) // Extract the ID of each cake
                .max() // Get the maximum value
                .orElse(0); // Return -1 if the file is empty
    }
}
