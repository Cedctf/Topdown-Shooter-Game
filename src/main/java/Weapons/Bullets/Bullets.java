package Weapons.Bullets;

import GameObject.GameObject;
import Main.GameApplication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Bullets extends GameObject {

    private BufferedImage bulletImage;
    private double dx, dy;
    private double angle; // 子弹的旋转角度
    private double distanceTraveled = 0; // 子弹飞行距离
    private final int range; // 子弹的最大射程
    private final int speed;
    private int damage; // 新增字段，子弹伤害值
    private final double accuracy; // 精准度
    private int penetration; // 新增穿透值
    private boolean isPenetrated = false; // 新增字段，跟踪子弹是否应该移除
    // 新增：记录已经击中的敌人
    private Set<GameObject> hitEnemies;


    public Bullets(GameApplication gameApplication, int x, int y, int targetX, int targetY, int speed, int range, double accuracy, int damage) {
        super(gameApplication, x, y);
        this.speed = speed;
        this.range = range;
        this.accuracy = accuracy;
        this.damage = damage; // 设置子弹的伤害值
        hitEnemies = new HashSet<>();
        calculateDirectionAndAngle(x, y, targetX, targetY);
        loadBulletImage("src/main/resources/Weapon/bullet.png");
    }

    private void loadBulletImage(String filePath) {
        try {
            bulletImage = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateDirectionAndAngle(int startX, int startY, int targetX, int targetY) {
        int deltaX = targetX - startX;
        int deltaY = targetY - startY;
        double baseAngle = Math.atan2(deltaY, deltaX);

        // 根据精准度计算角度偏移量
        double maxAngleOffset = Math.toRadians((1.0 - accuracy) * 15); // ±15度为基准偏移
        double spreadAngle = new Random().nextDouble() * maxAngleOffset * 2 - maxAngleOffset;

        angle = baseAngle + spreadAngle;

        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
    }

    public void update() {
        setX(getX() + (int) dx);
        setY(getY() + (int) dy);
        distanceTraveled += Math.sqrt(dx * dx + dy * dy);
    }

    public boolean isOutOfRange() {
        return distanceTraveled >= range;
    }

    @Override
    public void paintSelf(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x = getX();
        int y = getY();
        int width = bulletImage.getWidth();
        int height = bulletImage.getHeight();

        // 使用 AffineTransform 来旋转子弹
        AffineTransform backup = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.rotate(angle, x + width / 2, y + height / 2);
        g2d.setTransform(transform);
        g2d.drawImage(bulletImage, x, y, null);
        g2d.setTransform(backup);

        //bullet hit box for debugging purpose
//        g.setColor(Color.YELLOW);
//        g.fillOval(x + width / 2, y + height / 2, 3, 3);
//        g.drawRect(x, y + 1, width, height);

    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(getX(), getY(), bulletImage.getWidth(), bulletImage.getHeight());
    }

    // 新增穿透值的 getter 和 setter 方法
    public int getPenetration() {
        return penetration;
    }

    public void setPenetration(int penetration) {
        this.penetration = penetration;
    }

    public boolean isPenetrated() {
        return isPenetrated;
    }

    public void setPenetrated(boolean pentrated) {
        isPenetrated = pentrated;
    }

    public int getDamage() {
        return damage;
    }

    public Set<GameObject> getHitEnemies() {
        return hitEnemies;
    }
}
