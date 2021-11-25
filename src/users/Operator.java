package users;
import java.sql.Connection;

public class Operator extends User{
    public Operator(Connection conn, int id, String firstName, String lastName, String username, String street,
                    String city, String zip, String op_ico_number, boolean op) {
        super(conn, id, firstName, lastName, username, street, city, zip, op_ico_number, op);
    }
}
