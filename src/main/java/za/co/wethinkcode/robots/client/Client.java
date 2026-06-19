package za.co.wethinkcode.robots.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;
//adding two imports to store multiple robots names
import java.util.ArrayList;
import java.util.List;


public class Client {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Usage: java client <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket socket = new Socket(host, port);
        System.out.println("Connected to server");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        //launching more than one robot
        System.out.println("How many robots do you want to launch ? ");
        int numRobots = Integer.parseInt(scanner.nextLine());

        //store all robot names
        List<String> robotNames = new ArrayList<>();

        //tracking robots that are dead
        List<String> deadRobots = new ArrayList<>();

        //Launch each robot one by one
        for (int i = 0; i < numRobots; i++) {
            System.out.print("Enter robot name" + (i + 1) + ": ");
            String robotName = scanner.nextLine();
            robotNames.add(robotName);

            String launchJson = "{\"robot\":\"" + robotName + "\",\"command\":\"launch\",\"arguments\":[\"sniper\",5,5]}";
            out.println(launchJson);
            String launchResponse = in.readLine();
            displayLaunchResponse(launchResponse);
        }


        while (true) {

            //ask which robot to control
            System.out.print("which robot do you want to control ?(" + String.join("/", robotNames) + "): ");
            String robotName = scanner.nextLine();
            //
            if (!robotNames.contains(robotName)) {
                System.out.println("Unknown robot. choose from: " + robotNames);
                continue;
            }

            //preventing dead robots from sending commands
            if (deadRobots.contains(robotName)) {
                System.out.println(robotName + " is DEAD and cannot send commands");
                continue;
            }

            System.out.print(robotName + ">");
            String input = scanner.nextLine();


            if (input.trim().isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye");
                break;
            }

            String[] parts = input.trim().split("\\s+");
            String command = parts[0].toLowerCase();

            String json;

            switch (command) {
                case "forward":
                case "back":

                    if (parts.length < 2) {
                        System.out.println("Usage: " + command + " <steps>");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Steps must be a number");
                        continue;
                    }

                    json = "{\"robot\":\"" + robotName +
                            "\",\"command\":\"" + command +
                            "\",\"arguments\":[" + parts[1] + "]}";
                    break;

                case "turn":
                    if (parts.length < 2) {
                        System.out.println("Usage: turn left/right");
                        continue;
                    }

                    json = "{\"robot\":\"" + robotName +
                            "\",\"command\":\"turn\"" +
                            ",\"arguments\":[\"" + parts[1] + "\"]}";

                    break;
                case "fire":
                case "look":
                case "state":
                case "repair":
                case "reload":

                    json = "{\"robot\":\"" + robotName +
                            "\",\"command\":\"" + command +
                            "\",\"arguments\":[]}";
                    break;

                default:
                    System.out.println("Unknown command");
                    continue;

            }
            out.println(json);


            String response = in.readLine();

            if (input.equalsIgnoreCase("look")) {

                displayLookResponse(response);
            } else if (input.equalsIgnoreCase("state")) {
                displayStateResponse(response);

            } else if (command.equals("forward") || command.equals("back")) {
                displayMovementResponse(response);

            } else if (command.equals("turn")) {
                displayTurnResponse(response);

            } else if (command.equals("fire")) {
                displayFireResponse(response);

            } else if (command.equals("repair")) {
                displayRepairResponse(response);

            } else if (command.equals("reload")) {
                displayReloadResponse(response);

            } else {
                System.out.println(response);
            }

            try {
                JsonNode node = mapper.readTree(response);
                JsonNode state = node.get("state");
                if (state != null && state.get("status").asText().equals("DEAD")) {
                    if (!deadRobots.contains(robotName)) {
                        deadRobots.add(robotName);
                    }
                }
            } catch (Exception e) {

            }
        }


        socket.close();
    }


    private static void displayLaunchResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode state = node.get("state");
            if (state != null) {
                System.out.println("Robot launched successfully!");
                System.out.println("Position: " + state.get("position"));
                System.out.println("Direction:" + state.get("direction").asText());
            } else {
                System.out.println("Launch response: " + response);
            }
        } catch (Exception e) {
            System.out.println("Launch response: " + response);

        }
    }

    private static void displayLookResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode objects = node.get("data").get("objects");
            if (objects != null) {
                System.out.println("You see");
                boolean sawSomething = false;
                for (JsonNode obj : objects) {
                    if (!obj.get("type").asText().equals("NONE")) {

                        System.out.println(" " +
                                obj.get("direction").asText() + ": " +
                                obj.get("type").asText() + " at distance " +
                                obj.get("distance").asInt()
                        );
                        sawSomething = true;
                    }
                }
                if (!sawSomething) {
                    System.out.println("Nothing is visible ");
                }
            }
        } catch (Exception e) {
            System.out.println("unexpected response from java");
        }
    }


    private static void displayStateResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode state = node.get("state");
            if (state != null) {
                System.out.println("Robot state:");
                System.out.println("  Position:  " + state.get("position"));
                System.out.println("  Direction: " + state.get("direction").asText());
                System.out.println("  Shields:   " + state.get("shields").asInt());
                System.out.println("  Shots:     " + state.get("shots").asInt());

                String status = state.get("status").asText();
                System.out.println(" Status: " + status);
                if (status.equals("DEAD")) {
                    System.out.println("Your robot is DEAD!");
                }
            } else {
                System.out.println(response);
            }
        } catch (Exception e) {
            System.out.println(response);
        }
    }

    private static void displayMovementResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode state = node.get("state");
            String message = node.get("data").asText();
            if (state != null) {
                System.out.println(message);
                System.out.println(" Position: " + state.get("position"));
                System.out.println(" Direction: " + state.get("direction").asText());
                // CHECK IF THE ROBOT IS DEAD
                String status = state.get("status").asText();
                if (status.equals("DEAD")) {
                    System.out.println("Your robot is DEAD");
                }
            } else {
                System.out.println(response);
            }
        } catch (Exception e) {
            System.out.println(response);
        }
    }

    private static void displayTurnResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode state = node.get("state");
            if (state != null) {
                System.out.println("Turned. Now facing: " +
                        state.get("direction").asText());
            } else {
                System.out.println(response);
            }
        } catch (Exception e) {
            System.out.println(response);
        }
    }

    private static void displayFireResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode data = node.get("data");
            JsonNode state = node.get("state");
            if (data != null) {
                String outcome = data.get("outcome").asText();
                int shotsLeft = state.get("shots").asInt();
                // check Kill before HIt
                if (outcome.equals("KILL")) {
                    System.out.println("Target robot destroyed!");
                } else if (outcome.equals("HIT")) {
                    System.out.println("HIT! Target struck at distance " +
                            data.get("distance").asInt());
                } else {
                    System.out.println("Miss, no robot in range.");
                }
                System.out.println(" shots remaining: " + shotsLeft);
                //check if robot is DEAD after firing
                String status = state.get("status").asText();
                if (status.equals("DEAD")) {
                    System.out.println("Your robot is DEAD");
                }
            } else {
                System.out.println(response);
            }
        } catch (Exception e) {
            System.out.println(response);
        }
    }

    private static void displayRepairResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode state = node.get("state");
            if (state != null) {
                System.out.println("Robot repaired");
                System.out.println(" Shields: " + state.get("shields").asInt());
                // check if robot is DEAD after repair
                String status= state.get("status").asText();
                if (status.equals("DEAD")) {
                    System.out.println("Your robot is DEAD");
                }
            } else {
                System.out.println(response);
            }
        } catch (Exception e) {
            System.out.println(response);
        }
    }

    private static void displayReloadResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode state = node.get("state");
            if (state != null) {
                System.out.println("Robot reloaded!");
                System.out.println(" shots: " + state.get("shots").asInt());

            } else {
                System.out.println(response);
            }
        } catch (Exception e) {
            System.out.println(response);
        }
    }
}






















