import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    public static final int portNumber = 40008;
    private static final HashMap<String, Socket> clientSockets = new HashMap<>();
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Server started. Listening on port " + portNumber);

        Thread commandThread = new Thread(() -> {
            Scanner input = new Scanner(System.in);
            while (true) {
                String cmd = input.next();
                if ("dc".equalsIgnoreCase(cmd)) {
                    try {
                        clientSockets.get(input.next()).close();
                    } catch (NullPointerException | IOException e) {
                        System.err.println("Client does not exist");
                    }
                } else if("msg".equalsIgnoreCase(cmd)){
                    try{
                        var outputStream = clientSockets.get(input.next()).getOutputStream();
                        var dataOutputStream = new DataOutputStream(outputStream);
                        dataOutputStream.writeUTF(input.nextLine());
                    } catch (NullPointerException | IOException e) {
                        System.err.println("Client does not exist");
                    }
                } else if("list".equalsIgnoreCase(cmd)){
                    if(clientSockets.keySet().size() == 0){
                        System.err.println("No clients currently connected");
                    }
                    for(String s : clientSockets.keySet()){
                        System.out.println(ANSI_CYAN + s + ANSI_RESET);
                    }
                } else{
                    System.err.println("Invalid command");
                }
            }
        });
        commandThread.setDaemon(true);
        commandThread.start();

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(ANSI_CYAN + "New client connected: " + socket.getInetAddress().getHostName() + ANSI_RESET);
            clientSockets.put(socket.getInetAddress().getHostName(), socket);
            Thread inputThread = new Thread(() -> {
                try {
                    var inputStream = socket.getInputStream();
                    var dataInputStream = new DataInputStream(inputStream);

                    while (true) {
                        String message = dataInputStream.readUTF();
                        System.out.println(ANSI_CYAN + "Received message from " + socket.getInetAddress().getHostName() + ": " + message + ANSI_RESET);
                        if ("dc".equalsIgnoreCase(message)) {
                            break;
                        }
                    }
                    socket.close();
                    System.err.println("Client disconnected: " + socket.getInetAddress().getHostName());
                    clientSockets.remove(socket.getInetAddress().getHostName());
                } catch (SocketTimeoutException e) {
                    System.err.println("Socket timed out, disconnecting " + socket.getInetAddress().getHostName());
                    clientSockets.remove(socket.getInetAddress().getHostName());
                } catch (SocketException | EOFException e) {
                    System.err.println("Client disconnected: " + socket.getInetAddress().getHostName());
                    clientSockets.remove(socket.getInetAddress().getHostName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            inputThread.start();
        }

    }
}
