package org.confectionery.FileRepositories;

import org.confectionery.Domain.Date;
import org.confectionery.Domain.Drink;
import org.confectionery.Repository.FileRepository;

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
            Drink drink = new Drink(name, price, volume, expirationDate, points, alcoholPercentage);
            drink.setID(id);
            return drink;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing Drink: " + data, e);
        }
    }
}
