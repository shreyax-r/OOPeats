package Utils;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

public class PasswordUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LEN = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LEN = 256;

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LEN];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) throws Exception {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LEN);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = f.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public static String generateStrongPasswordHash(String password) throws Exception {
        String salt = generateSalt();
        String hash = hashPassword(password, salt);
        return salt + "$" + hash;
    }

    public static boolean verifyPassword(String password, String stored) throws Exception {
        if (stored == null) return false;
        String[] parts = stored.split("\\$");
        if (parts.length != 2) return false;
        String salt = parts[0];
        String hash = parts[1];
        String computed = hashPassword(password, salt);
        return computed.equals(hash);
    }
}
