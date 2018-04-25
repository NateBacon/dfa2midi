import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class DummyClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1337);
            OutputStream w = socket.getOutputStream();
            System.out.println("Sending data...");

            // Send MIDI data
            byte[] s = new byte[4];
            s[0] = 60;
            s[1] = 96;
            s[2] = 1;
            s[3] = 20;
            w.write(s);
            w.flush();

            // Send EOF
            byte c = (byte) 0xFF;
            w.write(c);
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
