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
                        ID INTEGER PRIMARY KEY,
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
                        ID INTEGER PRIMARY KEY ,
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
                statement.setInt(1, user.getID());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setString(5, ((Admin) user).getStatus());
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
                return new Client(clientResultSet.getInt("ID"),clientResultSet.getString("name"), clientResultSet.getString("email"), clientResultSet.getString("password"), clientResultSet.getString("address"));
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
            Admin admin = new Admin(adminResultSet.getInt("ID"),adminResultSet.getString("name"), adminResultSet.getString("email"), adminResultSet.getString("password"));
            admin.setStatus(adminResultSet.getString("status"));
            return admin;
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
                Admin admin = new Admin(resultSet.getInt("ID"),resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"));
                admin.setStatus(resultSet.getString("status"));
                users.add(admin);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String sqlClient = "SELECT * FROM Clients";
        try (PreparedStatement statement = connection.prepareStatement(sqlClient)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Client client = new Client(resultSet.getInt("ID"),resultSet.getString("name"), resultSet.getString("email"),resultSet.getString("password"), resultSet.getString("address"));
                users.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    /**
     * Retrieves the maximum ID from the union of the Admins and Clients tables.
     *
     * @return the maximum ID, or -1 if no IDs are found.
     */
    @Override
    public Integer getMaxID() {
        String sql = """
        SELECT MAX(ID) AS max_id
        FROM (
            SELECT ID FROM Admins
            UNION ALL
            SELECT ID FROM Clients
        ) combined_ids;
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

        // Return -1 if an error occurs or no rows are found
        return 0;
    }


}