package users;

import payment.PaymentMethod;
import auction.Auction;
import java.util.ArrayList;

public class Bidder extends User{
    ArrayList<PaymentMethod> paymentMethods;
    ArrayList<Auction> auctions;

    public Bidder(int id, String firstName, String lastName, String username, String street,
                  String city, String zip, String op_ico_number, boolean op) {
        super(id, firstName, lastName, username, street, city, zip, op_ico_number, op);
    }


}
