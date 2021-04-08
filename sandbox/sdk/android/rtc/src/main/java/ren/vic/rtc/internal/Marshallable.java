package ren.vic.rtc.internal;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

class Marshallable {
    public static final int PROTO_PACKET_SIZE = 8192;
    private ByteBuffer mBuffer = ByteBuffer.allocate(8192);

    public Marshallable() {
        this.mBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.mBuffer.position(2);
    }

    public byte[] marshall() {
        short len = (short)this.mBuffer.position();
        this.mBuffer.putShort(0, len);
        byte[] data = new byte[len];
        this.mBuffer.position(0);
        this.mBuffer.get(data);
        return data;
    }

    public void marshall(ByteBuffer buf) {
        this.mBuffer = buf;
    }

    public void unmarshall(byte[] buf) {
        this.mBuffer = ByteBuffer.wrap(buf);
        this.mBuffer.order(ByteOrder.LITTLE_ENDIAN);
        short len = this.popShort();
    }

    public void unmarshall(ByteBuffer buf) {
        this.mBuffer = buf;
    }

    public ByteBuffer getBuffer() {
        return this.mBuffer;
    }

    public void pushBool(Boolean val) {
        byte b = 0;
        if (val) {
            b = 1;
        }

        this.mBuffer.put(b);
    }

    public Boolean popBool() {
        byte b = this.mBuffer.get();
        return b == 1;
    }

    public void pushByte(byte val) {
        this.mBuffer.put(val);
    }

    public byte popByte() {
        return this.mBuffer.get();
    }

    public void pushBytes(byte[] buf) {
        this.mBuffer.putShort((short)buf.length);
        this.mBuffer.put(buf);
    }

    public byte[] popBytes() {
        int len = this.mBuffer.getShort();
        byte[] buf = new byte[0];
        if (len > 0) {
            buf = new byte[len];
            this.mBuffer.get(buf);
        }

        return buf;
    }

    public void pushBytes32(byte[] buf) {
        this.mBuffer.putInt(buf.length);
        this.mBuffer.put(buf);
    }

    public byte[] popBytes32() {
        int len = this.mBuffer.getInt();
        byte[] buf = null;
        if (len > 0) {
            buf = new byte[len];
            this.mBuffer.get(buf);
        }

        return buf;
    }

    public byte[] popAll() {
        int len = this.mBuffer.remaining();
        byte[] buf = new byte[len];
        this.mBuffer.get(buf);
        return buf;
    }

    public void pushShort(short val) {
        this.mBuffer.putShort(val);
    }

    public short popShort() {
        return this.mBuffer.getShort();
    }

    public void pushInt(int val) {
        this.mBuffer.putInt(val);
    }

    public void pushDouble(double val) {
        this.mBuffer.putDouble(val);
    }

    public int popInt() {
        return this.mBuffer.getInt();
    }

    public void pushInt64(long val) {
        this.mBuffer.putLong(val);
    }

    public long popInt64() {
        return this.mBuffer.getLong();
    }

    public void pushString16(String val) {
        if (val == null) {
            this.mBuffer.putShort((short)0);
        } else {
            this.mBuffer.putShort((short)val.getBytes().length);
            if (val.getBytes().length > 0) {
                this.mBuffer.put(val.getBytes());
            }

        }
    }

    public String popString16() {
        short len = this.mBuffer.getShort();
        //byte[] buf = null;
        if (len > 0) {
            byte[] buf = new byte[len];
            this.mBuffer.get(buf);

            try {
                return new String(buf, "ISO-8859-1");
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }
        }

        return "";
    }

    public String popString16UTF8() {
        short len = this.mBuffer.getShort();
        //byte[] buf = null;
        if (len > 0) {
            byte[] buf = new byte[len];
            this.mBuffer.get(buf);

            try {
                return new String(buf, "utf-8");
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }
        }

        return "";
    }

    public void pushStringArray(ArrayList<String> vals) {
        if (vals == null) {
            this.pushInt(0);
        } else {
            int len = vals.size();
            this.pushShort((short)len);

            for(int i = 0; i < len; ++i) {
                this.pushBytes(((String)vals.get(i)).getBytes());
            }

        }
    }

    public void pushIntArray(int[] vals) {
        if (vals == null) {
            this.pushInt(0);
        } else {
            int len = vals.length;
            this.pushInt(len);

            for(int i = 0; i < len; ++i) {
                this.pushInt(vals[i]);
            }

        }
    }

    public void pushIntArray(Integer[] vals) {
        if (vals == null) {
            this.pushInt(0);
        } else {
            int len = vals.length;
            this.pushInt(len);

            for(int i = 0; i < len; ++i) {
                this.pushInt(vals[i]);
            }

        }
    }

    public int[] popIntArray() {
        int len = this.popInt();
        int[] vals = new int[len];

        for(int i = 0; i < len; ++i) {
            vals[i] = this.popInt();
        }

        return vals;
    }

    public void pushShortArray(short[] vals) {
        if (vals == null) {
            this.pushInt(0);
        } else {
            int len = vals.length;
            this.pushInt(len);

            for(int i = 0; i < len; ++i) {
                this.pushShort(vals[i]);
            }

        }
    }

    public short[] popShortArray() {
        int len = this.popInt();
        short[] vals = new short[len];

        for(int i = 0; i < len; ++i) {
            vals[i] = this.popShort();
        }

        return vals;
    }

    public void clear() {
        this.mBuffer.position(10);
    }
}

