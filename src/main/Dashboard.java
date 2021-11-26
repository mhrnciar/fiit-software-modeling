package main;

import auction.*;
import users.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

public class Dashboard {
    JFrame frame;
    DashboardCanvas canvas;
    Connection connection;
    ArrayList<JComponent> components = new ArrayList<>();
    User loggedIn;

    public Dashboard(Connection conn, User user) {
        frame = new JFrame("Dashboard");
        connection = conn;
        loggedIn = user;
        generateDashboard();
    }

    /**
     * Generate dashboard screen with graphical objects declared DashboardCanvas. On the top right is
     * a button to sign in or sign up. Only Bidder or Organizer can sign up, Operator has to be created
     * through database.
     *
     * After clicking on sign in button, a new dialog opens with fields for username and password. After
     * inserting the login information, it is looked up in database and if the username and password match,
     * the user is logged in, otherwise nothing happens.
     * When choosing the sign up button, a new dialog opens with additional data for registration. Again,
     * the inserted information is validated, new user is created in the database and logged in.
     *
     * Every class of User can do other things, so after login is invoked function createComponents(), which
     * prepares additional buttons for every user according to their class.
     *
     * Under the top bar is a list of auctions ordered by name.
     */
    public void generateDashboard() {
        canvas = new DashboardCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(1280, 720));

        JLabel title = new JLabel(prepareHTML("white", "7", "e-charity"),  SwingConstants.CENTER);
        title.setBounds(400, 5, 480, 50);
        canvas.add(title);

        // No user is logged in - show sign in and sign up buttons
        if (loggedIn == null) {
            JLabel loginLabel = new JLabel(prepareHTML("white", "5", "Sign in"));
            loginLabel.setBounds(1050, 5, 100, 50);

            // Sign in dialog
            loginLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JDialog loginDialog = new JDialog();
                    loginDialog.setLayout(null);

                    JLabel userNameLabel = new JLabel("Username:", SwingConstants.CENTER);
                    userNameLabel.setBounds(100, 5, 100, 30);
                    loginDialog.add(userNameLabel);

                    JTextField userNameField = new JTextField();
                    userNameField.setBounds(25, 35, 250, 30);
                    loginDialog.add(userNameField);

                    JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
                    passwordLabel.setBounds(100, 70, 100, 30);
                    loginDialog.add(passwordLabel);

                    JPasswordField passwordField = new JPasswordField();
                    passwordField.setBounds(25, 100, 250, 30);
                    loginDialog.add(passwordField);

                    JButton loginButton = new JButton("Log in");
                    loginButton.setBounds(100, 130, 100, 50);

                    // Check if user exists and passwords match
                    loginButton.addActionListener (e1 -> {
                        if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(canvas, "All fields must be filled in and in right format!",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        try {
                            Statement stm = connection.createStatement();
                            ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username = '" +
                                    userNameField.getText() + "' and password = '" + passwordField.getText() + "';");

                            if (rs.next()) {
                                parseUser(rs);

                                frame.remove(canvas);
                                generateDashboard();
                                loginDialog.setVisible(false);
                            }
                            else {
                                JOptionPane.showMessageDialog(loginDialog, "Username and password don't match!",
                                        "Warning", JOptionPane.WARNING_MESSAGE);
                                passwordField.setText("");
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });
                    loginDialog.add(loginButton);

                    loginDialog.setSize(300,300);
                    loginDialog.setVisible(true);
                }
            });
            canvas.add(loginLabel);

            JLabel registerLabel = new JLabel(prepareHTML("white", "5", "Sign up"));
            registerLabel.setBounds(1175, 5, 80, 50);

            // Sign up dialog
            registerLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    User user = new User(connection);
                    frame.remove(canvas);
                    frame.setVisible(false);
                }
            });
            canvas.add(registerLabel);

        }
        // Some user is logged in - show name and logout button
        else {
            JLabel loggedinUser = new JLabel(prepareHTML("white", "4", "Hello, " + loggedIn.getFirstName() + "!"));
            loggedinUser.setBounds(1050, 5, 230, 50);
            canvas.add(loggedinUser);

            JLabel logoutLabel = new JLabel(prepareHTML("white", "5", "Logout"));
            logoutLabel.setBounds(1175, 5, 80, 50);
            logoutLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    loggedIn = null;
                    canvas.setVisible(false);
                    generateDashboard();
                }
            });
            canvas.add(logoutLabel);

            components.clear();
            createComponents();

            for (Component c : components) {
                canvas.add(c);
            }
        }

        // List auctions
        ArrayList<Auction> auctions = new ArrayList<>();

        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM auctions ORDER BY name LIMIT 5;");
            while (rs.next()) {
                auctions.add(new Auction(loggedIn, connection, rs.getInt("id"),
                        rs.getInt("organizer_id"), rs.getInt("charity_id"), rs.getString("name"),
                        rs.getString("description"), new Datetime(rs.getString("auction_start")),
                        new Datetime(rs.getString("auction_end"))));
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        int i = 70;
        for (Auction a : auctions) {
            JLabel auctionLabel = new JLabel(prepareHTML("black", "5", a.getName()));
            auctionLabel.setBounds(100, i, 1080, 80);
            auctionLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    a.generateAuctionInfo();
                }
            });

            canvas.add(auctionLabel);
            i += 80;
        }

        frame.add(canvas);

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Parse user data from database and determine their class
     * @param rs found user
     * @throws SQLException database error
     */
    private void parseUser(ResultSet rs) throws SQLException {
        boolean op = false;
        String op_ico_number = null;
        if (rs.getString("op") == null) {
            op_ico_number = rs.getString("ico");
            op = false;
        }
        else if (rs.getString("op") != null) {
            op_ico_number = rs.getString("op");
            op = true;
        }

        if (rs.getInt("is_bidder") == 1) {
            loggedIn = new Bidder(connection, rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("state"), rs.getString("zip"), op_ico_number, op);
        }
        else if (rs.getInt("is_organizer") == 1) {
            loggedIn = new Organizer(connection, rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("state"), rs.getString("zip"), op_ico_number, op);
        }
        else if (rs.getInt("is_operator") == 1) {
            loggedIn = new Operator(connection, rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("state"), rs.getString("zip"), op_ico_number, op);
        }
    }

    /**
     * Create additional labels and buttons according to the class of logged in user
     */
    private void createComponents() {
        components.clear();

        if (loggedIn.getClass() == Organizer.class) {
            JLabel createAuctionLabel = new JLabel(prepareHTML("white", "4", "Create new auction"), SwingConstants.CENTER);
            createAuctionLabel.setBounds(10, 5, 150, 50);
            createAuctionLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Auction auction = new Auction(loggedIn, connection);
                    frame.remove(canvas);
                    frame.setVisible(false);
                }
            });
            components.add(createAuctionLabel);
        }
        else if (loggedIn.getClass() == Bidder.class) {
            JLabel reportAuctionLabel = new JLabel(prepareHTML("white", "4", "Report auction"), SwingConstants.CENTER);
            reportAuctionLabel.setBounds(10, 5, 150, 50);
            components.add(reportAuctionLabel);
        }
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
     * Create blue bar at the top of window
     */
    private static class DashboardCanvas extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.blue);
            g2d.fillRect(0, 0, 1280, 60);
        }
    }
}
