package carsharing.database;

import carsharing.util.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector implements AutoCloseable {

    private static final String JDBC_DRIVER = "jdbc:h2:";
    private static final String FILE_PATH = "./src/main/java/carsharing/db/";

    private static final String USER = "root";
    private static final String PASSWORD = "hyperskill";
    private final String url;

    private Connection connection;

    public DBConnector(String fileName) {
        url = JDBC_DRIVER + FILE_PATH + fileName;
        tryToCreateConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseException("Cannot close a connection with database!", e);
            }
        }
    }

    private void tryToCreateConnection() {
        try {
            connection = DriverManager.getConnection(url, USER, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot create a connection with database!", e);
        }
    }
}
