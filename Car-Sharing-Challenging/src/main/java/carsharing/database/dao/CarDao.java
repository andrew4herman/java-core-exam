package carsharing.database.dao;

import carsharing.database.DBConnector;
import carsharing.model.Car;
import carsharing.util.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDao {

    private static final String GET_BY_ID =
            "SELECT (ID, NAME, COMPANY_ID, IS_RENTED) FROM car WHERE id = ?;";
    private static final String GET_ALL_FROM_COMPANY =
            "SELECT (ID, NAME, COMPANY_ID, IS_RENTED) FROM car WHERE company_id = ?;";
    private static final String GET_RENTED_FROM_COMPANY =
            "SELECT (ID, NAME, COMPANY_ID, IS_RENTED) FROM car WHERE is_rented = false AND company_id = ?;";
    private static final String SAVE_CAR = "INSERT INTO car(name, company_id) VALUES(?, ?);";
    private static final String UPDATE_BY_ID = "UPDATE car SET is_rented = ? WHERE ID = ?;";

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_COMPANY_ID = "COMPANY_ID";
    private static final String COLUMN_RENTED = "IS_RENTED";

    private final DBConnector dbConnector;

    public CarDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public Optional<Car> getById(int id) {
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(GET_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Car(
                        resultSet.getInt(COLUMN_ID),
                        resultSet.getString(COLUMN_NAME),
                        resultSet.getInt(COLUMN_COMPANY_ID),
                        resultSet.getBoolean(COLUMN_RENTED)));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Cannot get from database a car with id " + id, e);
        }
        return Optional.empty();
    }

    public List<Car> getAllCarsFrom(int companyId) {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(GET_ALL_FROM_COMPANY)) {
            stmt.setInt(1, companyId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                cars.add(new Car(
                        resultSet.getInt(COLUMN_ID),
                        resultSet.getString(COLUMN_NAME),
                        resultSet.getInt(COLUMN_COMPANY_ID),
                        resultSet.getBoolean(COLUMN_RENTED)));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Cannot get cars from database", e);
        }

        return cars;
    }

    public List<Car> getRentedCarsFrom(int companyId) {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(GET_RENTED_FROM_COMPANY)) {
            stmt.setInt(1, companyId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                cars.add(new Car(
                        resultSet.getInt(COLUMN_ID),
                        resultSet.getString(COLUMN_NAME),
                        resultSet.getInt(COLUMN_COMPANY_ID),
                        resultSet.getBoolean(COLUMN_RENTED)));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Cannot get all rented/unrented cars from database", e);
        }

        return cars;
    }

    public void save(String name, int companyId) {
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(SAVE_CAR)) {
            stmt.setString(1, name);
            stmt.setInt(2, companyId);
            stmt.executeUpdate();

            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot save car %s from company %d".formatted(name, companyId), e);
        }
    }

    public void updateCar(int id, boolean rented) {
        try (PreparedStatement stmt =
                     dbConnector.getConnection().prepareStatement(UPDATE_BY_ID)) {
            stmt.setBoolean(1, rented);
            stmt.setInt(2, id);
            stmt.executeUpdate();

            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot update car with id " + id, e);
        }
    }
}
