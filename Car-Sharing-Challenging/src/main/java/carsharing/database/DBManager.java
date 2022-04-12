package carsharing.database;

import carsharing.util.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class DBManager {

    private static final String CREATE_TABLE_COMPANY = """
            CREATE TABLE IF NOT EXISTS company(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(64) UNIQUE NOT NULL
            );""";
    private static final String CREATE_TABLE_CAR = """
            CREATE TABLE IF NOT EXISTS car(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(64) NOT NULL UNIQUE,
                company_id INT NOT NULL,
                is_rented BOOLEAN NOT NULL DEFAULT FALSE,
                FOREIGN KEY (company_id) REFERENCES company(id)
            );""";
    private static final String CREATE_TABLE_CUSTOMER = """
            CREATE TABLE IF NOT EXISTS customer(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(64) NOT NULL UNIQUE,
                rented_car_id INT,
                FOREIGN KEY (rented_car_id) REFERENCES car(id)
            );""";

    private final DBConnector dbConnector;

    public DBManager(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void makeTransaction(Runnable runnable) {
        try {
            Connection connection = dbConnector.getConnection();
            Savepoint savepoint = connection.setSavepoint();

            tryToRun(runnable, connection, savepoint);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot rollback to savepoint!", e);
        }
    }

    public void migrateUp() {
        try (Statement statement = dbConnector.getConnection().createStatement()) {
            statement.executeUpdate(CREATE_TABLE_COMPANY);
            statement.executeUpdate(CREATE_TABLE_CAR);
            statement.executeUpdate(CREATE_TABLE_CUSTOMER);

            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot create tables", e);
        }
    }

    private void tryToRun(Runnable runnable, Connection connection, Savepoint savepoint) throws SQLException {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            connection.rollback(savepoint);
        }
    }
}
