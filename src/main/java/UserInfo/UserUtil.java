package UserInfo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserUtil {
    private static final String FILE_PATH = "users.dat";
    private static final String[] ACTIONS = {"Move up", "Move down", "Move left", "Move right", "Shoot"};
    private static final String[] DEFAULT_KEYS = {"W", "S", "A", "D", "SPACE"};
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final byte[] SECRET_KEY = "MySuperSecretKey".getBytes();

    public static void saveUser(String username, String password, Map<String, String> keybinds) throws IOException {
        if (isUsernameTaken(username)) {
            throw new IOException("Username already exists");
        }
        Map<String, User> users = loadUsers();
        users.put(username, new User(username, encrypt(password), keybinds));
        saveAllUsers(users);
    }

    public static boolean validateUser(String username, String password) throws IOException {
        Map<String, User> users = loadUsers();
        User user = users.get(username);
        return user != null && decrypt(user.getPassword()).equals(password);
    }

    public static boolean isUsernameTaken(String username) throws IOException {
        Map<String, User> users = loadUsers();
        return users.containsKey(username);
    }

    public static Map<String, String> loadUserKeybinds(String username) throws IOException {
        Map<String, User> users = loadUsers();
        User user = users.get(username);
        if (user != null) {
            return user.getKeybinds();
        }
        return getDefaultKeybinds();
    }

    public static Map<String, User> loadUsers() throws IOException {
        Map<String, User> users = new HashMap<>();
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (Map<String, User>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static void saveAllUsers(Map<String, User> users) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        }
    }

    public static Map<String, String> getDefaultKeybinds() {
        Map<String, String> defaultKeybinds = new HashMap<>();
        for (int i = 0; i < ACTIONS.length; i++) {
            defaultKeybinds.put(ACTIONS[i], DEFAULT_KEYS[i]);
        }
        return defaultKeybinds;
    }

    private static String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Encryption error: " + e.getMessage(), e);
        }
    }

    private static String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Decryption error: " + e.getMessage(), e);
        }
    }
}
