package users;

import auction.Datetime;
import main.Dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.Date;

public class User {
    JFrame frame;
    UserCanvas canvas;
    Connection connection;

    int id;
    String firstName;
    String lastName;
    String username;
    String password;
    String addressLine;
    String OP;
    String ICO;

    /**
     * Constructor for existing user
     * @param conn          connection to database
     * @param id            user's id in database
     * @param firstName     first name
     * @param lastName      last name
     * @param username      username
     * @param street        street from address line
     * @param city          city from address line
     * @param state         state from address line
     * @param zip           zip from address line
     * @param op_ico_number OP or ICO number, depending on op variable
     * @param op            if true, op_ico_number is for OP field, otherwise for ICO field
     */
    public User(Connection conn, int id, String firstName, String lastName, String username,
                String street, String city, String state, String zip, String op_ico_number, boolean op) {
        this.connection = conn;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.addressLine = street + ", " + zip + " " + city + " " + state;

        if (op)
            this.OP = op_ico_number;
        else
            this.ICO = op_ico_number;
    }

    /**
     * Constructor for new user
     * @param conn  connection to database
     */
    public User(Connection conn) {
        this.connection = conn;
        frame = new JFrame("Create new user");
        canvas = new UserCanvas();
        createUser();
    }

    /**
     * createUser generates new frame with fields for first and last name, username, password, address separated
     * into street, city, state and zip, OP or ICO number and option to select whether the account is personal
     * (OP) or company (ICO), and option to register as Bidder or Organizer. Then, the information is evaluated -
     * check whether all fields have been filled in, password and confirm password fields match, username is not
     * in use by other user and if everything is in order, a new user is created in database and logged in.
     */
    public void createUser() {
        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(440, 720));

        JLabel title = new JLabel(prepareHTML("white", "5", "Create new user"),  SwingConstants.CENTER);
        title.setBounds(5, 5, 430, 50);
        canvas.add(title);

        JLabel firstNameLabel = new JLabel("First name:");
        firstNameLabel.setBounds(20, 80, 100, 30);
        canvas.add(firstNameLabel);

        JTextField firstNameField = new JTextField();
        firstNameField.setBounds(120, 80, 250, 30);
        canvas.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last name:");
        lastNameLabel.setBounds(20, 130, 100, 30);
        canvas.add(lastNameLabel);

        JTextField lastNameField = new JTextField();
        lastNameField.setBounds(120, 130, 250, 30);
        canvas.add(lastNameField);

        JLabel userNameLabel = new JLabel("Username:");
        userNameLabel.setBounds(20, 180, 100, 30);
        canvas.add(userNameLabel);

        JTextField userNameField = new JTextField();
        userNameField.setBounds(120, 180, 250, 30);
        canvas.add(userNameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 230, 100, 30);
        canvas.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 230, 250, 30);
        canvas.add(passwordField);

        JLabel passwordAgainLabel = new JLabel("Confirm Password:");
        passwordAgainLabel.setBounds(20, 280, 100, 30);
        canvas.add(passwordAgainLabel);

        JPasswordField passwordAgainField = new JPasswordField();
        passwordAgainField.setBounds(120, 280, 250, 30);
        canvas.add(passwordAgainField);

        /*
         * There are multiple address fields so every one of them has written in what's supposed to go there,
         * for example, field for street has written in Street in gray color, which is erased when user selects
         * the field. If he leaves it empty and selects another field, the Street in gray color appears in the
         * field again.
         */
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 330, 100, 30);
        canvas.add(addressLabel);

        JTextField streetField = new JTextField();
        streetField.setBounds(120, 330, 250, 30);
        streetField.setText("Street");
        streetField.setForeground(Color.gray);
        streetField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (streetField.getText().equals("Street")) {
                    streetField.setText("");
                    streetField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (streetField.getText().isEmpty()) {
                    streetField.setForeground(Color.gray);
                    streetField.setText("Street");
                }
            }
        });
        canvas.add(streetField);

        JTextField cityField = new JTextField();
        cityField.setBounds(120, 370, 250, 30);
        cityField.setText("City");
        cityField.setForeground(Color.gray);
        cityField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cityField.getText().equals("City")) {
                    cityField.setText("");
                    cityField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cityField.getText().isEmpty()) {
                    cityField.setForeground(Color.gray);
                    cityField.setText("City");
                }
            }
        });
        canvas.add(cityField);

        JTextField stateField = new JTextField();
        stateField.setBounds(120, 410, 150, 30);
        stateField.setText("State");
        stateField.setForeground(Color.gray);
        stateField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (stateField.getText().equals("State")) {
                    stateField.setText("");
                    stateField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (stateField.getText().isEmpty()) {
                    stateField.setForeground(Color.gray);
                    stateField.setText("State");
                }
            }
        });
        canvas.add(stateField);

        JTextField zipField = new JTextField();
        zipField.setBounds(280, 410, 100, 30);
        zipField.setText("ZIP");
        zipField.setForeground(Color.gray);
        zipField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (zipField.getText().equals("ZIP")) {
                    zipField.setText("");
                    zipField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (zipField.getText().isEmpty()) {
                    zipField.setForeground(Color.gray);
                    zipField.setText("ZIP");
                }
            }
        });
        canvas.add(zipField);

        /*
         * Toggle buttons to select whether new account is personal or company, and if it's registered as Bidder
         * or Organizer.
         */
        JLabel opLabel = new JLabel("OP or ICO:");
        opLabel.setBounds(20, 460, 100, 30);
        canvas.add(opLabel);

        JTextField opField = new JTextField();
        opField.setBounds(120, 460, 200, 30);
        canvas.add(opField);

        JToggleButton opToggle = new JToggleButton();
        opToggle.setBounds(170, 510, 100, 30);
        opToggle.setText("ICO");
        opToggle.addItemListener(e -> {
            if (opToggle.isSelected())
                opToggle.setText("OP");
            else
                opToggle.setText("ICO");
        });
        canvas.add(opToggle);

        JToggleButton userToggle = new JToggleButton();
        userToggle.setBounds(170, 560, 100, 30);
        userToggle.setText("Organizer");
        userToggle.addItemListener(e -> {
            if (userToggle.isSelected())
                userToggle.setText("Bidder");
            else
                userToggle.setText("Organizer");
        });
        canvas.add(userToggle);

        /*
         * After pressing button, all fields are checked if some of them are empty, if they are, a warning is
         * produced. Warning is also produced if password and check password don't match, or if the username is
         * already in use by other user.
         */
        JButton registerButton = new JButton("Sign up");
        registerButton.setBounds(170, 610, 100, 50);
        // Create new row in database
        registerButton.addActionListener (e -> {
            firstName = firstNameField.getText();
            lastName = lastNameField.getText();
            username = userNameField.getText();
            password = passwordField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String zip = zipField.getText();

            // Check empty fields
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() ||
                    passwordAgainField.getText().isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty() ||
                    zip.isEmpty()) {
                JOptionPane.showMessageDialog(canvas, "All fields must be filled in and in right format!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check username
            try {
                Statement stm = connection.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username = '" + username + "';");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(canvas, "Username already exists!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    userNameField.setText("");
                    return;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if (opToggle.isSelected())
                OP = opField.getText();
            else
                ICO = opField.getText();

            boolean bidder = userToggle.isSelected();

            // Check passwords
            if (!password.equals(passwordAgainField.getText())) {
                JOptionPane.showMessageDialog(canvas, "Passwords don't match!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                passwordField.setText("");
                passwordAgainField.setText("");
            }
            else {
                try {
                    save(street, city, state, zip, bidder);

                    // Log in new user
                    if (userToggle.isSelected()) {
                        Dashboard d = new Dashboard(connection, new Bidder(connection, id, firstName, lastName,
                                username, street, city, state, zip, opField.getText(), opToggle.isSelected()));
                    } else {
                        Dashboard d = new Dashboard(connection, new Organizer(connection, id, firstName, lastName,
                                username, street, city, state, zip, opField.getText(), opToggle.isSelected()));
                    }

                    frame.remove(canvas);
                    frame.setVisible(false);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        canvas.add(registerButton);

        frame.add(canvas);

        frame.setSize(440, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Set new personal information
     * @param firstName new first name
     * @param lastName  new last name
     */
    public void setPersonal(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        try {
            Statement stm = connection.createStatement();
            stm.executeQuery("UPDATE users SET first_name = " + this.firstName +
                    ", last_name = " + this.lastName +  " WHERE id = " + this.id + ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Save new username to database
     * @param new_username  new username
     */
    public void setUsername(String new_username) {
        this.username = new_username;

        try {
            Statement stm = connection.createStatement();
            stm.executeQuery("UPDATE users SET username = " + this.username + " WHERE id = " + this.id + ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Save new password to database
     * @param new_password  new password
     */
    public void setPassword(String new_password) {
        this.password = new_password;

        try {
            Statement stm = connection.createStatement();
            stm.executeQuery("UPDATE users SET password = " + this.password + " WHERE id = " + this.id + ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getId() {
        return id;
    }

    /**
     * Save new user to database. Function differentiates whether the account is personal (OP is filled in and
     * ICO is null) or company (ICO is filled in and OP is null) and saves the user with respective field. Function
     * also needs the adress line separated into street, city, state and zip as it is defined in database.
     * @param street    street from address line
     * @param city      city from address line
     * @param state     state from address line
     * @param zip       zip from address line
     * @param bidder    if true, user is bidder, if false, user is organizer
     * @throws SQLException database error
     */
    private void save(String street, String city, String state, String zip, boolean bidder) throws SQLException {
        Statement stm = connection.createStatement();
        String query= "INSERT INTO users ";
        Datetime currentDate = new Datetime(new java.util.Date());

        if (OP == null) {
            query += "(username, password, first_name, last_name, street, city, state, " +
                    "zip, ico, is_bidder, is_organizer, is_operator, created_at, updated_at) " +
                    "VALUES ('" + username + "', '" + password + "', '" + firstName + "', '" + lastName +
                    "', '" + street + "', '" + city + "', '" + state + "', '" + zip + "', '" + ICO + "', ";
        } else if (ICO == null) {
            query += "(username, password, first_name, last_name, street, city, state, " +
                    "zip, op, is_bidder, is_organizer, is_operator, created_at, updated_at) " +
                    "VALUES ('" + username + "', '" + password + "', '" + firstName + "', '" + lastName +
                    "', '" + street + "', '" + city + "', '" + state + "', '" + zip + "', '" + OP + "', ";
        }

        if (bidder) {
            query += "1, 0, 0, '" + currentDate.getDateString() + "', '" + currentDate.getDateString() + "');";
        } else {
            query += "0, 1, 0, '" + currentDate.getDateString() + "', '" + currentDate.getDateString() + "');";
        }

        stm.executeUpdate(query);
    }

    /**
     * Prepare string in HTML format to easily change color, size or font of text
     * @param color text color
     * @param size text size
     * @param text text
     * @return prepared text in HTML format
     */
    private String prepareHTML(String color, String size, String text) {
        return "<html><font color='" + color + "' face='Verdana' size='" + size + "'>" + text + "</font></html>";
    }

    /**
     * Create green bar at the top of window
     */
    private static class UserCanvas extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.getHSBColor(0.33f, 1.0f, 0.5f));
            g2d.fillRect(0, 0, 440, 60);
        }
    }
}
