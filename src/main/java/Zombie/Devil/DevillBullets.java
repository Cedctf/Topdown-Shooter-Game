package Zombie.Devil;

import GameObject.GameObject;
import Main.GameApplication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DevillBullets extends GameObject {

    private BufferedImage spriteSheet;
    private BufferedImage[] bulletFrames;
    private int currentFrame = 0;
    private int animationCounter = 0;
    private final int animationSpeed = 5;
    private final int speed = 8;
    private double dx, dy;
    private double angle; // 子弹的旋转角度
    private double distanceTraveled = 0; // 子弹飞行距离

    public DevillBullets(GameApplication gameApplication, int x, int y, int targetX, int targetY) {
        super(gameApplication, x, y);
        calculateDirectionAndAngle(x, y, targetX, targetY);
        loadSpriteSheet("src/main/resources/Zombie/devilbullets.png");
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
        int tileWidth = 33;
        int tileHeight = 33;

        bulletFrames = new BufferedImage[7];

        for (int i = 0; i < 7; i++) {
            bulletFrames[i] = spriteSheet.getSubimage(i * tileWidth, 0, tileWidth, tileHeight);
        }
    }

    private void calculateDirectionAndAngle(int startX, int startY, int targetX, int targetY) {
        int deltaX = targetX - startX;
        int deltaY = targetY - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        dx = (deltaX / distance) * speed;
        dy = (deltaY / distance) * speed;
        angle = Math.atan2(deltaY, deltaX);
    }

    public void update() {
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            animationCounter = 0;
            currentFrame++;
            if (currentFrame >= bulletFrames.length) {
                currentFrame = 0;
            }
        }

        setX(getX() + (int) dx);
        setY(getY() + (int) dy);
        distanceTraveled += Math.sqrt(dx * dx + dy * dy);
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public BufferedImage getCurrentFrame() {
        return bulletFrames[currentFrame];
    }

    @Override
    public void paintSelf(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage frame = getCurrentFrame();
        int x = getX();
        int y = getY();
        int width = frame.getWidth();
        int height = frame.getHeight();

        // 使用 AffineTransform 来旋转子弹
        AffineTransform backup = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.rotate(angle, x + width / 2, y + height / 2);
        g2d.setTransform(transform);
        g2d.drawImage(frame, x, y, null);
        g2d.setTransform(backup);

        //Devil Bullet hit box for debugging purpose
//        g.setColor(Color.YELLOW);
//        g.fillOval(x + width / 2, y + height / 2, 3, 3);
//        g.drawRect(x, y + 1, width, height);
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(getX(), getY(), bulletFrames[0].getWidth(), bulletFrames[0].getHeight());
    }
}