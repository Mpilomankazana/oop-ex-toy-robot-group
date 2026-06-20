package za.co.wethinkcode.robots.server;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.world.World;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;



public class ClientHandlerTest {

    @Test
    void shouldRespondToClientMessage() throws Exception {

        World world = new World(10, 10);

        ServerSocket serverSocket = new ServerSocket(6000);

        Thread serverThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();

                ClientHandler handler = new ClientHandler(socket, world);
                handler.run();
            } catch (Exception e) {
                fail(e);
            }
        });

        serverThread.start();

        Socket client = new Socket("localhost", 6000);

        PrintWriter out = new PrintWriter(
                client.getOutputStream(), true);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));

        out.println("{\"robot\":\"HAL\",\"command\":\"launch\",\"arguments\":[\"sniper\",3,5]}");

        String response = in.readLine();

        assertNotNull(response);
        assertTrue(response.contains("OK"));

        client.close();
        serverSocket.close();
    }

    @Test
    void shouldHandleMultipleMessages() throws Exception {

        World world = new World(10, 10);

        ServerSocket serverSocket = new ServerSocket(6001);

        Thread serverThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();

                ClientHandler handler = new ClientHandler(socket, world);
                handler.run();
            } catch (Exception e) {
                fail(e);
            }
        });

        serverThread.start();

        Socket client = new Socket("localhost", 6001);

        PrintWriter out = new PrintWriter(
                client.getOutputStream(), true);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));

        out.println("{\"command\":\"one\"}");
        String response1 = in.readLine();

        out.println("{\"command\":\"two\"}");
        String response2 = in.readLine();

        assertNotNull(response1);
        assertNotNull(response2);

        client.close();
        serverSocket.close();
    }

    @Test
    void shouldNotCrashOnEmptyMessage() throws Exception {

        World world = new World(10, 10);

        ServerSocket serverSocket = new ServerSocket(6002);

        Thread serverThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();

                ClientHandler handler = new ClientHandler(socket, world);
                handler.run();
            } catch (Exception e) {
                fail(e);
            }
        });

        serverThread.start();

        Socket client = new Socket("localhost", 6002);

        PrintWriter out = new PrintWriter(
                client.getOutputStream(), true);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));

        out.println("");

        String response = in.readLine();

        assertNotNull(response);

        client.close();
        serverSocket.close();
    }

    @Test
    void shouldReturnErrorForInvalidJson() throws Exception{

        World world = new World(10, 10);

        ServerSocket serverSocket = new ServerSocket(6003);

        Thread serverThread = new Thread(() ->{
            try {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, world).run();
            } catch (Exception e) {
                fail(e);
            }
        });

        serverThread.start();

        Socket client = new Socket("localhost", 6003);

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(
                                client.getInputStream()));

        out.println("{invalid}");

        String response = in.readLine();

        assertTrue(response.contains("ERROR"));

        client.close();
        serverSocket.close();
    }

    @Test
    void shouldHandleUnknownCommand() throws Exception{

        World world = new World(10, 10);

        ServerSocket serverSocket = new ServerSocket(6004);

        Thread serverThread = new Thread(() ->{
            try{
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, world).run();
            } catch (Exception e) {
                fail(e);
            }
        });

        serverThread.start();

        Socket client = new Socket("localhost", 6004);

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(
                                client.getInputStream()));

        out.println("""
                {
                    "robot":"HAL",
                    "command":"dance",
                    "arguments":[]
                }
                """);

        String response = in.readLine();

        assertTrue(response.contains("Command not implemented"));
    }

    @Test
    void shouldReturnRobotNotFoundForState() throws Exception{

        World world = new World(10, 10);

        ServerSocket serverSocket = new ServerSocket(6005);

        Thread serverThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, world).run();
            } catch (Exception e) {
                fail(e);
            }
        });

        serverThread.start();

        Socket client = new Socket("localhost", 6005);

        PrintWriter out =
                new PrintWriter(client.getOutputStream(), true);

        BufferedReader in  =
                new BufferedReader(
                        new InputStreamReader(
                                client.getInputStream()));

        out.println("""
                {
                    "robot":"HAL",
                    "command":"state",
                    "arguments":[]
                }
                """);

        String response = in.readLine();

        assertTrue(response.contains("Robot not found"));
    }

    @Test
    void shouldRejectInvalidTurnDirection() throws Exception{

        World world = new World(10, 10);

        ServerSocket serverSocket = new ServerSocket(6006);

        Thread serverThread = new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, world).run();
            } catch (Exception e) {
                fail(e);
            }
        });

        serverThread.start();

        Socket client = new Socket("localhost", 6006);

        PrintWriter out =
                new PrintWriter(client.getOutputStream(), true);

        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(
                                client.getInputStream()));

        out.println("""
        {
          "robot":"HAL",
          "command":"launch",
          "arguments":[]
        }
        """);

        in.readLine();

        out.println("""
        {
          "robot":"HAL",
          "command":"turn",
          "arguments":["spin"]
        }
        """);

        String response = in.readLine();

        assertTrue(
                response.contains(
                        "Invalid direction"));
    }
}
