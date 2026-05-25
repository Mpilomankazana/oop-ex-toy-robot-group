package za.co.wethinkcode.robots.server;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;


//Tests server connections.
@Disabled("Temporarily disabled until server startup is automated")
public class ServerTest {

    //Server should accept a connection.
    @Test
    void shouldConnectClientToServer() {

        assertDoesNotThrow(() -> {
            Socket socket = new Socket("localhost", 5000);
        });
    }

    //Server should support mulitple clients.
    @Test
    void shouldAllowMultipleClients() {

        assertDoesNotThrow(() -> {

            Socket client1 = new Socket("localhost", 5000);
            Socket client2 = new Socket("localhost", 5000);
        });
    }

    //Server should disconnect properly.
    @Test
    void shouldCloseConnection() {

        assertDoesNotThrow(() -> {
            Socket socket = new Socket("localhost", 5000);
            socket.close();
        });
    }
}
