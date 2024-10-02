import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NoxusAI {
    private static final int BUFFER_SIZE = 213;
    private byte[] buffer;
    private int bufferSize;

    // Initialize NoxusAI
    public NoxusAI() {
        buffer = new byte[BUFFER_SIZE];
        bufferSize = 0;
    }

    // Update the digest with a single byte
    public void update(byte value) {
        if (bufferSize >= BUFFER_SIZE) {
            throw new RuntimeException("Buffer overflow");
        }
        buffer[bufferSize++] = value;
    }

    // Update the digest with an array of bytes
    public void updateBytes(byte[] bytes) {
        for (byte b : bytes) {
            update(b);
        }
    }

    // Update the digest with a big-endian integer
    public void updateBigEndianInt(int value) {
        byte[] byteBuffer = new byte[4];
        byteBuffer[0] = (byte) ((value >> 24) & 0xFF);
        byteBuffer[1] = (byte) ((value >> 16) & 0xFF);
        byteBuffer[2] = (byte) ((value >> 8) & 0xFF);
        byteBuffer[3] = (byte) (value & 0xFF);
        updateBytes(byteBuffer);
    }

    // Reset the buffer
    public void reset() {
        bufferSize = 0;
    }

    // Calculate the SHA-256 digest
    public byte[] digest() {
        if (bufferSize != BUFFER_SIZE) {
            throw new RuntimeException("Buffer not filled with " + BUFFER_SIZE + " bytes");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(buffer);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public static void main(String[] args) {
        // Tests
        NoxusAI noxusDigest = new NoxusAI();

        // Update the digest with 213 bytes
        for (int i = 0; i < BUFFER_SIZE; i++) {
            noxusDigest.update((byte) 0);
        }

        byte[] digestOutput = noxusDigest.digest();

        // Print the digest in hexadecimal format
        for (byte b : digestOutput) {
            System.out.printf("%02x", b);
        }
        System.out.println();

        // More tests can be added here...
    }
}
