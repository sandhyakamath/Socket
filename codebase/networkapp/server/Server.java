package networkapp.server;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Create a server socket listening on port 5000
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is listening on port 5000...");

            // Accept incoming connections
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            // Set up input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Read messages from client and respond
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from client: " + message);
                writer.println("Server received: " + message); // Respond back
            }

            // Close resources
            reader.close();
            writer.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
