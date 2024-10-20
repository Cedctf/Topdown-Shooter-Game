package UserInfo;


import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String password;
    private Map<String, String> keybinds;

    public User(String username, String password, Map<String, String> keybinds) {
        this.username = username;
        this.password = password;
        this.keybinds = keybinds;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, String> getKeybinds() {
        return keybinds;
    }

    public void setKeybinds(Map<String, String> keybinds) {
        this.keybinds = keybinds;
    }
}
