package Weapons;

import Player.Player;
import Sound.Sound;
import Weapons.Bullets.Bullets;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Shotgun extends Weapon {

    private final Timer timer = new Timer();

    public Shotgun(Player player) {
        super(player, 7, 6, -6); // 示例路径和旋转点
        setImg("src/main/resources/Weapon/shotgun.png");
        setAmmo(6);
        setMaxAmmo(6);
        setRange(300);
        setAccuracy(3);
        setBulletSpeed(20);
        setFireRate(1);
        setLastFireTime(0);
        setReloadTime(4000);
        setReloadStartTime(0);
        setPenetration(5);
        setDamage(50);
    }

    @Override
    public void Shoot(int mouseX, int mouseY) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - getLastFireTime() >= 1000 / getFireRate()) {
            if (getAmmo() > 0) {
                setAmmo(getAmmo() - 1);

                // 随机生成子弹数量
                int numShots = 4 + new Random().nextInt(7 - 4 + 1);

                // 创建随机角度的子弹并添加到 bullets 列表中
                for (int i = 0; i < numShots; i++) {
                    Bullets bullets1 = new Bullets(
                            player.getGameApplication(),
                            getX(),
                            getY(),
                            mouseX,
                            mouseY,
                            getBulletSpeed(),
                            getRange(),
                            getAccuracy(),
                            getDamage()){};
                    bullets1.setPenetration(getPenetration());
                    addBullet(bullets1);
                }

                Sound.shotgunShootSound.playSoundEffect();
                setLastFireTime(currentTime);
                System.out.println("Shotgun fired. Ammo left: " + getAmmo());
            } else {
                System.out.println("Out of ammo.");
            }
        }
    }

    @Override
    public void Reload() {
        reloadStartTime = System.currentTimeMillis();
        System.out.println("Reloading...");

        // 计算初始的时间间隔
        long initialInterval = getReloadTime() / getMaxAmmo();

        // 计算总共需要的时间
        long totalReloadTime = getReloadTime();

        // 根据当前子弹数量比例调整时间间隔
        long interval = getAmmo() > 0 ? totalReloadTime / (getMaxAmmo() - getAmmo()) : initialInterval;

        // 设置定时任务，根据 interval 定期增加子弹直到满
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getAmmo() < getMaxAmmo()) {
                    setAmmo(getAmmo() + 1);
                    System.out.println("Ammo loaded: " + getAmmo());
                    Sound.shotgunReloadShell.playSoundEffect();
                } else {
                    cancel(); // 达到最大子弹数量时取消定时器
                    player.isReloading = false;
                    System.out.println("Reload complete. Ammo: " + getAmmo());
                    Sound.shotgunReloadPump.playSoundEffect();
                }
            }
        }, interval, interval);
    }

    @Override
    public Point getOffset() {
        return new Point(16, 16);
    }
}