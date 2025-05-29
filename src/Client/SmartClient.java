package Client;
import java.io.*;            // For reading and writing data
import java.net.*;           // For network communication (sockets)
import java.awt.*;           // For layout stuff
import javax.swing.*;        // For GUI components like buttons and text boxes

// This class represents the client-side of a smart network application
public class SmartClient {

    // These are used to connect and communicate with the server
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    // GUI components to show messages and take input
    private JTextArea display;
    private JTextField inputField;
    private JButton connectBtn, sendBtn;
    // This keeps track of whether the client is connected to the server or not
    private boolean connected = false;
    // Constructor: sets up the GUI and event listeners
    public SmartClient() {
        JFrame frame = new JFrame("Smart Client");
        // Create the text area to show server responses
        display = new JTextArea(15, 40);
        display.setEditable(false); // So user can't change it
        // Field where the user will type messages
        inputField = new JTextField();
        // Buttons to connect and send messages
        connectBtn = new JButton("Connect");
        sendBtn = new JButton("Send");
        // Layout management
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendBtn, BorderLayout.EAST);
        // Adding components to the frame
        frame.add(new JScrollPane(display), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.add(connectBtn, BorderLayout.NORTH);

        // Set up the window
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // When buttons are clicked, call the appropriate functions
        connectBtn.addActionListener(e -> connectToServer());
        sendBtn.addActionListener(e -> sendRequest());
    }

    // This function connects the client to the server
    public void connectToServer() {
        if (connected) {
            log("Already connected.");
            return;
        }
        try {
            // Try to connect to the server at localhost and port 9999
            socket = new Socket("127.0.0.1", 9999);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            connected = true;
            log("Connected to server.");

            // Start a new thread to listen for messages from server
            new Thread(this::listen).start();
        } catch (IOException e) {
            log("Connection failed: " + e.getMessage());
        }
    }
    // This function sends the message typed by the user to the server
    public void sendRequest() {
        if (!connected) {
            log("Not connected.");
            return;
        }
        try {
            String msg = inputField.getText(); // Get the message
            out.writeUTF(msg);                // Send it to the server
            inputField.setText("");           // Clear the input field
        } catch (IOException e) {
            log("Send failed: " + e.getMessage());
        }
    }
    // This function keeps listening for messages from the server
    public void listen() {
        try {
            while (true) {
                String response = in.readUTF(); // Read server message
                log("Server: " + response);     // Show it in display area
            }
        } catch (IOException e) {
            log("Disconnected from server.");  // If error happens, log it
            connected = false;
        }
    }
    // Helper function to add a message to the display area
    public void log(String msg) {
        display.append(msg + "\n");
    }
    // Main function to start the client
    public static void main(String[] args) {
        new SmartClient();
    }
}