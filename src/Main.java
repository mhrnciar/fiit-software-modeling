import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Connection conn = connect();

        Dashboard d = new Dashboard(conn);
    }

    /**
     * Establish connection to SQLite database which will be used to access data about auctions,
     * users, charities, ...
     * @return connection to SQLite database
     */
    private static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:charity.sqlite";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection successful");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
