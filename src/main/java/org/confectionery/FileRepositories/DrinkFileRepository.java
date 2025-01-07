package org.confectionery.FileRepositories;

import org.confectionery.Domain.Date;
import org.confectionery.Domain.Drink;
import org.confectionery.Domain.HasID;
import org.confectionery.Repository.FileRepository;

import java.util.List;

public class DrinkFileRepository extends FileRepository<Drink> {
    /**
     * Constructs a FileRepository with the specified file path.
     *
     * @param filePath the path to the file used for storage
     */
    public DrinkFileRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected String serialize(Drink obj) {
        return obj.getID() + "," +
                obj.getName() + "," +
                obj.getPrice() + "," +
                obj.getAlcoholPercentage() + "," +
                obj.getExpirationDate().toString() + "," +
                obj.getPoints() + "," +
                obj.getAlcoholPercentage();
    }

    @Override
    protected Drink deserialize(String data) {
        try {
            String[] parts = data.split(",");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double price = Double.parseDouble(parts[2]);
            double volume = Double.parseDouble(parts[3]);
            Date expirationDate = Date.parse(parts[4]);
            int points = Integer.parseInt(parts[5]);
            double alcoholPercentage = Double.parseDouble(parts[6]);
            return new Drink(id,name, price, volume, expirationDate, points, alcoholPercentage);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing Drink: " + data, e);
        }
    }

    @Override
    public Integer getMaxID() {
        List<Drink> allDrinks = getAll();

        // Use a stream to find the maximum ID among all cakes
        return allDrinks.stream()
                .mapToInt(HasID::getID) // Extract the ID of each cake
                .max() // Get the maximum value
                .orElse(0); // Return -1 if the file is empty
    }
}
