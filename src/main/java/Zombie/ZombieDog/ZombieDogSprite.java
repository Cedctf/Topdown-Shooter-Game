package Zombie.ZombieDog;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ZombieDogSprite {
    private BufferedImage spriteSheet;

    private BufferedImage[] walkFrames;

    public ZombieDogSprite(String filePath) {
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
        int tileWidth = 72;
        int tileHeight = 37;

        walkFrames = new BufferedImage[6];

        // Load WALK animation frames
        for (int i = 0; i < 6; i++) {
            walkFrames[i] = spriteSheet.getSubimage(i * tileWidth, 0, tileWidth, tileHeight);
        }
    }

    public BufferedImage[] getWalkFrames() {
        return walkFrames;
    }
}
