package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.robots.protocol.Protocol;
import za.co.wethinkcode.robots.protocol.Request;
import za.co.wethinkcode.robots.protocol.StateData;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;
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

                Request request = Protocol.parseRequest(line);
                if (request == null) {
                    out.println(Protocol.buildErrorResponse("Invalid JSON"));
                    continue;
                }

                robotName = request.getRobot();
                String command = request.getCommand();

                switch (command.toLowerCase()) {
                    case "launch" -> {
                        Robot robot = new Robot(robotName);
                        world.addRobot(robot);
                        StateData state = new StateData(
                            new int[]{robot.getX(), robot.getY()},
                            robot.getDirection().name(),
                            robot.getShields(),
                            robot.getShots(),
                            robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse("Launched", state));
                    }
                    case "state" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        StateData state = new StateData(
                            new int[]{robot.getX(), robot.getY()},
                            robot.getDirection().name(),
                            robot.getShields(),
                            robot.getShots(),
                            robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse("State", state));
                    }
                    default -> {
                        out.println(Protocol.buildErrorResponse("Command not implemented yet: " + command));
                    }
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