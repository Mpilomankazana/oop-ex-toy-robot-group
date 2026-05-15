package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.robots.world.World;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final World world;

    public ClientHandler(Socket socket, World world) {
        this.socket = socket;
        this.world = world;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true)
        ) {
            String line;

            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);

                // TEMP RESPONSE (we will replace with CommandProcessor later)
                out.println("{\"result\":\"OK\",\"data\":{\"message\":\"received\"}}");
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}