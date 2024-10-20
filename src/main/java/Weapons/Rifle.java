package Weapons;

import Player.Player;
import Sound.Sound;
import Weapons.Bullets.Bullets;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Rifle extends Weapon {

    private final Timer timer = new Timer();

    public Rifle(Player player) {
        super(player, 7, 6, -9); // 示例路径和旋转点
        setImg("src/main/resources/Weapon/rifle.png");
        setAmmo(30);
        setMaxAmmo(30);
        setRange(800);
        setAccuracy(0.8);
        setBulletSpeed(15);
        setFireRate(5);
        setLastFireTime(0);
        setReloadTime(3000);
        setReloadStartTime(0);
        setPenetration(2);
        setDamage(20);
    }

    @Override
    public void Shoot(int mouseX, int mouseY) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - getLastFireTime() >= 1000 / getFireRate()) {
            if (getAmmo() > 0) {
                setAmmo(getAmmo() - 1);

                // 创建新的子弹并添加到 bullets 列表中
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

                Sound.rifleShootSound.playSoundEffect();
                setLastFireTime(currentTime);
                System.out.println("Pistol fired. Ammo left: " + getAmmo());
            } else {
                System.out.println("Out of ammo.");
            }
        }
    }

    @Override
    public void Reload() {
        reloadStartTime = System.currentTimeMillis();
        System.out.println("Reloading...");
        Sound.rifleReloadingSound.playSoundEffect();

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
                } else {
                    cancel(); // 达到最大子弹数量时取消定时器
                    player.isReloading = false;
                    System.out.println("Reload complete. Ammo: " + getAmmo());
                    Sound.rifleReloadedSound.playSoundEffect();
                }
            }
        }, interval, interval);
    }

    @Override
    public Point getOffset() {
        return new Point(16, 16);
    }
}