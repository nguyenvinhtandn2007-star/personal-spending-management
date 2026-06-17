package ConnectSever;

import config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    public static Connection getConnection() {
        try {
            // Server đọc thông tin database từ app.properties để dễ đổi máy/chỉnh cổng.
            String url = AppConfig.get("database.url", "");
            String user = AppConfig.get("database.user", "sa");
            String password = AppConfig.get("database.password", "");

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Lỗi kết nối SQL Server");
            e.printStackTrace();
            return null;
        }
    }
}
