package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;
import za.co.wethinkcode.robots.protocol.Protocol;
import za.co.wethinkcode.robots.protocol.Request;
import za.co.wethinkcode.robots.command.CommandProcessor;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final World world;
    private String robotName;
    private final CommandProcessor processor = new CommandProcessor();

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

                // Parse the incoming JSON request
                Request request = Protocol.parseRequest(line);

                if (request == null) {
                    out.println(Protocol.buildErrorResponse("Could not parse arguments"));
                    continue;
                }

                // Store robot name on launch
                if ("launch".equals(request.getCommand())) {
                    robotName = request.getRobot();
                }

                // Process the command
                String command = request.getCommand();
                String result = processor.execute(command);

                if (result.equals("ERROR")) {
                    out.println(Protocol.buildErrorResponse("Unsupported command"));
                } else {
                    out.println(Protocol.buildOkResponse(
                        java.util.Map.of("message", "Done"), null));
                }

                // Check if robot is dead after each command
                if (robotName != null) {
                    Robot robot = world.getRobot(robotName);
                    if (robot != null && "DEAD".equals(robot.getStatus())) {
                        System.out.println(robotName + " is dead. Closing connection.");
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } finally {
            if (robotName != null) world.removeRobot(robotName);
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}