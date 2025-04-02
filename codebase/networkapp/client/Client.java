package networkapp.client;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Connect to server on localhost and port 5000
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to server!");

            // Set up input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Send messages to server
            writer.println("Hello, Server!");
            writer.println("How are you?");
            writer.println("Goodbye!");

            // Read responses from server
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println("Received from server: " + response);
            }

            // Close resources
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
