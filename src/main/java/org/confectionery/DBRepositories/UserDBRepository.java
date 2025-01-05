package org.confectionery.DBRepositories;


import org.confectionery.Domain.Admin;
import org.confectionery.Domain.Client;
import org.confectionery.Domain.User;
import org.confectionery.Repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository extends DBRepository<User> {

    public UserDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
        Client();
        Admin();
    }

    private void Client() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS Clients (
                        ID INT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        address VARCHAR(255) NOT NULL
                    );
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Clients table", e);
        }
    }

    private void Admin() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS Admins (
                        ID INT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        status VARCHAR(255) NOT NULL
                    );
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Admins table", e);
        }
    }


    @Override
    public void create(User user) {
        String sql;
        if (user instanceof Admin) {
            sql = "INSERT INTO Admins (ID, name, email, password, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Admin admin = (Admin) user;
                statement.setInt(1, admin.getID());
                statement.setString(2, admin.getName());
                statement.setString(3, admin.getEmail());
                statement.setString(4, admin.getPassword());
                statement.setString(5, admin.getStatus());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (user instanceof Client) {
            sql = "INSERT INTO Clients (ID, name, email, password, address) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Client client = (Client) user;
                statement.setInt(1, client.getID());
                statement.setString(2, client.getName());
                statement.setString(3, client.getEmail());
                statement.setString(4, client.getPassword());
                statement.setString(5, client.getAddress());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void update(User user) {
        String sql;
        if (user instanceof Admin) {
            sql = "UPDATE Admins SET name = ?, email = ?, password = ?, status= ? WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Admin admin = (Admin) user;
                statement.setString(1, admin.getName());
                statement.setString(2, admin.getEmail());
                statement.setString(3, admin.getPassword());
                statement.setString(4, admin.getStatus());
                statement.setInt(5, admin.getID());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (user instanceof Client) {
            sql = "UPDATE Clients SET name = ?, email = ?, password = ?, address = ? WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Client client = (Client) user;
                statement.setString(1, client.getName());
                statement.setString(2, client.getEmail());
                statement.setString(3, client.getPassword());
                statement.setString(4, client.getAddress());
                statement.setInt(5, client.getID());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Integer id) {
        // Try deleting from Clients table
        String sqlClient = "DELETE FROM Clients WHERE ID=?";
        try (PreparedStatement clientStmt = connection.prepareStatement(sqlClient)) {
            clientStmt.setInt(1, id);
            int rowsAffected = clientStmt.executeUpdate();

            // If the user was deleted from Clients, return
            if (rowsAffected > 0) {
                System.out.println("Deleted Client: " + id);
                return;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting from Clients table", ex);
        }

        // Try deleting from Admins table
        String sqlAdmin = "DELETE FROM Admins WHERE ID=?";
        try (PreparedStatement adminStmt = connection.prepareStatement(sqlAdmin)) {
            adminStmt.setInt(1, id);
            int rowsAffected = adminStmt.executeUpdate();

            // If the user was deleted from Admins, return
            if (rowsAffected > 0) {
                System.out.println("Deleted Admin: " + id);
                return;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting from Admins table", ex);
        }

        // If no rows were affected, the user does not exist
        throw new RuntimeException("User with ID " + id + " not found in either Clients or Admins tables");
    }



    @Override
    public User get(Integer id) {
        // Query the Clients table first
        String sqlClient = "SELECT * FROM Clients WHERE ID=?";
        try (PreparedStatement clientStmt = connection.prepareStatement(sqlClient)) {
            clientStmt.setInt(1, id);
            ResultSet clientResultSet = clientStmt.executeQuery();

            if (clientResultSet.next()) {
                return extractClientFromResultSet(clientResultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error querying Clients table", ex);
        }

        // Query the Admins table if not found in Clients
        String sqlAdmin = "SELECT * FROM Admins WHERE ID=?";
        try (PreparedStatement adminStmt = connection.prepareStatement(sqlAdmin)) {
            adminStmt.setInt(1, id);
            ResultSet adminResultSet = adminStmt.executeQuery();

            if (adminResultSet.next()) {
                return extractAdminFromResultSet(adminResultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error querying Admins table", ex);
        }

        // If the user is not found in either table
        return null;
    }


    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();


        String sqlAdmin = "SELECT * FROM Admins";
        try (PreparedStatement statement = connection.prepareStatement(sqlAdmin)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(extractAdminFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String sqlClient = "SELECT * FROM Clients";
        try (PreparedStatement statement = connection.prepareStatement(sqlClient)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(extractClientFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    private Admin extractAdminFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        Admin admin = new Admin(name, email, password);
        admin.setID(id);
        return admin;
    }

    private Client extractClientFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String address = resultSet.getString("address");

        Client client = new Client(name, email, password, address);
        client.setID(id);
        return client;
    }
}
