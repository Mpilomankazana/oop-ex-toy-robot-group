package za.co.wethinkcode.robots.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import za.co.wethinkcode.robots.command.CommandProcessor;
import za.co.wethinkcode.robots.protocol.Protocol;
import za.co.wethinkcode.robots.protocol.Request;
import za.co.wethinkcode.robots.protocol.StateData;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;

/**
 * Handles communication with a single connected robot client.
 * Runs in its own thread — one instance per connected robot.
 * Reads JSON commands from the client, processes them using the
 * GameEngine and World, and sends JSON responses back.
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final World world;
    private String robotName;
    private final CommandProcessor processor = new CommandProcessor();
    private final GameEngine engine = new GameEngine();

/**
 * Creates a new ClientHandler for the given socket and world.
 * @param socket the client's socket connection
 * @param world the shared world instance
 */
    public ClientHandler(Socket socket, World world) {
        this.socket = socket;
        this.world = world;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())); PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true)) {
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
                        Robot robot = engine.launch(robotName, world);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("No space in world"));
                            continue;
                        }

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
                    case "forward" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        int steps = Integer.parseInt(
                                request.getArguments()[0].toString());
                        int moved = engine.moveForward(robot, steps, world);
                        StateData state = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse(
                                "Moved " + moved + " step(s)", state));
                    }
                    case "back" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        int steps = Integer.parseInt(
                                request.getArguments()[0].toString());
                        int moved = engine.moveBack(robot, steps, world);
                        StateData state = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse(
                                "Moved back " + moved + " step(s)", state));
                    }
                    case "turn" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        String dir = request.getArguments()[0].toString();
                        Direction turned = engine.turn(robot, dir);
                        if (turned == null) {
                            out.println(Protocol.buildErrorResponse("Invalid direction: " + dir));
                            continue;
                        }
                        StateData state = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse("Turned " + dir, state));
                    }
                    case "look" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        java.util.List<java.util.Map<String, Object>> objects = engine.look(robot, world);
                        StateData state = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus());
                        out.println(Protocol.buildOkResponse(
                                java.util.Map.of("objects", objects), state));

                    }
                    case "fire" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        GameEngine.FireResult result = engine.fire(robot, world);
                        StateData state = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus());
                        out.println(Protocol.buildOkResponse(
                                java.util.Map.of(
                                        "outcome", result.outcome,
                                        "distance", result.distance,
                                        "shotLeft", result.shotleft),
                                state
                        ));
                    }

                    case "repair" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        robot.setStatus("REPAIR");
                        try {
                            Thread.sleep(world.getRepairTime() * 1000L);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        robot.setShields(world.getMaxShields());
                        robot.setStatus("NORMAL");
                        StateData state = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse("Shields repaired", state));
                    }
                    case "reload" -> {
                        Robot robot = world.getRobot(robotName);
                        if (robot == null) {
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        robot.setStatus("RELOAD");
                        StateData reloadingState = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse("Reloading...", reloadingState));
                        try {
                            Thread.sleep(world.getReloadTime() * 1000L);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        robot.setShots(world.getMaxShields());
                        robot.setStatus("NORMAL");
                        StateData doneState = new StateData(
                                new int[]{robot.getX(), robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus()
                        );
                        out.println(Protocol.buildOkResponse("Reloaded", doneState));
                    }
                    default -> {
                        out.println(Protocol.buildErrorResponse("Command not implemented: " + command));
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
            if (robotName != null) {
                world.removeRobot(robotName);
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
