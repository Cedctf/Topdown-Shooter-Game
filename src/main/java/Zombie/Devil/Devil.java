package Zombie.Devil;

import Collision.CollisionChecker;
import GameObject.GameObject;
import Main.GameApplication;
import Player.Player;
import Map.Map;
import Sound.Sound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Devil extends GameObject {

    // 怪物spriteSheet
    private DevilSprite devilSprite;
    private int currentFrame = 0;
    private int animationCounter = 0;
    private String currentAnimation = "WALK";

    // 怪物朝向
    private boolean facingRight = true;

    // 动画状态的动画速度
    private final int walkAnimationSpeed = 10;
    private final int shootAnimationSpeed = 20;

    private long lastShootTime = 0;
    private boolean isShooting = false;

    // 子弹列表
    private ArrayList<DevillBullets> devillBullets = new ArrayList<>();

    private Map map;
    private CollisionChecker collisionChecker;

    public Devil(GameApplication gameApplication, Map map) {
        super(gameApplication);
        devilSprite = new DevilSprite("src/main/resources/Zombie/devil.png");
        this.map = map;
        this.collisionChecker = new CollisionChecker(map);
        setSpeed(1); // Devil移动速度
        setHP(500);
        setCurrentHP(500);
    }

    private void setAnimation(String animation) {
        if (!currentAnimation.equals(animation)) {
            currentAnimation = animation;
            currentFrame = 0;
            animationCounter = 0;
        }
    }

    private int getCurrentAnimationSpeed() {
        return currentAnimation.equals("SHOOT") ? shootAnimationSpeed : walkAnimationSpeed;
    }

    public void update(Player player) {
        double distanceToPlayer = distanceToPlayer(player);

        // 检查是否在射击冷却中
        if (System.currentTimeMillis() - lastShootTime < 8000) {
            if (distanceToPlayer >= 300) {
                moveTowardsPlayer(player);
                setAnimation("WALK");
            } else if (distanceToPlayer < 300) {
                moveAwayFromPlayer(player);
                setAnimation("WALK");
            }
        } else if (distanceToPlayer <= 800) {
            // 优先处理射击
            setAnimation("SHOOT");
            if (!isShooting) {
                isShooting = true;
            }
        } else {
            // 只有当不在射击冷却中，才进行移动
            if (distanceToPlayer >= 300) {
                moveTowardsPlayer(player);
                setAnimation("WALK");
            } else if (distanceToPlayer < 300) {
                moveAwayFromPlayer(player);
                setAnimation("WALK");
            }
            isShooting = false;
        }

        determineFacing(player.getX());
        updateAnimation(player);
        updateBullets();
    }

    private void moveTowardsPlayer(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();

        if (getX() < playerX && !willCollide(getX() + getSpeed(), getY())) {
            setX(getX() + getSpeed());
        } else if (getX() > playerX && !willCollide(getX() - getSpeed(), getY())) {
            setX(getX() - getSpeed());
        }

        if (getY() < playerY && !willCollide(getX(), getY() + getSpeed())) {
            setY(getY() + getSpeed());
        } else if (getY() > playerY && !willCollide(getX(), getY() - getSpeed())) {
            setY(getY() - getSpeed());
        }
    }

    private void moveAwayFromPlayer(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();

        if (getX() < playerX && !willCollide(getX() - getSpeed(), getY())) {
            setX(getX() - getSpeed());
        } else if (getX() > playerX && !willCollide(getX() + getSpeed(), getY())) {
            setX(getX() + getSpeed());
        }

        if (getY() < playerY && !willCollide(getX(), getY() - getSpeed())) {
            setY(getY() - getSpeed());
        } else if (getY() > playerY && !willCollide(getX(), getY() + getSpeed())) {
            setY(getY() + getSpeed());
        }
    }

    private boolean willCollide(int newX, int newY) {
        Rectangle futureRect = new Rectangle(newX + 20, newY + 20, 56, 59); // 假设怪物的宽度和高度分别为96和99
        return collisionChecker.checkCollision(futureRect);
    }

    private void updateAnimation(Player player) {
        animationCounter++;
        if (animationCounter >= getCurrentAnimationSpeed()) {
            animationCounter = 0;
            currentFrame++;
            if (currentAnimation.equals("WALK") && currentFrame >= devilSprite.getWalkFrames().length) {
                currentFrame = 0;
            } else if (currentAnimation.equals("SHOOT") && currentFrame >= devilSprite.getShootFrames().length) {
                currentFrame = 0;
                shootAtPlayer(player);
                isShooting = false;
                lastShootTime = System.currentTimeMillis();
            }
        }
    }

    private void updateBullets() {
        Iterator<DevillBullets> iterator = devillBullets.iterator();
        while (iterator.hasNext()) {
            DevillBullets devillBullets1 = iterator.next();
            devillBullets1.update();
            // 检查子弹是否超出屏幕或命中目标
            if (devillBullets1.getDistanceTraveled() > 1500) {
                iterator.remove();
            }
        }
    }

    private void determineFacing(int playerX) {
        // 如果Devil在player左边，则朝向右边，否则朝向左边
        facingRight = getX() < playerX;
    }

    private double distanceToPlayer(Player player) {
        int dx = getX() - player.getX();
        int dy = getY() - player.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void shootAtPlayer(Player player) {
        // 创建子弹对象并添加到子弹列表中
        DevillBullets devillBullets1 = new DevillBullets(gameApplication, getX(), getY(), player.getX(), player.getY());
        devillBullets.add(devillBullets1);
        Sound.devilFireball.playSoundEffect();
    }

    public BufferedImage getCurrentFrame() {
        if (currentAnimation.equals("SHOOT")) {
            return devilSprite.getShootFrames()[currentFrame];
        } else {
            return devilSprite.getWalkFrames()[currentFrame];
        }
    }

    public List<DevillBullets> getBullets() {
        return devillBullets;
    }

    //devil take damage
    public void takeDamage(int damage){
        setCurrentHP(getCurrentHP() - damage);
    }

    @Override
    public void paintSelf(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage frame = getCurrentFrame();
        int x = getX();
        int y = getY();
        int width = frame.getWidth();
        int height = frame.getHeight();

        if (facingRight) {
            g2d.drawImage(frame, x, y, null);
        } else {
            g2d.drawImage(frame, x + width, y, -width, height, null);
        }

        // 绘制子弹
        for (DevillBullets devillBullets1 : devillBullets) {
            devillBullets1.paintSelf(g);
        }

        //Devil hit box for debugging purpose
//        g.setColor(Color.RED);
//        g.fillOval(x + width / 2, y + height / 2, 5, 5);
//        g.drawRect(getX() + 20, getY() + 20, 56, 59);

        //添加生命值
        this.addHP(g,0,0, 96,10, Color.RED);
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(getX() + 20, getY() + 20, 56, 59); // 调整宽度和高度以适应sprite大小
    }
}