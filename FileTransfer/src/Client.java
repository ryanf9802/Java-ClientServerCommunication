import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final int portNumber = Server.portNumber;

    public static void main(String[]args) throws IOException {
        try{
            Socket socket = new Socket("192.168.1.160", 40008);
            Scanner scanner = new Scanner(System.in);

            Thread inputThread = new Thread( () -> {

            });
            Thread outputThread = new Thread( () -> {

            });

            inputThread.start();
            outputThread.start();

        } catch (ConnectException e){
            System.err.println("Connection could not be established");
        }
    }


}
