package Weapons;

import Player.Player;
import Sound.Sound;
import Weapons.Bullets.Bullets;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Sniper extends Weapon {

    private final Timer timer = new Timer();

    public Sniper(Player player) {
        super(player, 14, 8, 16); // 示例路径和旋转点
        setImg("src/main/resources/Weapon/sniper.png");
        setAmmo(6);
        setMaxAmmo(6);
        setRange(1500);
        setAccuracy(0.95);
        setBulletSpeed(30);
        setFireRate(1);
        setLastFireTime(0);
        setReloadTime(5000);
        setReloadStartTime(0);
        setPenetration(5);
        setDamage(200);
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

                Sound.sniperShootSound.playSoundEffect();
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
        Sound.sniperReloadingSound.playSoundEffect();

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
                    Sound.sniperReloadedSound.playSoundEffect();
                }
            }
        }, interval, interval);
    }

    @Override
    public Point getOffset() {
        return new Point(0, 12);
    }
}