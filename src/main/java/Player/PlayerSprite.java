package Player;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PlayerSprite {
    private BufferedImage spriteSheet;

    private BufferedImage[] idleFrames;
    private BufferedImage[] reloadFrames;
    private BufferedImage[] shootFrames;
    private BufferedImage[] walkFrames;

    public PlayerSprite(String filePath) {
        loadSpriteSheet(filePath);
        loadAnimations();
    }

    private void loadSpriteSheet(String filePath) {
        try {
            spriteSheet = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAnimations() {
        int tileWidth = 44;
        int tileHeight = 46;

        idleFrames = new BufferedImage[6];
        reloadFrames = new BufferedImage[11];
        shootFrames = new BufferedImage[4];
        walkFrames = new BufferedImage[7];

        // Load IDLE animation frames
        for (int i = 0; i < 6; i++) {
            idleFrames[i] = spriteSheet.getSubimage(i * tileWidth, 0, tileWidth, tileHeight);
        }

        // Load RELOAD animation frames
        for (int i = 0; i < 11; i++) {
            reloadFrames[i] = spriteSheet.getSubimage(i * tileWidth, tileHeight, tileWidth, tileHeight);
        }

        // Load SHOOT animation frames
        for (int i = 0; i < 4; i++) {
            shootFrames[i] = spriteSheet.getSubimage(i * tileWidth, 2 * tileHeight, tileWidth, tileHeight);
        }

        // Load WALK animation frames
        for (int i = 0; i < 7; i++) {
            walkFrames[i] = spriteSheet.getSubimage(i * tileWidth, 3 * tileHeight, tileWidth, tileHeight);
        }
    }

    public BufferedImage[] getIdleFrames() {
        return idleFrames;
    }

    public BufferedImage[] getReloadFrames() {
        return reloadFrames;
    }

    public BufferedImage[] getShootFrames() {
        return shootFrames;
    }

    public BufferedImage[] getWalkFrames() {
        return walkFrames;
    }
}
