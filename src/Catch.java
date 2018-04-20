
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
	
	public static void main(String[] args) {

		try { 
			ServerSocket server = new ServerSocket(1337);
			Socket s = server.accept();
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			//integer by integer is just a placeholder, eventually figure out number of bytes in a packet
			int c;
			while((c = r.read()) != EOS) {
				for(int i = 0; i < 4; i++) {
					switch(i) {
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					}
				}
			}
			
		} catch(IOException e) {
			
		}
	}
	
	
	
	
	
	
	public MidiEvent convertToMsg(RustPacket pkt) {
		MidiEvent ret = new MidiEvent(new ShortMessage(), 0); //change this
		return ret;
	}
	

}
