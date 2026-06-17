package config;

import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    private static final Properties PROPERTIES = new Properties();

    static {
        // Đọc cấu hình từ file resources để không hard-code toàn bộ thông tin trong source.
        try (InputStream in = AppConfig.class.getResourceAsStream("/app.properties")) {
            if (in != null) {
                PROPERTIES.load(in);
            }
        } catch (Exception e) {
            System.err.println("Không đọc được app.properties, dùng cấu hình mặc định.");
        }
    }

    private AppConfig() {
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
