public class RustPacket {
    int pitch;
    int velocity;
    int duration;
    long startTime;

    static int PITCH_MULT = 1;
    static int VELOCITY_MULT = 1;
    static int TIME_MULT = 1;

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
        pkt.pitch = buffer[0] * PITCH_MULT;
        pkt.velocity = buffer[1] * VELOCITY_MULT;
        pkt.duration = buffer[2] * TIME_MULT;
        pkt.startTime = buffer[3] * TIME_MULT;

        return pkt;
    }
}
