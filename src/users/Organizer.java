package users;

import payment.PaymentMethod;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Organizer extends User{
    ArrayList<PaymentMethod> paymentMethods;

    public Organizer(int id, String firstName, String lastName, String username, String street,
                     String city, String zip, String op_ico_number, boolean op) {
        super(id, firstName, lastName, username, street, city, zip, op_ico_number, op);
    }

    private static class AuctionCreationCanvas extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.white);

            g2d.setColor(Color.blue);
            g2d.fillRect(0, 0, 1280, 50);
        }
    }
}
