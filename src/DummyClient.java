import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class DummyClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1337);
            PrintWriter w = new PrintWriter(socket.getOutputStream());
            System.out.println("Sending data...");

            // Send MIDI data
            char[] s = new char[4];
            s[0] = 60;
            s[1] = 96;
            s[2] = 1;
            s[3] = 20;
            w.print(s);
            w.flush();

            // Send EOF
            char c = 0xFF;
            w.print(c);
            w.flush();

            // Close
            w.close();
            socket.close();
            System.out.println("Finished");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
