package auction;

import users.Organizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Auction {
    JFrame frame;
    AuctionCanvas canvas;

    int organizerId;
    int charityId;
    String name;
    String description;
    Datetime auctionStart;
    Datetime auctionEnd;

    public Auction(int organizerId, int charityId, String name, String description,
                   Datetime auctionStart, Datetime auctionEnd) {
        this.organizerId = organizerId;
        this.charityId = charityId;
        this.name = name;
        this.description = description;
        this.auctionStart = auctionStart;
        this.auctionEnd = auctionEnd;

        generateAuctionInfo();
    }

    public Auction() {
        generateAuctionCreation();
    }

    public void generateAuctionInfo() {
        frame = new JFrame(name);
        canvas = new AuctionCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(1280, 720));

        JLabel title = new JLabel(prepareHTML("white", "7", name),  SwingConstants.CENTER);
        title.setBounds(400, 5, 480, 50);
        canvas.add(title);


        frame.add(canvas);

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void generateAuctionCreation() {
        frame = new JFrame("Create new auction");
        canvas = new AuctionCanvas();

        canvas.setLayout(null);
        canvas.setPreferredSize(new Dimension(1280, 720));

        JLabel title = new JLabel(prepareHTML("white", "7", "Create new auction"),  SwingConstants.CENTER);
        title.setBounds(400, 5, 480, 50);
        canvas.add(title);


        frame.add(canvas);

        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

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

    private String prepareHTML(String color, String size, String text) {
        return "<html><font color='" + color + "' face='Verdana' size='" + size + "'>" + text + "</font></html>";
    }

    private static class AuctionCanvas extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.green);
            g2d.fillRect(0, 0, 1280, 60);
        }
    }
}
