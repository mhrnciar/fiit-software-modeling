package auction;

import payment.Bank;

public class Charity {
    int id;
    String name;
    String description;
    Bank bank;

    public Charity(int id, String name, String description, Bank bank) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bank = bank;
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

    public Bank getBank() {
        return bank;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
