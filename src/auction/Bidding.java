package auction;

import main.Dashboard;
import users.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Bidding {
    JFrame frame;
    BiddingCanvas canvas;
    Connection connection;
    User loggedIn;

    int id;
    int auctionId;
    String name;
    String description;
    float startingBid;
    float highestBid;
    int highestBidderId;
    Datetime biddingStart;
    Datetime biddingEnd;

    public Bidding(Connection conn, int id, int auctionId, String name, String description, float startingBid,
                   float highestBid, int highestBidderId, Datetime biddingStart, Datetime biddingEnd) {
        this.connection = conn;

        this.id = id;
        this.auctionId = auctionId;
        this.name = name;
        this.description = description;
        this.startingBid = startingBid;
        this.highestBid = highestBid;
        this.highestBidderId = highestBidderId;
        this.biddingStart = biddingStart;
        this.biddingEnd = biddingEnd;
    }

    public Bidding(Connection conn, int auctionId, User loggedIn) {
        this.connection = conn;
        this.auctionId = auctionId;
        this.loggedIn = loggedIn;

        generateBiddingCreation();
    }

    public void generateBiddingInfo() {
        frame = new JFrame("Bidding information");
        canvas = new BiddingCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(440, 720));

        JLabel title = new JLabel(prepareHTML("white", "5", name),  SwingConstants.CENTER);
        title.setBounds(5, 5, 430, 50);
        canvas.add(title);

        frame.add(canvas);

        frame.setSize(440, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Generate screen for creating new auction
     */
    public void generateBiddingCreation() {
        frame = new JFrame("Create new auction");
        canvas = new BiddingCreationCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(440, 520));

        JLabel title = new JLabel(prepareHTML("white", "6", "Create new bidding"),  SwingConstants.CENTER);
        title.setBounds(5, 5, 430, 50);
        canvas.add(title);

        JLabel nameLabel = new JLabel("Name:", SwingConstants.CENTER);
        nameLabel.setBounds(20, 80, 100, 30);
        canvas.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(140, 80, 250, 30);
        canvas.add(nameField);

        JLabel descriptionLabel = new JLabel("Description:", SwingConstants.CENTER);
        descriptionLabel.setBounds(20, 130, 100, 30);
        canvas.add(descriptionLabel);

        JTextArea descriptionField = new JTextArea(5, 30);
        //JScrollPane scrollPane = new JScrollPane(descriptionField);
        descriptionField.setBounds(140, 130, 250, 90);
        descriptionField.setLineWrap(true);
        canvas.add(descriptionField);

        JLabel startingBidLabel = new JLabel("Starting bid:", SwingConstants.CENTER);
        startingBidLabel.setBounds(20, 240, 100, 30);
        canvas.add(startingBidLabel);

        JSpinner startingBidSpinner = new JSpinner(new SpinnerNumberModel(0.0, null, null, 1.0));
        startingBidSpinner.setBounds(140, 240, 250, 30);
        canvas.add(startingBidSpinner);

        JLabel startLabel = new JLabel("Bidding start:", SwingConstants.CENTER);
        startLabel.setBounds(20, 290, 100, 30);
        canvas.add(startLabel);

        JSpinner startDateSpinner = new JSpinner( new SpinnerDateModel() );
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner,"yyyy-MM-dd HH:mm:ss"));
        startDateSpinner.setValue(new Date());
        startDateSpinner.setBounds(140, 290, 290, 30);
        canvas.add(startDateSpinner);

        JLabel timeLabel = new JLabel("Bidding end:", SwingConstants.CENTER);
        timeLabel.setBounds(20, 340, 100, 30);
        canvas.add(timeLabel);

        JSpinner endDateSpinner = new JSpinner( new SpinnerDateModel() );
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner,"yyyy-MM-dd HH:mm:ss"));
        endDateSpinner.setValue(new Date());
        endDateSpinner.setBounds(140, 340, 290, 30);
        canvas.add(endDateSpinner);

        JButton createButton = new JButton("Save and create another");
        createButton.setBounds(10, 390, 200, 50);

        createButton.addActionListener (e -> {
            this.name = nameField.getText();
            this.description = descriptionField.getText();
            this.startingBid = ((Double) startingBidSpinner.getValue()).floatValue();
            this.biddingStart = new Datetime((Date) startDateSpinner.getValue());
            this.biddingEnd = new Datetime((Date) endDateSpinner.getValue());
            save();

            Bidding b = new Bidding(connection, auctionId, loggedIn);

            frame.remove(canvas);
            frame.setVisible(false);
        });
        canvas.add(createButton);

        JButton finishButton = new JButton("Save and finish");
        finishButton.setBounds(230, 390, 200, 50);
        finishButton.addActionListener (e -> {
            this.name = nameField.getText();
            this.description = descriptionField.getText();
            this.startingBid = ((Double) startingBidSpinner.getValue()).floatValue();
            this.biddingStart = new Datetime((Date) startDateSpinner.getValue());
            this.biddingEnd = new Datetime((Date) endDateSpinner.getValue());
            save();

            Dashboard d = new Dashboard(connection, loggedIn);

            frame.remove(canvas);
            frame.setVisible(false);
        });
        canvas.add(finishButton);

        frame.add(canvas);

        frame.setSize(440, 520);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getStartingBid() {
        return startingBid;
    }

    public float getHighestBid() {
        return highestBid;
    }

    public int getHighestBidderId() {
        return highestBidderId;
    }

    public Datetime getBiddingStart() {
        return biddingStart;
    }

    public Datetime getBiddingEnd() {
        return biddingEnd;
    }

    /**
     * Save new bidding to database
     */
    public void save() {
        try {
            Datetime createdAt = new Datetime(new Date());
            Datetime updatedAt = new Datetime(new Date());

            Statement stm = connection.createStatement();

            stm.executeUpdate("INSERT INTO biddings (auction_id, name, description, " +
                    "starting_bid, bidding_start, bidding_end, created_at, updated_at) " +
                    "VALUES (" + auctionId + ", '" + name + "', '" + description + "', " + startingBid + ", '" +
                    biddingStart.getDateString() + "', '" + biddingEnd.getDateString() + "', '" +
                    createdAt.getDateString() + "', '" + updatedAt.getDateString() + "');");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
     * Create red bar at the top of window
     */
    private static class BiddingCanvas extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.red);
            g2d.fillRect(0, 0, 440, 60);
        }
    }

    /**
     * Create red bar at the top of window
     */
    private static class BiddingCreationCanvas extends BiddingCanvas {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.getHSBColor(0.33f, 1.0f, 0.5f));
            g2d.fillRect(0, 0, 440, 60);
        }
    }
}
