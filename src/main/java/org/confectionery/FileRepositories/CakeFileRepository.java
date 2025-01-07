package org.confectionery.FileRepositories;

import org.confectionery.Domain.HasID;
import org.confectionery.Domain.Cake;
import org.confectionery.Domain.Date;
import org.confectionery.Repository.FileRepository;

import java.util.List;

public class CakeFileRepository extends FileRepository<Cake> {
    /**
     * Constructs a FileRepository with the specified file path.
     *
     * @param filePath the path to the file used for storage
     */
    public CakeFileRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected String serialize(Cake obj) {
        return obj.getID() + "," +
                obj.getName() + "," +
                obj.getPrice() + "," +
                obj.getWeight() + "," +
                obj.getExpirationDate().toString() + "," +
                obj.getPoints() + "," +
                obj.getCalories();
    }

    @Override
    protected Cake deserialize(String data) {
        try {
            String[] parts = data.split(",");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double price = Double.parseDouble(parts[2]);
            double weight = Double.parseDouble(parts[3]);
            Date expirationDate = Date.parse(parts[4]);
            int points = Integer.parseInt(parts[5]);
            int calories = Integer.parseInt(parts[6]);
            return new Cake(id,name, price, weight, expirationDate, points, calories);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing Cake: " + data, e);
        }
    }


    @Override
    public Integer getMaxID() {
        List<Cake> allCakes = getAll();

        // Use a stream to find the maximum ID among all cakes
        return allCakes.stream()
                .mapToInt(HasID::getID) // Extract the ID of each cake
                .max() // Get the maximum value
                .orElse(0); // Return -1 if the file is empty
    }

}
