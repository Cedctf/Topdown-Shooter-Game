package Zombie.Devil;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DevilSprite {
    private BufferedImage spriteSheet;

    private BufferedImage[] shootFrames;
    private BufferedImage[] walkFrames;

    public DevilSprite(String filePath) {
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
        int tileWidth = 96;
        int tileHeight = 99;

        shootFrames = new BufferedImage[7];
        walkFrames = new BufferedImage[7];

        // Load WALK animation frames
        for (int i = 0; i < 7; i++) {
            walkFrames[i] = spriteSheet.getSubimage(i * tileWidth, 0, tileWidth, tileHeight);
        }

        // Load SHOOT animation frames
        for (int i = 0; i < 7; i++) {
            shootFrames[i] = spriteSheet.getSubimage(i * tileWidth, tileHeight, tileWidth, tileHeight);
        }
    }

    public BufferedImage[] getShootFrames() {
        return shootFrames;
    }

    public BufferedImage[] getWalkFrames() {
        return walkFrames;
    }
}
