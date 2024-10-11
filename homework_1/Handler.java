package org.example;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Handler implements Runnable {
    private Socket clientSocket;
    private static final String FILE_NAME = "students.txt";

    public Handler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                if (request.startsWith("ADD")) {
                    String[] parts = request.split(" ");
                    String name = parts[1];
                    double grade = Double.parseDouble(parts[2]);
                    addStudent(new Student(name, grade));
                    out.println("Student added successfully.");
                } else if (request.equals("VIEW")) {
                    String studentData = viewAllGrades();
                    out.println(studentData);
                } else if (request.equals("AVERAGE")) {
                    double average = calculateAverage();
                    out.println("Average grade: " + average);
                } else {
                    out.println("Invalid command.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addStudent(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(student.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized String viewAllGrades() {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private synchronized double calculateAverage() {
        double total = 0;
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                double grade = Double.parseDouble(parts[1]);
                total += grade;
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count > 0 ? total / count : 0;
    }
}

