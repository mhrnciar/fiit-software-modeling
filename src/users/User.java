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

    public User(Connection conn) {
        this.connection = conn;
        frame = new JFrame("Create new user");
        canvas = new UserCanvas();
        createUser();
    }

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

            if (opToggle.isSelected())
                OP = opField.getText();
            else
                ICO = opField.getText();

            boolean bidder = userToggle.isSelected();

            if (!password.equals(passwordAgainField.getText())) {
                JOptionPane.showMessageDialog(canvas, "Passwords don't match!", "Warning", JOptionPane.WARNING_MESSAGE);
                passwordField.setText("");
                passwordAgainField.setText("");
            }
            else {
                try {
                    save(street, city, state, zip, bidder);

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

    public void setUsername(String username) {
        this.username = username;

        try {
            Statement stm = connection.createStatement();
            stm.executeQuery("UPDATE users SET username = " + this.username + " WHERE id = " + this.id + ";");

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

    public void setPassword(String password) {
        this.password = password;

        try {
            Statement stm = connection.createStatement();
            stm.executeQuery("UPDATE users SET password = " + this.password + " WHERE id = " + this.id + ";");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    private void save(String street, String city, String state, String zip, boolean bidder) throws SQLException {
        Statement stm = connection.createStatement();
        String query= "INSERT INTO users ";
        Datetime createdAt = new Datetime(new java.util.Date());
        Datetime updatedAt = new Datetime(new Date());

        if (OP == null) {
            query += "(username, password, first_name, last_name, street, city, state, " +
                    "zip, ico, is_bidder, is_organizer, is_operator, created_at, updated_at) " +
                    "VALUES ('" + username + "', '" + password + "', '" + firstName + "', '" + lastName +
                    "', '" + street + "', '" + city + "', '" + state + "', '" + zip + "', '" + ICO + "', ";
        } else if (ICO == null) {
            query += "(username, password, first_name, last_name, street, city, state, " +
                    "zip, ico, is_bidder, is_organizer, is_operator, created_at, updated_at) " +
                    "VALUES ('" + username + "', '" + password + "', '" + firstName + "', '" + lastName +
                    "', '" + street + "', '" + city + "', '" + state + "', '" + zip + "', '" + OP + "', ";
        }

        if (bidder) {
            query += "1, 0, 0, '" + createdAt.getDateString() + "', '" + updatedAt.getDateString() + "');";
        } else {
            query += "0, 1, 0, '" + createdAt.getDateString() + "', '" + updatedAt.getDateString() + "');";
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
