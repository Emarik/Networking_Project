import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ObjectServer {

    public static void main(String[] argv) throws Exception {
        @SuppressWarnings("resource")
		ServerSocket s = new ServerSocket(8788);
        System.out.println("Server started");
        while (true) {
            Socket t = s.accept();// wait for client to connect
            System.out.println("server connected");
            ObjectInputStream b = new ObjectInputStream(t.getInputStream());
            Reserve received = (Reserve) b.readObject();
            PrintWriter output = new PrintWriter(t.getOutputStream(), true);
            output.println("Machine: " + received.getMachine()
            + " Time: " + received.getTime()  + " Date: " + received.getDate());
            
            b.close();
            output.close();
            t.close();
        }
    }
}