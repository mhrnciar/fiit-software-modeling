import auction.Auction;
import users.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Dashboard {
    JFrame frame;
    DashboardCanvas canvas;
    Connection connection;
    ArrayList<JComponent> components = new ArrayList<>();
    User loggedIn = null;

    public Dashboard(Connection conn) {
        frame = new JFrame("Dashboard");
        connection = conn;
        generateDashboard();
    }

    public void generateDashboard() {
        canvas = new DashboardCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(1280, 720));

        JLabel title = new JLabel(prepareHTML("white", "7", "e-charity"),  SwingConstants.CENTER);
        title.setBounds(400, 5, 480, 50);
        canvas.add(title);

        if (loggedIn == null) {
            JLabel loginLabel = new JLabel(prepareHTML("white", "5", "Log in"), SwingConstants.CENTER);
            loginLabel.setBounds(1150, 5, 90, 50);

            loginLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JDialog loginDialog = new JDialog();
                    loginDialog.setLayout(null);

                    JLabel userNameLabel = new JLabel("Username:", SwingConstants.CENTER);
                    userNameLabel.setBounds(100, 5, 100, 25);
                    loginDialog.add(userNameLabel);

                    JTextField userNameField = new JTextField();
                    userNameField.setBounds(25, 30, 250, 30);
                    loginDialog.add(userNameField);

                    JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
                    passwordLabel.setBounds(100, 65, 100, 25);
                    loginDialog.add(passwordLabel);

                    JPasswordField passwordField = new JPasswordField();
                    passwordField.setBounds(25, 90, 250, 30);
                    loginDialog.add(passwordField);

                    JButton loginButton = new JButton("Log in");
                    loginButton.setBounds(100, 130, 100, 50);

                    loginButton.addActionListener (e1 -> {
                        try {
                            Statement stm = connection.createStatement();
                            ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username = '" +
                                    userNameField.getText() + "' and password = '" + passwordField.getText() + "';");
                            if (rs.next()) {
                                parseUser(rs);

                                canvas.setVisible(false);
                                generateDashboard();
                                loginDialog.setVisible(false);
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
        }
        else {
            JLabel loggedinUser = new JLabel(prepareHTML("white", "4", "Hello, " + loggedIn.getFirstName() + "!"));
            loggedinUser.setBounds(1050, 5, 230, 50);
            canvas.add(loggedinUser);

            JLabel logoutLabel = new JLabel(prepareHTML("white", "5", "Logout"), SwingConstants.CENTER);
            logoutLabel.setBounds(1150, 5, 90, 50);
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

        try {
            int i = 70;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM auctions ORDER BY name LIMIT 5;");
            while (rs.next()) {
                JLabel auctionLabel = new JLabel(prepareHTML("black", "4", rs.getString("name")));
                auctionLabel.setBounds(100, i, 1080, 80);
                canvas.add(auctionLabel);
                i += 80;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        frame.add(canvas);

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

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
            loggedIn = new Bidder(rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("zip"), op_ico_number, op);
        }
        else if (rs.getInt("is_organizer") == 1) {
            loggedIn = new Organizer(rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("zip"), op_ico_number, op);
        }
        else if (rs.getInt("is_operator") == 1) {
            loggedIn = new Operator(rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("zip"), op_ico_number, op);
        }
    }

    private void createComponents() {
        JLabel createAuctionLabel = new JLabel(prepareHTML("white", "4", "Create new auction"), SwingConstants.CENTER);
        createAuctionLabel.setBounds(5, 5, 150, 50);
        createAuctionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Auction auction = new Auction();
                canvas.setVisible(false);
                generateDashboard();
            }
        });
        components.add(createAuctionLabel);
    }

    private String prepareHTML(String color, String size, String text) {
        return "<html><font color='" + color + "' face='Verdana' size='" + size + "'>" + text + "</font></html>";
    }

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
