package Main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

public class ScoreManager {
    private static final String FILE_PATH = "highscore.txt";
    private Map<String, String> properties;

    public ScoreManager() {
        properties = new HashMap<>();
        loadProperties();
    }

    // 加载属性文件
    private void loadProperties() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(": ");
                    if (parts.length == 2) {
                        properties.put(parts[0].trim(), parts[1].trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found: " + FILE_PATH);
        }
    }

    // 保存属性文件
    private void saveProperties() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取属性值
    public String getProperty(String key) {
        return properties.get(key);
    }

    // 设置属性值并保存
    public void setProperty(String key, String value) {
        properties.put(key, value);
        saveProperties();
    }

    // 设置和获取最高分数
    public void setHighScore(String username, int score, String mode) {
        setProperty("username", username);
        setProperty("highestScore", String.valueOf(score));
        setProperty("mode", mode);
    }

//    public void displayHighScore() {
//        String username = getProperty("username");
//        String highScore = getProperty("highestScore");
//        String mode = getProperty("mode");
//
//        System.out.println("Username: " + username);
//        System.out.println("Highest Score: " + highScore);
//        System.out.println("Mode: " + mode);
//    }

}
