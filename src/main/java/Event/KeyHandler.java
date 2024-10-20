package Event;

import Player.Player;
import Sound.Sound;
import UserInfo.UserSession;
import UserInfo.UserUtil;
import Weapons.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Menu.GameMenu.launchGameMenu;

public class KeyHandler implements KeyListener {
    private Player player;
    private static boolean isPaused = false;
    public static boolean isStop = false;
    private Map<String, Integer> keybinds;

    public KeyHandler(Player player) {
        this.player = player;
        loadKeybinds();
    }

    private void loadKeybinds() {
        try {
            Map<String, String> userKeybinds = UserUtil.loadUserKeybinds(UserSession.getUsername());
            keybinds = convertKeybindsToKeyCode(userKeybinds);
        } catch (IOException e) {
            e.printStackTrace();
            keybinds = getDefaultKeybinds();
        }
    }

    private Map<String, Integer> convertKeybindsToKeyCode(Map<String, String> userKeybinds) {
        Map<String, Integer> keyCodes = new HashMap<>();
        for (Map.Entry<String, String> entry : userKeybinds.entrySet()) {
            keyCodes.put(entry.getKey(), KeyEvent.getExtendedKeyCodeForChar(entry.getValue().charAt(0)));
        }
        return keyCodes;
    }

    private Map<String, Integer> getDefaultKeybinds() {
        Map<String, Integer> defaultKeybinds = new HashMap<>();
        defaultKeybinds.put("Move up", KeyEvent.VK_W);
        defaultKeybinds.put("Move down", KeyEvent.VK_S);
        defaultKeybinds.put("Move left", KeyEvent.VK_A);
        defaultKeybinds.put("Move right", KeyEvent.VK_D);
        defaultKeybinds.put("Shoot", KeyEvent.VK_SPACE);
        return defaultKeybinds;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == keybinds.get("Move up") && !isPaused && !isStop) {
            player.up = true;
        } else if (keyCode == keybinds.get("Move down") && !isPaused && !isStop) {
            player.down = true;
        } else if (keyCode == keybinds.get("Move left") && !isPaused && !isStop) {
            player.left = true;
        } else if (keyCode == keybinds.get("Move right") && !isPaused && !isStop) {
            player.right = true;
        } else {
            switch (keyCode) {
                case KeyEvent.VK_R:
                    if (!isPaused && !isStop) {
                        player.isReloading = true;
                        player.getCurrentWeapon().Reload();
                    }
                    break;
                case KeyEvent.VK_1:
                    if (!isPaused && !isStop && !player.isReloading) player.setCurrentWeapon(new Pistol(player));
                    break;
                case KeyEvent.VK_2:
                    if (!isPaused && !isStop && !player.isReloading) player.setCurrentWeapon(new Rifle(player));
                    break;
                case KeyEvent.VK_3:
                    if (!isPaused && !isStop && !player.isReloading) player.setCurrentWeapon(new Shotgun(player));
                    break;
                case KeyEvent.VK_4:
                    if (!isPaused && !isStop && !player.isReloading) player.setCurrentWeapon(new Sniper(player));
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (!isStop) togglePause();
                    else {
                        player.getGameApplication().dispose();
                        player.getGameApplication().cancelSpawnTimer();
                        Sound.menuTheme.playMusic();
                        Sound.menuTheme.loop();
                        Sound.menuTheme.setVolume(0.9f);
                        launchGameMenu();
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == keybinds.get("Move up")) {
            player.up = false;
        } else if (keyCode == keybinds.get("Move down")) {
            player.down = false;
        } else if (keyCode == keybinds.get("Move left")) {
            player.left = false;
        } else if (keyCode == keybinds.get("Move right")) {
            player.right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void togglePause() {
        isPaused = !isPaused;
    }

    public static boolean isPaused() {
        return isPaused;
    }

    public static boolean isStop() {
        return isStop;
    }

    public static void endGame() {
        isStop = true;
    }

    public static void reset() {
        isPaused = false;
        isStop = false;
    }
}
