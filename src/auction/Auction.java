package auction;

import users.Organizer;

import java.util.ArrayList;

public class Auction {
    Organizer organizer;
    String name;
    String description;
    Charity charity;
    ArrayList<Bidding> biddingsList;
    Datetime auction_start;
    Datetime auction_end;

    public Auction(Organizer organizer, String name, String description, Charity charity,
                   ArrayList<Bidding> biddingsList, Datetime auction_start, Datetime auction_end) {
        this.organizer = organizer;
        this.name = name;
        this.description = description;
        this.charity = charity;
        this.biddingsList = biddingsList;
        this.auction_start = auction_start;
        this.auction_end = auction_end;
    }
}
