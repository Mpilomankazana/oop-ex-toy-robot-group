package za.co.wethinkcode.robots.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;

/**
 * The main server class for the Robot World application.
 * Listens on port 5000 for incoming robot client connections.
 * Each connected client is handled in its own thread via ClientHandler.
 * Loads world configuration from config.properties on startup.
 */
public class Server {

    private final World world;
    private ServerSocket serverSocket;

    public Server(World world) {
        this.world = world;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server listening on port 5000...");

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
                                List<Robot> allRobots = world.getRobots();
                                if (allRobots.isEmpty()) {
                                    System.out.println("No robots in the world.");
                                } else {
                                    System.out.println("Robots in world");
                                    for (Robot robot : allRobots) {
                                        System.out.println(" " + robot.toString());
                                    }
                                }
                                break;
                            case "dump":
                                System.out.println("World size: " + world.getWidth() + "x" + world.getHeight());
                                System.out.println("Reload time: " + world.getReloadTime() + " seconds");
                                System.out.println("Repair time: " + world.getRepairTime() + " seconds");
                                System.out.println("Max shields: " + world.getMaxShields());
                                System.out.println("Robots in world: " + world.getRobots().size());
                                for (Robot robot : world.getRobots()) {
                                    System.out.println("  - " + robot.toString());
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
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                new Thread(new ClientHandler(socket, world)).start();
            }

        } catch (IOException e) {
            System.out.println("Server stopped.");
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        World loadedWorld;
        try {
            loadedWorld = new World("config.properties");
            System.out.println("World loaded from config.properties");
        } catch (Exception e) {
            System.out.println("config.properties not found, using default 10x10 world.");
            loadedWorld = new World(10, 10);
        }
        new Server(loadedWorld).start();
    }

    static {
        new Recorder().logRun();
    }
}
