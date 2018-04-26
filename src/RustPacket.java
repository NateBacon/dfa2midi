class RustPacket {
    int pitch;
    int velocity;
    int duration;
    long startTime;

    RustPacket(int pitch, int velocity, int duration, long startTime) {
        this.pitch = pitch;
        this.velocity = velocity;
        this.duration = duration;
        this.startTime = startTime;
    }
}
