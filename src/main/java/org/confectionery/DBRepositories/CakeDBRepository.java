package org.confectionery.DBRepositories;


import org.confectionery.Domain.Cake;
import org.confectionery.Domain.Date;
import org.confectionery.Repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CakeDBRepository extends DBRepository<Cake> {


    public CakeDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
        createTableIfNotExists();

    }
    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Cakes (
                cakeID INTEGER PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                price DOUBLE NOT NULL,
                weight DOUBLE NOT NULL,
                expirationDate VARCHAR(255) NOT NULL,
                points INT NOT NULL,
                calories INT NOT NULL
            );
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Cake table", e);
        }
    }
    @Override
    public void create(Cake obj) {
        String sql = "INSERT INTO Cakes (cakeID, name, price, weight, expirationDate, points, calories) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getID());
            statement.setString(2, obj.getName());
            statement.setDouble(3, obj.getPrice());
            statement.setDouble(4, obj.getWeight());
            statement.setString(5, obj.getExpirationDate().toString());
            statement.setInt(6, obj.getPoints());
            statement.setInt(7, obj.getCalories());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Cake obj) {
        String sql = "UPDATE Cakes SET name = ?, price = ?, weight = ?, expirationDate = ?, points = ?, calories = ? WHERE cakeID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setDouble(2, obj.getPrice());
            statement.setDouble(3, obj.getWeight());
            statement.setString(4, obj.getExpirationDate().toString());
            statement.setInt(5, obj.getPoints());
            statement.setInt(6, obj.getCalories());
            statement.setInt(7, obj.getID());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Cakes WHERE cakeID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cake> getAll() {
        String sql = "SELECT * FROM Cakes";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Cake> cakes = new ArrayList<>();

            while (resultSet.next()) {
                String date = resultSet.getString("expirationDate");
                Date expirationDate = Date.parse(date);
                Cake cake = new Cake(resultSet.getInt("cakeID"),resultSet.getString("name"), resultSet.getDouble("price"), resultSet.getDouble("weight"), expirationDate, resultSet.getInt("points"), resultSet.getInt("calories"));
                cakes.add(cake);
            }

            return cakes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Cake get(Integer id) {
        String sql = "SELECT * FROM Cakes WHERE cakeID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String date = resultSet.getString("expirationDate");
                Date expirationDate = Date.parse(date);
                return new Cake(resultSet.getInt("cakeID"),resultSet.getString("name"), resultSet.getDouble("price"), resultSet.getDouble("weight"), expirationDate, resultSet.getInt("points"), resultSet.getInt("calories"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}


