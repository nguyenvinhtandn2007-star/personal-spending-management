package server;

import network.Request;
import network.Response;
import util.AppLogger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final RequestRouter router = new RequestRouter();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Socket client = socket;
             ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {

            Request request = (Request) in.readObject();
            Response response = router.handle(request);
            out.writeObject(response);
            out.flush();
        } catch (Exception e) {
            AppLogger.log("unknown", "CLIENT_HANDLER", "ERROR: " + e.getMessage());
        }
    }
}
