import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class DummyClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1337);
            PrintWriter w = new PrintWriter(socket.getOutputStream());
            System.out.println("Printing 50...");
            char c = 50;
            w.print(c);
            w.flush();
            char[] s = new char[4];
            s[0] = 60;
            s[1] = 96;
            s[2] = 1;
            s[3] = 20;
            w.print(s);
            w.flush();
            w.close();
            socket.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
