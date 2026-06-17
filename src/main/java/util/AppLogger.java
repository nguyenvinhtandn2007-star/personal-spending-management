package util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public final class AppLogger {
    private AppLogger() {
    }

    public static synchronized void log(String username, String action, String result) {
        try (PrintWriter out = new PrintWriter(new FileWriter("server.log", true))) {
            out.printf("%s | %s | %s | %s%n", LocalDateTime.now(), username, action, result);
        } catch (Exception e) {
            System.err.println("Không ghi được log: " + e.getMessage());
        }
    }
}
