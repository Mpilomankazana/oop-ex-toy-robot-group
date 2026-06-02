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
                //out.println("{\"result\":\"OK\",\"data\":{\"message\":\"received\"}}");
                Request request = Protocol.parseRequest(line);
                if (request == null){
                    out.println(Protocol.buildErrorResponse("Invalid JSON"));
                    continue;
                }
                String robotName = request.getRobot();
                String command = request.getCommand();

//                if (processor.execute(command).equals("ERROR")){
//                    out.println(Protocol.buildErrorResponse("Invalid command: " + command));
//                    continue;
//                }

                switch (command.toLowerCase()){
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
                    }
                    case  "state" -> {
                        Robot robot = world.getRobot(robotName);

                        if (robot == null){
                            out.println(Protocol.buildErrorResponse("Robot not found"));
                            continue;
                        }
                        StateData state = new StateData(
                                new int[]{robot.getX(),robot.getY()},
                                robot.getDirection().name(),
                                robot.getShields(),
                                robot.getShots(),
                                robot.getStatus()

                        );
                        out.println(Protocol.buildOkResponse("State", state));
                    }
                    default -> {
                        out.println(Protocol.buildErrorResponse("Command not implemented yet" + command));
                    }
                }
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