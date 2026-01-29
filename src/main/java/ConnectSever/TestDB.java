package ConnectSever;

import ConnectSever.DBConnect;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {

        Connection conn = DBConnect.getConnection();

        if (conn != null) {
            System.out.println("OK");
        } else {
            System.out.println("NOT OK");
        }
    }
}
