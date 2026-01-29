package ConnectSever;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    public static Connection getConnection() {
        try {
            String url =
                    "jdbc:sqlserver://ASUSF15;" +
                            "instanceName=SQLEXPRESS;" +
                            "databaseName=QLLL_CHI_TIEU_CA_NHAN;" +
                            "encrypt=true;" +
                            "trustServerCertificate=true;";

            String user = "sa";
            String password = "Demo@123";

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            System.out.println("Lỗi kết nối SQL Server");
            e.printStackTrace();
        }
        return null;
    }
}
