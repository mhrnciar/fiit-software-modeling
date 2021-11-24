package users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    int id;
    String firstName;
    String lastName;
    String username;
    String password;
    String addressLine;
    String OP;
    String ICO;

    public User(int id, String firstName, String lastName, String username, String street,
                String city, String zip, String op_ico_number, boolean op) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.addressLine = street + ", " + zip + " " + city;

        if (op)
            this.OP = op_ico_number;
        else
            this.ICO = op_ico_number;
    }

    public void setPersonal(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        try {
            String url = "jdbc:sqlite:" + "charity.sqlite";
            Connection conn = DriverManager.getConnection(url);

            Statement stm = conn.createStatement();
            stm.executeQuery("UPDATE users SET first_name = " + this.firstName +
                    ", last_name = " + this.lastName +  " WHERE id = " + this.id + ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUsername(String username) {
        this.username = username;

        try {
            String url = "jdbc:sqlite:" + "charity.sqlite";
            Connection conn = DriverManager.getConnection(url);

            Statement stm = conn.createStatement();
            stm.executeQuery("UPDATE users SET username = " + this.username + " WHERE id = " + this.id + ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;

        try {
            String url = "jdbc:sqlite:" + "charity.sqlite";
            Connection conn = DriverManager.getConnection(url);

            Statement stm = conn.createStatement();
            stm.executeQuery("UPDATE users SET password = " + this.password + " WHERE id = " + this.id + ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
