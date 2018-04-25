class RustPacket {
    int pitch;
    int velocity;
    int duration;
    long startTime;

    private RustPacket(int pitch, int velocity, int duration, long startTime) {
        this.pitch = pitch;
        this.velocity = velocity;
        this.duration = duration;
        this.startTime = startTime;
    }

    /**
     * Create a {@link RustPacket} from raw data from the client
     *
     * @param buffer the raw data
     * @return the parsed {@link RustPacket}
     */
    static RustPacket convert(byte[] buffer) {
        return new RustPacket(buffer[0], buffer[1], buffer[2], buffer[3]);
    }
}
