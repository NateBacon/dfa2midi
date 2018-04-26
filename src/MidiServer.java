import javax.sound.midi.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MidiServer {
    private static byte EOF = (byte) 0xFF;

    private Sequencer sequencer;
    private DataInputStream reader;
    private Sequence sequence;
    private Track track;
    private File file;

    public static void main(String[] args) {
        new MidiServer("test.mid").run();
    }

    /**
     * Create a new MidiServer
     *
     * @param filename the filename to write the new midi data to
     */
    private MidiServer(String filename) {
       file = new File(filename);
    }

    /**
     * Start the server to listen for data and play it back
     */
    private void run() {
        try {
            // Setup the MIDI system
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, 4);

            // Start the TCP server
            ServerSocket server = new ServerSocket(1337);

            // Wait for a client
            System.out.println("Server started, waiting for client...");
            Socket socket = server.accept();
            System.out.println("Client connected.");

            // Setup the reader and track for data transfer
            reader = new DataInputStream(socket.getInputStream());
            track = sequence.createTrack();

            // Transfer data
            readIn();
            writeToFile();

            // Play the MIDI
            play();
        }
        catch(IOException | MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read in the MIDI data from the client
     *
     * @throws IOException If there was an error reading from the client
     * @throws InvalidMidiDataException If the client gives invalid MIDI data
     */
    private void readIn() throws IOException, InvalidMidiDataException {
        byte pitch, velocity, duration;
        long startTime;

        while(true) {
            pitch = reader.readByte();

            if(pitch == EOF)
                break;

            velocity = reader.readByte();
            duration = reader.readByte();
            startTime = reader.readLong();

            convertToMsg(new RustPacket(pitch, velocity, duration, startTime));
        }

        System.out.println("Client sent EOF");
    }

    /**
     * Write the MIDI data to the file
     *
     * @throws IOException If the write failed
     */
    private void writeToFile() throws IOException {
        MidiSystem.write(sequence, 1, file);
    }

    /**
     * Play the MIDI file
     *
     * @throws InvalidMidiDataException If the MIDI data is invalid
     * @throws MidiUnavailableException If the MIDI device could not be opened
     */
    private void play() throws InvalidMidiDataException, MidiUnavailableException {
        sequencer.setSequence(sequence);
        sequencer.open();
        sequencer.start();
    }

    /**
     * Convert a {@link RustPacket} to a MIDI message and add it to the track
     *
     * @param pkt the MIDI data packet
     * @throws InvalidMidiDataException If the MIDI data is invalid
     */
    private void convertToMsg(RustPacket pkt) throws InvalidMidiDataException {
        System.out.println("Adding note " + pkt.pitch + " at time " + pkt.startTime + " for duration " + pkt.duration);
        // Add the NOTE_ON message
        ShortMessage msgOn = new ShortMessage();
        msgOn.setMessage(ShortMessage.NOTE_ON, pkt.pitch, pkt.velocity);
        MidiEvent evtOn = new MidiEvent(msgOn, pkt.startTime);
        track.add(evtOn);

        // Add the NOTE_OFF message
        ShortMessage msgOff = new ShortMessage();
        msgOff.setMessage(ShortMessage.NOTE_OFF, pkt.pitch, pkt.velocity);
        MidiEvent evtOff = new MidiEvent(msgOff, pkt.startTime + pkt.duration);
        track.add(evtOff);
    }
}
