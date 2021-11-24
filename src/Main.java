import users.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Main {
    private static final User[] loggedIn = {null};

    public static void main(String[] args) {
        Connection conn = connect("charity.sqlite");

        dashboard(conn);
    }

    public static void dashboard(Connection conn) {
        JFrame frame = new JFrame("e-charity");

        DashboardCanvas canvas = new DashboardCanvas();
        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(1280, 720));

        JLabel title = new JLabel("<html><h1><font color='white'>e-charity</font></h1></html>",  SwingConstants.CENTER);
        title.setBounds(400, 5, 480, 50);
        canvas.add(title);

        if (loggedIn[0] == null) {
            JLabel logInButton = new JLabel("<html><h3><font color='white'>Log in</font></h3></html>", SwingConstants.CENTER);
            logInButton.setBounds(1000, 5, 280, 50);

            logInButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JDialog login = new JDialog();
                    login.setLayout(null);

                    JLabel userNameLabel = new JLabel("Username:", SwingConstants.CENTER);
                    userNameLabel.setBounds(100, 5, 100, 25);
                    login.add(userNameLabel);

                    JTextField userNameField = new JTextField();
                    userNameField.setBounds(25, 30, 250, 30);
                    login.add(userNameField);

                    JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
                    passwordLabel.setBounds(100, 65, 100, 25);
                    login.add(passwordLabel);

                    JPasswordField passwordField = new JPasswordField();
                    passwordField.setBounds(25, 90, 250, 30);
                    login.add(passwordField);

                    JButton button = new JButton("Log in");
                    button.setBounds(100, 130, 100, 50);

                    button.addActionListener ( new ActionListener()
                    {
                        public void actionPerformed( ActionEvent e ) {
                            try {
                                Statement stm = conn.createStatement();
                                ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username = '" +
                                        userNameField.getText() + "' and password = '" + passwordField.getText() + "';");
                                if (rs.next()) {
                                    parseUser(rs);

                                    login.setVisible(false);
                                    frame.remove(canvas);
                                    dashboard(conn);
                                    frame.setVisible(false);
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    });
                    login.add(button);

                    login.setSize(300,300);
                    login.setVisible(true);
                }
            });
            canvas.add(logInButton);
        }
        else {
            JLabel loggedIn = new JLabel("<html><font color='white'>Logged in: " + Main.loggedIn[0].getUsername() +  "</font></html>");
            loggedIn.setBounds(1000, 5, 280, 50);
            canvas.add(loggedIn);
        }

        frame.add(canvas);

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void parseUser(ResultSet rs) throws SQLException {
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
            loggedIn[0] = new Bidder(rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("zip"), op_ico_number, op);
        }
        else if (rs.getInt("is_organizer") == 1) {
            loggedIn[0] = new Organizer(rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("zip"), op_ico_number, op);
        }
        else if (rs.getInt("is_operator") == 1) {
            loggedIn[0] = new Operator(rs.getInt("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("street"), rs.getString("city"),
                    rs.getString("zip"), op_ico_number, op);
        }
    }

    public static Connection connect(String dbname) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + dbname;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection successful");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
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