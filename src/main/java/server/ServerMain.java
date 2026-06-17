package server;

import config.AppConfig;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static void main(String[] args) {
        int port = AppConfig.getInt("server.port", 9000);
        int poolSize = AppConfig.getInt("server.threadPoolSize", 10);
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang chạy tại cổng " + port);
            while (true) {
                // Mỗi client được xử lý trong một thread của ThreadPool.
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
