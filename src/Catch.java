
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


import javax.sound.midi.*;

public class Catch {
	static int HANDSHAKE = 50; //this is a placeholder
	static int EOF = 0xFF; //more placeholders
	
	Sequencer seq;
	ServerSocket server;
	Socket s;
	BufferedReader r;
	Sequence se;
	Track t;
	File f = new File("test.mid");
	
	
	public Catch() {
		try {
			seq = MidiSystem.getSequencer();
			se = new Sequence(Sequence.PPQ, 4);
			server = new ServerSocket(1337);
			s = server.accept();
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			t = se.createTrack();
			readIn();
			writeToFile();
			play();
		} catch (IOException | MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void readIn() throws IOException, InvalidMidiDataException {
		
		int i = r.read();
		System.out.println(i);
		while(i != HANDSHAKE) { // block until we get the handshake from rust
			i = r.read();
			continue;
		}
		System.out.println("We made it");
		
		char[] buff = new char[4];
		//add metadata to sequence
		while(r.read(buff) != -1) {
			RustPacket pkt = RustPacket.convert(buff);
			convertToMsg(pkt);
		} 
		System.out.println("Do we get here?");
	}
	
	public void writeToFile() throws InvalidMidiDataException, IOException {
		MidiSystem.write(se, 1, f);
	}
	
	public void play() throws InvalidMidiDataException, MidiUnavailableException {
		seq.setSequence(se);
		seq.open();
		seq.start();
	}
	
	public static void main(String[] args) {
        new Catch();
	}
	
	public void convertToMsg(RustPacket pkt) throws InvalidMidiDataException {
		ShortMessage msgOn = new ShortMessage();
		msgOn.setMessage(ShortMessage.NOTE_ON, pkt.pitch, pkt.velocity);
		MidiEvent evtOn = new MidiEvent(msgOn, pkt.startTime);
		t.add(evtOn);
		
		ShortMessage msgOff = new ShortMessage();
		msgOff.setMessage(ShortMessage.NOTE_OFF, pkt.pitch, pkt.velocity);
		MidiEvent evtOff = new MidiEvent(msgOff, pkt.startTime + pkt.duration);
		t.add(evtOff);
	}
	

}
