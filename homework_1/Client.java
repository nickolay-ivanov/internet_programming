package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8089;

        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            while (true) {
                System.out.println("Enter a command (ADD, VIEW, AVERAGE) or EXIT to quit:");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("EXIT")) {
                    System.out.println("Closing connection.");
                    break;
                }

                if (command.startsWith("ADD")) {
                    System.out.println("Enter the student's name:");
                    String name = scanner.nextLine();

                    System.out.println("Enter the student's grade:");
                    String gradeInput = scanner.nextLine();

                    try {
                        double grade = Double.parseDouble(gradeInput);
                        out.println("ADD " + name + " " + grade);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid grade format.");
                        continue;
                    }
                } else if (command.equalsIgnoreCase("VIEW")) {
                    out.println("VIEW");
                } else if (command.equalsIgnoreCase("AVERAGE")) {
                    out.println("AVERAGE");
                } else {
                    System.out.println("Invalid command.");
                    continue;
                }

                String response = in.readLine();
                if (response != null) {
                    System.out.println("Server response: " + response);
                } else {
                    System.out.println("No response from the server.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

