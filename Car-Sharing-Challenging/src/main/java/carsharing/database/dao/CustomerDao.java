package carsharing.database.dao;

import carsharing.database.DBConnector;
import carsharing.model.Customer;
import carsharing.util.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    private static final String GET_ALL = "SELECT * FROM customer";
    private static final String SAVE_CUSTOMER = "INSERT INTO customer(name) VALUES(?);";
    private static final String UPDATE_BY_ID = "UPDATE customer SET rented_car_id = ? WHERE id = ?;";

    private final DBConnector dbConnector;

    public CustomerDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(GET_ALL)) {
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int rentedCarId = resultSet.getInt("rented_car_id");

                customers.add(new Customer(id, name, rentedCarId));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Cannot get customers from database", e);
        }

        return customers;
    }

    public void save(String name) {
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(SAVE_CUSTOMER)) {
            stmt.setString(1, name);
            stmt.executeUpdate();

            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot save customer " + name, e);
        }
    }

    public void updateCustomer(int id, int rentedCarId) {
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(UPDATE_BY_ID)) {
            if (rentedCarId == 0) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, rentedCarId);
            }
            stmt.setInt(2, id);
            stmt.executeUpdate();

            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot update customer with id" + id, e);
        }
    }
}
