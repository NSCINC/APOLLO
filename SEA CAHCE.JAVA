import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Token structure
class NSC_IAToken {
    String tokenId; // SHA256 produces a 64-character hex string
    long expirationTime;

    NSC_IAToken() {
        this.tokenId = generateToken();
        this.expirationTime = System.currentTimeMillis() + 300000; // 5 minutes in milliseconds
    }

    private String generateToken() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(timestamp.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isValid() {
        return System.currentTimeMillis() < expirationTime;
    }
}

// SEA Asset structure
class SEAAsset {
    String origin;
    String createdAt;
    double value;

    SEAAsset(String origin, double value) {
        this.origin = origin;
        this.value = value;
        this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}

// Cache structure
class Cache {
    private static final int MAX_SIZE = 3;
    private final String[] keys = new String[MAX_SIZE];
    private final SEAAsset[] values = new SEAAsset[MAX_SIZE];
    private int currentSize = 0;
    private final Lock lock = new ReentrantLock();

    void add(NSC_IAToken token, String key, double value, String origin) {
        if (!token.isValid()) {
            System.out.println("Invalid token. Operation denied.");
            return;
        }

        lock.lock();
        try {
            if (currentSize >= MAX_SIZE) {
                System.out.println("Removed from cache (LRU): " + keys[0]);
                System.arraycopy(keys, 1, keys, 0, MAX_SIZE - 1);
                System.arraycopy(values, 1, values, 0, MAX_SIZE - 1);
                currentSize--;
            }

            SEAAsset seaAsset = new SEAAsset(origin, value);
            keys[currentSize] = key;
            values[currentSize] = seaAsset;
            currentSize++;
            System.out.println("New item added to cache: " + key);
        } finally {
            lock.unlock();
        }
    }

    SEAAsset get(NSC_IAToken token, String key) {
        if (!token.isValid()) {
            System.out.println("Invalid token. Operation denied.");
            return null;
        }

        lock.lock();
        try {
            for (int i = 0; i < currentSize; i++) {
                if (keys[i].equals(key)) {
                    System.out.println("Item found in cache: " + key);
                    System.out.printf("Value: %.2f\n", values[i].value);
                    return values[i];
                }
            }
            System.out.println("Item not found in cache: " + key);
            return null;
        } finally {
            lock.unlock();
        }
    }

    void remove(NSC_IAToken token, String key) {
        if (!token.isValid()) {
            System.out.println("Invalid token. Operation denied.");
            return;
        }

        lock.lock();
        try {
            for (int i = 0; i < currentSize; i++) {
                if (keys[i].equals(key)) {
                    System.out.println("Item removed from cache: " + key);
                    System.arraycopy(keys, i + 1, keys, i, currentSize - i - 1);
                    System.arraycopy(values, i + 1, values, i, currentSize - i - 1);
                    currentSize--;
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    void list(NSC_IAToken token) {
        if (!token.isValid()) {
            System.out.println("Invalid token. Operation denied.");
            return;
        }

        lock.lock();
        try {
            System.out.println("Listing all items in cache:");
            for (int i = 0; i < currentSize; i++) {
                System.out.printf("%s: %.2f (Origin: %s, Created At: %s)\n",
                        keys[i], values[i].value, values[i].origin, values[i].createdAt);
            }
        } finally {
            lock.unlock();
        }
    }
}

// Thread class for cache operations
class CacheOperationsThread extends Thread {
    private final Cache cache;
    private final NSC_IAToken token;

    CacheOperationsThread(Cache cache, NSC_IAToken token) {
        this.cache = cache;
        this.token = token;
    }

    @Override
    public void run() {
        cache.add(token, "Key1", 100.0, "System A");
        cache.add(token, "Key2", 200.0, "System B");
        cache.get(token, "Key1");
        cache.remove(token, "Key1");
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        NSC_IAToken token = new NSC_IAToken();
        Cache myCache = new Cache();

        // Create two threads for cache operations
        CacheOperationsThread thread1 = new CacheOperationsThread(myCache, token);
        CacheOperationsThread thread2 = new CacheOperationsThread(myCache, token);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // List remaining items in the cache
        myCache.list(token);
    }
}
