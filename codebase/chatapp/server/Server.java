package chatapp.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

// Class for handling each client connection in a separate thread
class ClientHandler extends Thread {
    private Socket clientSocket;
    private int clientId;
    private PrintWriter writer;
    private static ConcurrentHashMap<Integer, ClientHandler> activeClients = new ConcurrentHashMap<>();

    public ClientHandler(Socket socket, int id) {
        this.clientSocket = socket;
        this.clientId = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("Connected to client: " + clientSocket.getInetAddress().getHostAddress());

            // Set up input and output streams for communication with the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            activeClients.put(clientId, this);

            // Read and send messages
            BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            String message, response;
            while (true) {
                message = reader.readLine(); // Read message from client
                System.out.println("Client " + clientId + ": " +message); // Display client message

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client " + clientId + " disconnected.");
                    break;
                }

                System.out.print("You "+ clientId + ": ");
                response = keyboardReader.readLine(); // Read response from keyboard

                for (ClientHandler client : activeClients.values()) {
                    if (client.clientId == this.clientId) {
                        client.writer.println(response); // Send response to client
                    }
                }

                if (response.equalsIgnoreCase("exit")) {
                    System.out.println("Client " + clientId + " disconnected.");
                    break;
                }
            }
            // Remove this client from the active clients map
            activeClients.remove(clientId);
            // Close resources
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error in client communication: " + e.getMessage());
        }
    }
}
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is waiting for client...");
            int id = 1;

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept a client connection
                System.out.println("A new client has connected!");

                // Create and start a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, id);
                clientHandler.start();
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
