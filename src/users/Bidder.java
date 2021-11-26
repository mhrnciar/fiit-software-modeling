package users;

import payment.*;
import auction.*;

import java.sql.Connection;
import java.util.ArrayList;

public class Bidder extends User{
    ArrayList<PaymentMethod> paymentMethods;
    ArrayList<Auction> auctions;

    public Bidder(Connection conn, int id, String firstName, String lastName, String username, String street,
                  String city, String state, String zip, String op_ico_number, boolean op) {
        super(conn, id, firstName, lastName, username, street, city, state, zip, op_ico_number, op);
    }


}
