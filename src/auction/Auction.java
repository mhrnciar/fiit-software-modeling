package auction;

import users.*;
import payment.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class Auction {
    JFrame frame;
    AuctionCanvas canvas;
    Connection connection;
    User loggedIn;

    int id;
    int organizerId;
    int charityId;
    String name;
    String description;
    Datetime auctionStart;
    Datetime auctionEnd;

    /**
     * Constructor for existing auction
     * @param user user that is currently logged in
     * @param organizerId id of organizer from database
     * @param charityId id of charity from database
     * @param name name of auction
     * @param description description of auction
     * @param auctionStart datetime of the start of the auction
     * @param auctionEnd datetime of the end of the auction
     */
    public Auction(User user, Connection conn, int id, int organizerId, int charityId, String name,
                   String description, Datetime auctionStart, Datetime auctionEnd) {
        this.loggedIn = user;
        this.connection = conn;
        this.id = id;
        this.organizerId = organizerId;
        this.charityId = charityId;
        this.name = name;
        this.description = description;
        this.auctionStart = auctionStart;
        this.auctionEnd = auctionEnd;
    }

    /**
     * Constructor for new auction
     * @param user user that is currently logged in
     */
    public Auction(User user, Connection conn) {
        this.loggedIn = user;
        this.connection = conn;
        generateAuctionCreation();
    }

    /**
     * Generate screen with information about auction
     */
    public void generateAuctionInfo() {
        frame = new JFrame("Auction information");
        canvas = new AuctionCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(440, 600));

        JLabel title = new JLabel(prepareHTML("white", "5", name),  SwingConstants.CENTER);
        title.setBounds(5, 5, 430, 50);
        canvas.add(title);

        JLabel charityLabel = new JLabel("Charity:");
        charityLabel.setBounds(20, 80, 100, 30);
        canvas.add(charityLabel);

        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM charities WHERE id = " + charityId + ";");
            if (rs.next()) {
                JLabel charityText = new JLabel(rs.getString("name"));
                charityText.setBounds(120, 80, 100, 30);
                canvas.add(charityText);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(20, 130, 100, 30);
        canvas.add(descriptionLabel);

        JTextArea descriptionText = new JTextArea(description);
        descriptionText.setBounds(120, 130, 300, 100);
        descriptionText.setLineWrap(true);
        descriptionText.setEditable(false);
        canvas.add(descriptionText);

        JLabel startLabel = new JLabel("Auction start:");
        startLabel.setBounds(20, 250, 100, 30);
        canvas.add(startLabel);

        JLabel startText = new JLabel(auctionStart.getDateString());
        startText.setBounds(120, 250, 200, 30);
        canvas.add(startText);

        JLabel endLabel = new JLabel("Auction end:");
        endLabel.setBounds(20, 300, 100, 30);
        canvas.add(endLabel);

        JLabel endText = new JLabel(auctionEnd.getDateString());
        endText.setBounds(120, 300, 200, 30);
        canvas.add(endText);

        JLabel biddingsLabel = new JLabel("Biddings:");
        biddingsLabel.setBounds(20, 350, 200, 30);
        canvas.add(biddingsLabel);

        // List biddings
        ArrayList<Bidding> biddings = new ArrayList<>();

        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM biddings WHERE auction_id = " + id + ";");
            while (rs.next()) {
                biddings.add(new Bidding(connection, loggedIn, rs.getInt("id"), id, rs.getString("name"),
                        rs.getString("description"), rs.getFloat("starting_bid"),
                        rs.getFloat("highest_bid"), rs.getInt("highest_bidder_id"),
                        new Datetime(rs.getString("bidding_start")), new Datetime(rs.getString("bidding_end"))));
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        int i = 390;
        for (Bidding b : biddings) {
            JLabel biddingText = new JLabel(prepareHTML("black", "4", b.getName()));
            biddingText.setBounds(50, i, 200, 30);
            biddingText.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    b.generateBiddingInfo();
                }
            });
            canvas.add(biddingText);

            JLabel biddingAmount;
            if (b.getHighestBid() == 0) {
                biddingAmount = new JLabel("Starting bid: " + b.getStartingBid() + "€");
            } else {
                biddingAmount = new JLabel("Highest bid: " + b.getHighestBid() + "€");
            }
            biddingAmount.setBounds(220, i, 190, 30);
            canvas.add(biddingAmount);

            i += 40;
        }

        frame.add(canvas);

        frame.setSize(440, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Generate screen for creating new auction
     */
    public void generateAuctionCreation() {
        frame = new JFrame("Create new auction");
        canvas = new AuctionCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(440, 520));

        JLabel title = new JLabel(prepareHTML("white", "6", "Create new auction"),  SwingConstants.CENTER);
        title.setBounds(5, 5, 430, 50);
        canvas.add(title);

        JLabel nameLabel = new JLabel("Name:", SwingConstants.CENTER);
        nameLabel.setBounds(20, 80, 100, 30);
        canvas.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(140, 80, 250, 30);
        canvas.add(nameField);

        JLabel charityLabel = new JLabel("Charity:", SwingConstants.CENTER);
        charityLabel.setBounds(20, 130, 100, 30);
        canvas.add(charityLabel);

        int i = 0;
        ArrayList<Charity> charities = new ArrayList<>();
        JComboBox<String> charitySelect = new JComboBox<>();

        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM charities;");
            while (rs.next()) {
                charities.add(new Charity(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), new Bank(rs.getString("bank_code"),
                        rs.getString("bank_number"))));
                charitySelect.addItem(charities.get(i++).getName());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        charitySelect.setBounds(140, 130, 250, 30);
        canvas.add(charitySelect);

        JLabel descriptionLabel = new JLabel("Description:", SwingConstants.CENTER);
        descriptionLabel.setBounds(20, 200, 100, 30);
        canvas.add(descriptionLabel);

        JTextArea descriptionField = new JTextArea(5, 30);
        //JScrollPane scrollPane = new JScrollPane(descriptionField);
        descriptionField.setBounds(140, 200, 250, 90);
        descriptionField.setLineWrap(true);
        canvas.add(descriptionField);

        JLabel startLabel = new JLabel("Auction start:", SwingConstants.CENTER);
        startLabel.setBounds(20, 310, 100, 30);
        canvas.add(startLabel);

        JSpinner startDateSpinner = new JSpinner( new SpinnerDateModel() );
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner,"yyyy-MM-dd HH:mm:ss"));
        startDateSpinner.setValue(new Date());
        startDateSpinner.setBounds(140, 310, 290, 30);
        canvas.add(startDateSpinner);

        JLabel timeLabel = new JLabel("Auction end:", SwingConstants.CENTER);
        timeLabel.setBounds(20, 360, 100, 30);
        canvas.add(timeLabel);

        JSpinner endDateSpinner = new JSpinner( new SpinnerDateModel() );
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner,"yyyy-MM-dd HH:mm:ss"));
        endDateSpinner.setValue(new Date());
        endDateSpinner.setBounds(140, 360, 290, 30);
        canvas.add(endDateSpinner);

        JButton createButton = new JButton("Create auction");
        createButton.setBounds(145, 410, 150, 50);

        createButton.addActionListener (e -> {
            this.organizerId = loggedIn.getId();
            this.name = nameField.getText();
            this.description = descriptionField.getText();
            this.charityId = charitySelect.getSelectedIndex() + 1;
            this.auctionStart = new Datetime((Date) startDateSpinner.getValue());
            this.auctionEnd = new Datetime((Date) endDateSpinner.getValue());

            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(canvas, "All fields must be filled in and in right format!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!auctionStart.isLower(auctionEnd)) {
                JOptionPane.showMessageDialog(canvas, "Start date must be before end date!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            save();

            Bidding b = new Bidding(connection, id, loggedIn);

            frame.remove(canvas);
            frame.setVisible(false);
        });
        canvas.add(createButton);

        frame.add(canvas);

        frame.setSize(440, 520);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public int getId() {
        return id;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public int getCharityId() {
        return charityId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Datetime getAuctionStart() {
        return auctionStart;
    }

    public Datetime getAuctionEnd() {
        return auctionEnd;
    }

    /**
     * Save new auction to database
     */
    public void save() {
        try {
            Datetime createdAt = new Datetime(new Date());
            Datetime updatedAt = new Datetime(new Date());

            Statement stm = connection.createStatement();

            stm.executeUpdate("INSERT INTO auctions (organizer_id, charity_id, name, " +
                    "description, auction_start, auction_end, created_at, updated_at) " +
                    "VALUES (" + organizerId + ", " + charityId + ", '" + name + "', '" +
                    description + "', '" + auctionStart.getDateString() + "', '" +
                    auctionEnd.getDateString() + "', '" + createdAt.getDateString() + "', '" +
                    updatedAt.getDateString() + "');");

            ResultSet rs = stm.executeQuery("SELECT * FROM auctions ORDER BY id DESC LIMIT 1");
            if (rs.next())
                id = rs.getInt("id");

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
     * Create green bar at the top of window
     */
    private static class AuctionCanvas extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.getHSBColor(0.33f, 1.0f, 0.5f));
            g2d.fillRect(0, 0, 440, 60);
        }
    }
}
