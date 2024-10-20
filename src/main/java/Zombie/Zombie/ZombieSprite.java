package Zombie.Zombie;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ZombieSprite {
    private BufferedImage spriteSheet;

    private BufferedImage[] walkFrames;

    public ZombieSprite(String filePath) {
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

        walkFrames = new BufferedImage[7];

        // Load WALK animation frames
        for (int i = 0; i < 7; i++) {
            walkFrames[i] = spriteSheet.getSubimage(i * tileWidth, 0, tileWidth, tileHeight);
        }
    }

    public BufferedImage[] getWalkFrames() {
        return walkFrames;
    }
}
