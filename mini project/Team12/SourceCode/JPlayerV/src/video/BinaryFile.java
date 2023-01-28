package video;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryFile {
    private RandomAccessFile stream;

    public BinaryFile(String path) throws Exception {
        stream = new RandomAccessFile(path, "r");
    }

    public int readInt16() throws Exception {
        byte[] data = new byte[2];
        stream.readFully(data);
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(data);
        return buffer.getShort(0);
    }

    public int readInt32() throws Exception {
        byte[] data = new byte[4];
        stream.readFully(data);
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(data);
        return buffer.getInt(0);
    }

    public String readString(int length) throws Exception {
        byte[] data = new byte[length];
        stream.readFully(data);
        return new String(data);
    }

    public byte[] readBytes(int length) throws Exception {
        byte[] data = new byte[length];
        stream.readFully(data);
        return data;
    }

    public void skip(int length) throws Exception {
        stream.skipBytes(length);
    }

    public void close() throws Exception {
        stream.close();
    }
}
