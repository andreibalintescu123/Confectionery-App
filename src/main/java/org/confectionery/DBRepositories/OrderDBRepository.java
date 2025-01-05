package org.confectionery.DBRepositories;




import org.confectionery.Domain.*;
import org.confectionery.Domain.Date;
import org.confectionery.Repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDBRepository extends DBRepository<Order> {

    CakeDBRepository cakeRepo;
    DrinkDBRepository drinkRepo;

    public OrderDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
        cakeRepo = new CakeDBRepository(dbUrl, dbUser, dbPassword);
        drinkRepo = new DrinkDBRepository(dbUrl, dbUser, dbPassword);
        createTableIfNotExists();
        createOrderedDrinks();
        createOrderedCakes();
    }

    private void createTableIfNotExists() {
        String sql = """
        CREATE TABLE IF NOT EXISTS Orders (
            orderID INT PRIMARY KEY,
            date DATE,
            clientID INTEGER NOT NULL,
            FOREIGN KEY (clientID) REFERENCES Clients(ID) ON DELETE CASCADE
        );
    """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Orders table", e);
        }
    }

    private void createOrderedDrinks() {
        String sql = """
        CREATE TABLE IF NOT EXISTS OrderedDrinks (
            orderID INT,
            drinkID INT,
            PRIMARY KEY (orderID, drinkID),
            FOREIGN KEY (orderID) REFERENCES Orders(orderID) ON DELETE CASCADE,
            FOREIGN KEY (drinkID) REFERENCES Drinks(drinkID) ON DELETE CASCADE
        );
    """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating OrderedDrinks table", e);
        }
    }

    private void createOrderedCakes() {
        String sql = """
        CREATE TABLE IF NOT EXISTS OrderedCakes (
            orderID INT,
            cakeID INT,
            PRIMARY KEY (orderID, cakeID),
            FOREIGN KEY (orderID) REFERENCES Orders(orderID) ON DELETE CASCADE,
            FOREIGN KEY (cakeID) REFERENCES Cakes(cakeID) ON DELETE CASCADE
        );
    """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating OrderedCakes table", e);
        }
    }

    @Override
    public void create(Order order) {
        String sqlOrder = "INSERT INTO Orders (orderID, date, clientID) VALUES (?, ?, ?)";
        String sqlCakes = "INSERT INTO OrderedCakes (orderID, cakeID) VALUES (?, ?)";
        String sqlDrinks = "INSERT INTO OrderedDrinks (orderID, drinkID) VALUES (?, ?)";

        try (PreparedStatement orderStatement = connection.prepareStatement(sqlOrder);
             PreparedStatement cakesStatement = connection.prepareStatement(sqlCakes);
             PreparedStatement drinksStatement = connection.prepareStatement(sqlDrinks)) {

            // Insert into Orders table
            orderStatement.setInt(1, order.getID());
            orderStatement.setString(2, order.getDate().toString());
            orderStatement.setInt(3, order.getClientID());
            orderStatement.executeUpdate();

            // Insert into OrderedCakes and OrderedDrinks tables
            for (Product product : order.getProducts()) {
                if (product instanceof Cake) {
                    cakesStatement.setInt(1, order.getID());
                    cakesStatement.setInt(2, product.getID());
                    cakesStatement.addBatch(); // Use batch for efficiency
                } else if (product instanceof Drink) {
                    drinksStatement.setInt(1, order.getID());
                    drinksStatement.setInt(2, product.getID());
                    drinksStatement.addBatch(); // Use batch for efficiency
                }
            }

            // Execute batch inserts
            cakesStatement.executeBatch();
            drinksStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert order and products into the database", e);
        }
    }

    @Override
    public void update(Order order) {
        //Can't update orders, no implementation.
    }

    @Override
    public void delete(Integer id) {
        // Delete from OrderedDrinks
        String deleteOrderedDrinksSql = "DELETE FROM OrderedDrinks WHERE orderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderedDrinksSql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting ordered drinks", e);
        }

        // Delete from OrderedCakes
        String deleteOrderedCakesSql = "DELETE FROM OrderedCakes WHERE orderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderedCakesSql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting ordered cakes", e);
        }

        // Finally, delete from Orders
        String deleteOrderSql = "DELETE FROM Orders WHERE orderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderSql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }
// Trebuie legat la client
    @Override
    public List<Order> getAll() {
        String sql = "SELECT * FROM Orders";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                String expirationDateStr = resultSet.getString("date");
                Date date = Date.parse(expirationDateStr);
                List<Product> products = getProductsForOrder(orderID);
                int clientID = resultSet.getInt("clientID");
                Order order = new Order(products, date);
                order.setID(orderID);
                order.setClientID(clientID);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> getProductsForOrder(int orderID) {
        List<Product> products = new ArrayList<>();

        // Query OrderedCakes
        String sqlCakes = "SELECT c.* FROM OrderedCakes oc INNER JOIN Cakes c ON oc.cakeID = c.cakeID WHERE oc.orderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlCakes)) {
            statement.setInt(1, orderID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(cakeRepo.get(resultSet.getInt("cakeID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Query OrderedDrinks
        String sqlDrinks = "SELECT d.* FROM OrderedDrinks od INNER JOIN Drinks d ON od.drinkID = d.drinkID WHERE od.orderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlDrinks)) {
            statement.setInt(1, orderID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(drinkRepo.get(resultSet.getInt("drinkID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

// Trebuie legat la client
    @Override
    public Order get(Integer id) {
        String sql = "SELECT * FROM Orders WHERE orderID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Integer orderID = resultSet.getInt("orderID");
                String expirationDateStr = resultSet.getString("date");
                Date date = Date.parse(expirationDateStr);
                List<Product> products = getProductsForOrder(id);
                int clientID = resultSet.getInt("clientID");
                Order order = new Order(products, date);
                order.setID(orderID);
                order.setClientID(clientID);
                return order;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
