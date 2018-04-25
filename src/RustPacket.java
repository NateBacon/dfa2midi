public class RustPacket {
    int pitch;
    int velocity;
    int duration;
    long startTime;

    public RustPacket(int pitch, int velocity, int duration, long startTime) {
        this.pitch = pitch;
        this.velocity = velocity;
        this.duration = duration;
        this.startTime = startTime;
    }

    public RustPacket() {
        this.pitch = 0;
        this.velocity = 0;
        this.duration = 0;
        this.startTime = 0;
    }

    public static RustPacket convert(char[] buffer) {
        RustPacket pkt = new RustPacket();
        pkt.pitch = buffer[0];
        pkt.velocity = buffer[1];
        pkt.duration = buffer[2];
        pkt.startTime = buffer[3];

        return pkt;
    }
}
