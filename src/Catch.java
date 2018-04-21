
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


import javax.sound.midi.*;

public class Catch {
	static int EOS = 44; //this is a placeholder
	static int EOP = 45; //more placeholders
	
	Sequencer seq;
	ServerSocket server;
	Socket s;
	BufferedReader r;
	
	public Catch() {
		try {
			seq = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void readIn() {
		
	}
	
	public static void main(String[] args) {
        
	}
	
	
	
	
	
	public MidiEvent convertToMsg(RustPacket pkt) {
		ShortMessage msg = new ShortMessage();
		//edit message
		MidiEvent evt = new MidiEvent(msg, pkt.startTime);
		return evt;
	}
	

}
