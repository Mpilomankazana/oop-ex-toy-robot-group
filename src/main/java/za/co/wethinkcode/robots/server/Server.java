package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.World;
import za.co.wethinkcode.robots.robot.Robot;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server listening on port 5000...");

        // CREATE WORLD (shared state for all clients)
        World world = new World(10, 10);

        // Console thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String cmd = scanner.nextLine().trim();
                    switch (cmd) {
                        case "quit":
                            System.out.println("Shutting down...");
                            System.exit(0);
                            break;
                        case "robots":
                            System.out.println("Connected robots:");
                            for (Robot robot : world.getRobots()) {
                                System.out.println("  - " + robot.getName());
                            }
                            break;
                        case "dump":
                            System.out.println("World size: " + world.getWidth() + "x" + world.getHeight());
                            System.out.println("Robots in world: " + world.getRobots().size());
                            for (Robot robot : world.getRobots()) {
                                System.out.println("  - " + robot.getName() +
                                    " at (" + robot.getX() + "," + robot.getY() + ")");
                            }
                            break;
                        default:
                            System.out.println("Unknown command: " + cmd);
                            break;
                    }
                }
            }
        }).start();

        // Accept clients
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");
            new Thread(new ClientHandler(socket, world)).start();
        }
    }

    static {
        new Recorder().logRun();
    }
}