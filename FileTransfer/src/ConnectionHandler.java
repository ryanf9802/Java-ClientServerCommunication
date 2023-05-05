import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable{

    private Socket socket;

    public ConnectionHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            try{
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
