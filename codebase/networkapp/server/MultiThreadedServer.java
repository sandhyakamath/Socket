import java.io.*;
import java.net.*;

// Class for handling each client connection in a separate thread
class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Connected to client: " + clientSocket.getInetAddress().getHostAddress());

            // Set up input and output streams for communication with the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from client: " + message);
                writer.println("Server received: " + message); // Send a response
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnected.");
                    break;
                }
            }

            // Close resources for this client
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error in client communication: " + e.getMessage());
        }
    }
}

// Main server class
public class MultiThreadedServer {
    public static void main(String[] args) {
        try {
            // Create a server socket that listens on port 5000
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is listening on port 5000...");

            // Continuously accept and handle client connections
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept a client connection
                System.out.println("A new client has connected!");

                // Create and start a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}

