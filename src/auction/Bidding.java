package auction;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Bidding {
    JFrame frame;
    BiddingCanvas canvas;
    Connection connection;

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

            stm.executeQuery("INSERT INTO biddings (auction_id, name, description, " +
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
}
