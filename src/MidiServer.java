import javax.sound.midi.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MidiServer {
    static int HANDSHAKE = 50; //this is a placeholder
    static int EOF = 0xFF; //more placeholders

    private Sequencer sequencer;
    private BufferedReader reader;
    private Sequence sequence;
    private Track track;
    private File file;

    private MidiServer(String filename) {
       file = new File(filename);
    }

    private void run() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, 4);
            ServerSocket server = new ServerSocket(1337);
            Socket socket = server.accept();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            track = sequence.createTrack();
            readIn();
            writeToFile();
            play();
        }
        catch(IOException | MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private void readIn() throws IOException, InvalidMidiDataException {
        int i = reader.read();
        System.out.println(i);
        while(i != HANDSHAKE) // block until we get the handshake from rust
            i = reader.read();
        System.out.println("We made it");

        char[] buff = new char[4];
        //add metadata to sequence
        while(reader.read(buff) != -1) {
            RustPacket pkt = RustPacket.convert(buff);
            convertToMsg(pkt);
        }
        System.out.println("Do we get here?");
    }

    private void writeToFile() throws IOException {
        MidiSystem.write(sequence, 1, file);
    }

    private void play() throws InvalidMidiDataException, MidiUnavailableException {
        sequencer.setSequence(sequence);
        sequencer.open();
        sequencer.start();
    }

    public static void main(String[] args) {
        new MidiServer("test.mid").run();
    }

    private void convertToMsg(RustPacket pkt) throws InvalidMidiDataException {
        ShortMessage msgOn = new ShortMessage();
        msgOn.setMessage(ShortMessage.NOTE_ON, pkt.pitch, pkt.velocity);
        MidiEvent evtOn = new MidiEvent(msgOn, pkt.startTime);
        track.add(evtOn);

        ShortMessage msgOff = new ShortMessage();
        msgOff.setMessage(ShortMessage.NOTE_OFF, pkt.pitch, pkt.velocity);
        MidiEvent evtOff = new MidiEvent(msgOff, pkt.startTime + pkt.duration);
        track.add(evtOff);
    }


}
