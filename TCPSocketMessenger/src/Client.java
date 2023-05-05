import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * This class can be replicated on any device on the same network and will communicate with the server
 */
public class Client {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static void main(String[]args) throws IOException {
        try{
            Socket socket = new Socket("192.168.1.160", 40008);
            Scanner scanner = new Scanner(System.in);

            Thread inputThread = new Thread( () -> {
                try {
                    var inputStream = socket.getInputStream();
                    var dataInputStream = new DataInputStream(inputStream);

                    while(true){
                        String msg = dataInputStream.readUTF();
                        System.out.println(ANSI_CYAN + "\nReceived message from server: " +msg + ANSI_RESET);
                    }
                } catch (SocketException | EOFException e){
                    System.err.println("Disconnected from server.");
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            Thread outputThread = new Thread( () -> {
                try {
                    var outputStream = socket.getOutputStream();
                    var dataOutputStream = new DataOutputStream(outputStream);

                    while(true){
                        System.out.print("Enter message to send to server (or 'dc' to exit): ");
                        String msg = scanner.nextLine();

                        if("dc".equalsIgnoreCase(msg)){
                            break;
                        }
                        dataOutputStream.writeUTF(msg);
                    }
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            inputThread.start();
            outputThread.start();
        } catch (ConnectException e){
            System.err.println("Connection could not be established");
        }
    }
}
