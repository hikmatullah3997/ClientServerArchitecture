package serverapp;
import java.io.*;//for input and output operations
import java.net.*; // for connection
import java.text.SimpleDateFormat;// For date formate
import java.util.*;// for basic data structures
import java.util.concurrent.*;// for muntiple connection with client
import javax.swing.*; // for GUI components

public class SmartServer {
    // The server will listen on this port
    private static final int PORT = 9999;
    // A thread-safe set to keep track of all connected clients
    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    // This area displays logs in the GUI
    private static JTextArea logArea;
    public static void main(String[] args) throws IOException {
        // Setup the server GUI window
        JFrame frame = setupGUI();

        // Create a server socket to accept client connections
        ServerSocket serverSocket = new ServerSocket(PORT);
        log("Server started on port " + PORT);

        // Ensure server closes cleanly on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log("Shutting down server...");
            try {
                for (ClientHandler client : clients) client.close();
                serverSocket.close();
            } catch (IOException e) {
                log("Error during shutdown: " + e.getMessage());
            }
        }));
        // Main server loop to accept incoming clients
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler); // Track client
              
                new Thread(clientHandler).start(); // Handle client in a new thread
                log("Client connected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                log("Accept failed: " + e.getMessage());
            }
        }
    }


    // Setup and return the GUI frame
    public static JFrame setupGUI() {
        JFrame frame = new JFrame("Smart Server");
        logArea = new JTextArea(20, 40);
        logArea.setEditable(false);
        frame.add(new JScrollPane(logArea));
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    // Log messages with timestamps to the GUI
    public static void log(String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        logArea.append("[" + time + "] " + message + "\n");
    }

    // Class to handle each connected client
    static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        }




        // Run method that keeps listening for client messages
        public void run() {
            try {
                while (true) {
                    String request = in.readUTF();
                    log("Received from " + socket.getInetAddress() + ": " + request);
                    String response = processRequest(request);
                    out.writeUTF(response);
                }
            } catch (IOException e) {
                log("Client disconnected: " + socket.getInetAddress());
            } finally {
                close();
                clients.remove(this);
              
            }
        }



        // Handle different types of requests from the client
        public String processRequest(String request) {
            try {
                if (request.startsWith("GET:TIME")) {
                    return "TIME:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                } else if (request.startsWith("GET:DATE")) {
                    return "DATE:" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                } else if (request.startsWith("GET:TIMESTAMP")) {
                    return "TIMESTAMP:" + System.currentTimeMillis();

                } else if (request.startsWith("GET:FILE:")) {
                    String filename = request.substring("GET:FILE:".length()).trim();
                    File file = new File(filename);
                    if (!file.exists()) return "ERROR:File not found";
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) content.append(line).append("\n");
                    reader.close();
                    return "FILE_CONTENT:\n" + content.toString();

                } else if (request.equals("GET:FILE_LIST")) {
                    File dir = new File(".");
                    String[] files = dir.list();
                    return "FILES:" + String.join(",", files);

                } else if (request.startsWith("UPLOAD:FILE:")) {
                    String[] parts = request.split(":", 4);
                    if (parts.length < 4) return "ERROR:Invalid upload format";
                    String fileName = parts[2];
                    String content = parts[3];
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    writer.write(content);
                    writer.close();
                    return "UPLOAD:SUCCESS";

                } else if (request.startsWith("DELETE:FILE:")) {
                    String fileName = request.substring("DELETE:FILE:".length()).trim();
                    File file = new File(fileName);
                    if (file.exists() && file.delete()) return "DELETE:SUCCESS";
                    else return "ERROR:Delete failed";

                } else if (request.equals("GET:CLIENTS")) {
                    return "CLIENTS:" + clients.size();

                }  else if (request.equals("COMMAND:SAY_HELLO")) {
                    return "RESPONSE:Hello Client!";

                } else if (request.equals("COMMAND:SHUTDOWN_SERVER")) {
                    new Thread(() -> System.exit(0)).start();
                    return "SERVER:Shutting down...";

                } else if (request.equals("COMMAND:LOGOUT")) {
                    close(); // disconnects the client
                    return "LOGOUT:SUCCESS";

                } else if (request.startsWith("COMMAND:ECHO:")) {
                    return "ECHO:" + request.substring("COMMAND:ECHO:".length());

                } else if (request.startsWith("COMMAND:TIME_DIFF:")) {
                    String inputDate = request.substring("COMMAND:TIME_DIFF:".length()).trim();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(inputDate);
                    long diffMillis = new Date().getTime() - date.getTime();
                    long days = diffMillis / (1000 * 60 * 60 * 24);
                    return "TIME_DIFF:" + days + " days";

                } else {
                    return "ERROR:Unknown request";
                }
            } catch (Exception e) {
                return "ERROR:" + e.getMessage();
            }
        }

        // Close all connections for the client
        void close() {
            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException ignored) {}
        }
    }
}