package org.confectionery.DBRepositories;

import org.confectionery.Domain.Drink;
import org.confectionery.Domain.ExpirationDate;
import org.confectionery.Repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDBRepository extends DBRepository<Drink> {

    public DrinkDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
        createTableIfNotExists();

    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Drinks (
                drinkID INT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                price DOUBLE NOT NULL,
                weight DOUBLE NOT NULL,
                expirationDate VARCHAR(255) NOT NULL,
                points INT NOT NULL,
                alcohol INT NOT NULL
            );
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Drinks table", e);
        }
    }

    @Override
    public void create(Drink obj) {
        String sql = "INSERT INTO Drinks (drinkID, name, price, weight, expirationDate, points, alcohol) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getID());
            statement.setString(2, obj.getName());
            statement.setDouble(3, obj.getPrice());
            statement.setDouble(4, obj.getWeight());
            statement.setString(5, obj.getExpirationDate().toString());  // Convert ExpirationDate to String
            statement.setInt(6, obj.getPoints());
            statement.setDouble(7, obj.getAlcoholPercentage());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void update(Drink obj) {
        String sql = "UPDATE Drinks SET name = ?, price = ?, weight = ?, expirationDate = ?, points = ?, alcohol = ? WHERE drinkID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setDouble(2, obj.getPrice());
            statement.setDouble(3, obj.getWeight());
            statement.setString(4, obj.getExpirationDate().toString());  // Convert ExpirationDate to String
            statement.setInt(5, obj.getPoints());
            statement.setDouble(6, obj.getAlcoholPercentage());
            statement.setInt(7, obj.getID());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Drinks WHERE drinkID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Drink> getAll() {
        String sql = "SELECT * FROM Drinks";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Drink> drinks = new ArrayList<>();

            while (resultSet.next()) {
                drinks.add(extractFromResultSet(resultSet));
            }

            return drinks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Drink get(Integer id) {
        String sql = "SELECT * FROM Drinks WHERE drinkID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Drink extractFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("drinkID");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        double weight = resultSet.getDouble("weight");
        String expirationDateStr = resultSet.getString("expirationDate");
        ExpirationDate expirationDate = ExpirationDate.parse(expirationDateStr);
        int points = resultSet.getInt("points");
        double alcoholPercentage = resultSet.getDouble("alcohol");

        Drink drink = new Drink(name, price, weight, expirationDate, points, alcoholPercentage);
        drink.setID(id);
        return drink;
    }
}
