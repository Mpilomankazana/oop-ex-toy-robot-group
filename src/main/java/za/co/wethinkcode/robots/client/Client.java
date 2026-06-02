package za.co.wethinkcode.robots.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocol.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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

        System.out.print("Enter robot name: ");
        String robotName = scanner.nextLine();

        String launchJson = "{\"robot\":\"" + robotName + "\",\"command\":\"launch\",\"arguments\":[\"sniper\",5,5]}";
        out.println(launchJson);
        String launchResponse = in.readLine();

        displayLaunchResponse(launchResponse);

        while (true) {
            System.out.println(robotName + "> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye");
                break;
            }

            String json = "{\"robot\":\"" + robotName + "\",\"command\":\"" + input + "\",\"arguments\":[]}";
            out.println(json);

            String response = in.readLine();

            if (input.equalsIgnoreCase("look")) {

                displayLookResponse(response);
            }
             else if (input.equalsIgnoreCase("state")) {
                displayStateResponse(response);
            } else {

                System.out.println("Server: " + response);
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
        }catch (Exception e) {
            System.out.println("Launch response: " + response);

            }
        }

        private static void displayLookResponse(String response) {
        try {
            JsonNode node = mapper.readTree(response);
            JsonNode objects = node.get("data").get("objects");
            if (objects != null) {
                System.out.println("You see");
                for (JsonNode obj : objects) {
                        System.out.println(" " +
                                obj.get("direction").asText() + ": " +
                                obj.get("type").asText() + " at distance " +
                                obj.get("distance").asInt()
                        );
                    }
                } else {
                    System.out.println("Nothing is visible");
                }
            }catch(Exception e ){
                System.out.println(response);
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
                        System.out.println("  Status:    " + state.get("status").asText());
                    } else {
                        System.out.println(response);
                    }
                } catch (Exception e) {
                    System.out.println(response);
                }
            }
        }














