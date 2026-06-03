package za.co.wethinkcode.robots.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.world.World;

import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    private Server server;
    private Thread serverThread;

    @BeforeEach
    void startServer() {
        server = new Server(new World(10, 10));
        serverThread = new Thread(() -> server.start());
        serverThread.setDaemon(true);
        serverThread.start();
        try { Thread.sleep(300); }
        catch (InterruptedException ignored) {}
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void shouldConnectClientToServer() {
        assertDoesNotThrow(() -> {
            Socket socket = new Socket("localhost", 5000);
            socket.close();
        });
    }

    @Test
    void shouldAllowMultipleClients() {
        assertDoesNotThrow(() -> {
            Socket client1 = new Socket("localhost", 5000);
            Socket client2 = new Socket("localhost", 5000);
            client1.close();
            client2.close();
        });
    }

    @Test
    void shouldCloseConnection() {
        assertDoesNotThrow(() -> {
            Socket socket = new Socket("localhost", 5000);
            socket.close();
        });
    }
}

