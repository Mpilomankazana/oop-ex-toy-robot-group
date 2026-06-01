package za.co.wethinkcode.robots.client;

import za.co.wethinkcode.robots.protocol.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

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
        System.out.println("Launch response: " + launchResponse);

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
            int start = response.indexOf("\"message\":\"") + 11;
            int end = response.indexOf("\"", start);
            String message = response.substring(start, end);
            System.out.println();
            System.out.print("Server: " + message);

        }

        socket.close();

    }

}
