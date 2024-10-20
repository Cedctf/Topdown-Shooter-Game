package Weapons;

import GameObject.GameObject;
import Player.Player;
import Weapons.Bullets.Bullets;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public abstract class Weapon extends GameObject {

    protected int ammo;
    protected int maxAmmo;
    protected int range;
    protected int fireRate;
    protected int bulletSpeed;
    protected int reloadTime;
    protected int damage;
    protected double accuracy;
    protected long lastFireTime;
    protected long reloadStartTime;
    protected List<Bullets> bullets;
    protected int penetration; // 新增穿透值

    protected int rotationX;
    protected int rotationY;
    protected int offset;
    protected double angle;

    public Weapon(Player player, int rotationX, int rotationY, int offset) {
        super(player);
        bullets = new ArrayList<>();
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.offset = offset;
        this.angle = 0;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPenetration() {
        return penetration;
    }

    public void setPenetration(int penetration) {
        this.penetration = penetration;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public long getLastFireTime() {
        return lastFireTime;
    }

    public void setLastFireTime(long lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public long getReloadStartTime() {
        return reloadStartTime;
    }

    public void setReloadStartTime(long reloadStartTime) {
        this.reloadStartTime = reloadStartTime;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Point getOffset() {
        return new Point(0,0);
    }

    private int calculateOffset(){
        if (angle > Math.PI / 2 || angle < -Math.PI / 2) {
            return offset;
        }
        return 0;
    }

    public abstract void Shoot(int mouseX, int mouseY);

    public abstract void Reload();

    public void update() {
        for (Bullets bullets1: bullets) {
            bullets1.update();
        }
        bullets.removeIf(Bullets::isOutOfRange);
        bullets.removeIf(Bullets::isPenetrated);
    }

    public List<Bullets> getBullets() {
        return bullets;
    }

    protected void addBullet(Bullets bullet) {
        bullets.add(bullet);
    }

    public Rectangle getRec() {
        return null;
    }


    protected void drawShootingArc(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int arcAngle = (int) Math.round((1 - accuracy) * 30); // 精准度低时扇形角度大
        int x = getX() + rotationX;
        int y = getY() + rotationY;

        // 转换角度以便于画扇形
        int startAngle = (int) Math.toDegrees(-angle) - arcAngle / 2;
        int endAngle = arcAngle;

        // 设置半透明的红色用于标示扇形区域
        g2d.setColor(new Color(255, 0, 0, 128));
        g2d.rotate(Math.toRadians(-angle), x, y); // 使用负角度来旋转扇形
        g2d.fillArc(x - range, y - range, 2 * range, 2 * range, startAngle, endAngle);
        g2d.dispose();
    }


    @Override
    public void paintSelf(Graphics g) {

        //Debugging purpose
        //drawShootingArc(g);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(getX() + rotationX + calculateOffset(), getY() + rotationY);
        g2d.rotate(angle);
        if (angle > Math.PI / 2 || angle < -Math.PI / 2) {
            g2d.scale(1, -1);
        }
        g.drawImage(getImg(), -rotationX, -rotationY, null);
        g2d.setTransform(old);

        for (Bullets bullets1: bullets) {
            bullets1.paintSelf(g);
        }
    }
}