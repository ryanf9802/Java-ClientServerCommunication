import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    public static final int portNumber = 34523;
    private static final HashMap<String, Socket> clientSockets = new HashMap<>();
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[]args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Server started. Listening on port " + portNumber);

    }
}
