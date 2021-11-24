package auction;

import payment.Bank;

public class Charity {
    String name;
    String description;
    Bank bank;

    public Charity(String name, String description, Bank bank) {
        this.name = name;
        this.description = description;
        this.bank = bank;
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
