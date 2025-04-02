package chatapp.client;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to server!");

            // Create input and output streams for communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Read and send messages
            BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            String message, response;
            while (true) {
                System.out.print("You: ");
                message = keyboardReader.readLine(); // Read message from keyboard
                writer.println(message); // Send message to server

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Chat ended by you.");
                    break;
                }

                response = reader.readLine(); // Read response from server
                System.out.println("Server: " + response); // Display server message

                if (response.equalsIgnoreCase("exit")) {
                    System.out.println("Chat ended by server.");
                    break;
                }
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
