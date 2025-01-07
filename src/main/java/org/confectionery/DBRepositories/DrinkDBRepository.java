package org.confectionery.DBRepositories;

import org.confectionery.Domain.Drink;
import org.confectionery.Domain.Date;
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
                drinkID INTEGER PRIMARY KEY,
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
                String date = resultSet.getString("expirationDate");
                Date expirationDate = Date.parse(date);
                Drink drink = new Drink(resultSet.getInt("drinkID"),resultSet.getString("name"), resultSet.getDouble("price"), resultSet.getDouble("weight"), expirationDate, resultSet.getInt("points"), resultSet.getInt("alcohol"));
                drinks.add(drink);
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
                String date = resultSet.getString("expirationDate");
                Date expirationDate = Date.parse(date);
                return new Drink(resultSet.getInt("drinkID"),resultSet.getString("name"), resultSet.getDouble("price"), resultSet.getDouble("weight"), expirationDate, resultSet.getInt("points"), resultSet.getInt("alcohol"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Integer getMaxID() {
        String sql = """
        SELECT MAX(Drinks.drinkID) AS max_id FROM Drinks
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id");
            }
        } catch (SQLException e) {
            // Log the exception and handle it gracefully
            System.err.println("Error executing getMaxID query: " + e.getMessage());
            e.printStackTrace();
        }

        // Return 0 if an error occurs or no rows are found
        return 0;
    }
}
