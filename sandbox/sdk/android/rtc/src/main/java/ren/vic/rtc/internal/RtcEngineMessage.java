package ren.vic.rtc.internal;

public class RtcEngineMessage {
    public static class PError extends Marshallable {
        public int err;

        public PError() {
        }

        public byte[] marshall() {
            return super.marshall();
        }

        public void unmarshall(byte[] buf) {
            super.unmarshall(buf);
            this.err = this.popInt();
        }
    }
}
