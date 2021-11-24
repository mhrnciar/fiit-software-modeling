package auction;

import users.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Auction {
    JFrame frame;
    AuctionCanvas canvas;

    int id;
    int organizerId;
    int charityId;
    String name;
    String description;
    Datetime auctionStart;
    Datetime auctionEnd;

    /**
     * Constructor for existing auction
     * @param organizerId id of organizer from database
     * @param charityId id of charity from database
     * @param name name of auction
     * @param description description of auction
     * @param auctionStart datetime of the start of the auction
     * @param auctionEnd datetime of the end of the auction
     */
    public Auction(int id, int organizerId, int charityId, String name, String description,
                   Datetime auctionStart, Datetime auctionEnd) {
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
     */
    public Auction() {
        generateAuctionCreation();
    }

    /**
     * Generate screen with information about auction
     */
    public void generateAuctionInfo() {
        frame = new JFrame(name);
        canvas = new AuctionCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(720, 720));

        JLabel title = new JLabel(prepareHTML("white", "6", name),  SwingConstants.CENTER);
        title.setBounds(50, 5, 620, 50);
        canvas.add(title);


        frame.add(canvas);

        frame.setSize(720, 720);
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
        canvas.setPreferredSize(new Dimension(720, 720));

        JLabel title = new JLabel(prepareHTML("white", "7", "Create new auction"),  SwingConstants.CENTER);
        title.setBounds(50, 5, 620, 50);
        canvas.add(title);


        frame.add(canvas);

        frame.setSize(720, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Save new auction to database
     */
    public void save() {
        try {
            String url = "jdbc:sqlite:charity.sqlite";
            Connection conn = DriverManager.getConnection(url);

            Datetime createdAt = new Datetime(new Date());
            Datetime updatedAt = new Datetime(new Date());

            Statement stm = conn.createStatement();

            stm.executeQuery("INSERT INTO auctions (organizer_id, charity_id, name, " +
                    "description, auction_start, auction_end, created_at, updated_at) " +
                    "VALUES (" + organizerId + ", " + charityId + ", '" + name + "', '" +
                    description + "', '" + auctionStart.getDateString() + "', '" +
                    auctionEnd.getDateString() + "', '" + createdAt.getDateString() + "', '" +
                    updatedAt.getDateString() + "');");

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
     * Create green bar at the top of window
     */
    private static class AuctionCanvas extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.getHSBColor(0.33f, 1.0f, 0.5f));
            g2d.fillRect(0, 0, 720, 60);
        }
    }
}
