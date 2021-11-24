import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Connection conn = connect();

        Dashboard d = new Dashboard(conn);
    }

    private static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + "charity.sqlite";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection successful");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}