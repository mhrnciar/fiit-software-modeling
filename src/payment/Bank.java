package payment;

public class Bank extends PaymentMethod{
    String bankCode;
    String bankNumber;

    public Bank(String bankCode, String bankNumber) {
        this.bankCode = bankCode;
        this.bankNumber = bankNumber;
    }
}
