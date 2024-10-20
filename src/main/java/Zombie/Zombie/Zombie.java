package Zombie.Zombie;

import Collision.CollisionChecker;
import GameObject.GameObject;
import Main.GameApplication;
import Player.Player;
import Map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Zombie extends GameObject {

    // 怪物spriteSheet
    private ZombieSprite zombieSprite;
    private int currentFrame = 0;
    private int animationCounter = 0;
    private String currentAnimation = "WALK";

    // 怪物朝向
    private boolean facingRight = true;

    // 动画状态的动画速度
    private final int walkAnimationSpeed = 10;

    private Map map;
    private CollisionChecker collisionChecker;

    public Zombie(GameApplication gameApplication, Map map) {
        super(gameApplication);
        zombieSprite = new ZombieSprite("src/main/resources/Zombie/zombie.png");
        this.map = map;
        this.collisionChecker = new CollisionChecker(map);
        setX(100); // 初始化僵尸位置，可以根据需要调整
        setY(100);
        setHP(100);
        setCurrentHP(100);
        setSpeed(1); // 僵尸移动速度
    }

    private void setAnimation(String animation) {
        if (!currentAnimation.equals(animation)) {
            currentAnimation = animation;
            currentFrame = 0;
            animationCounter = 0;
        }
    }

    private int getCurrentAnimationSpeed() {
        return walkAnimationSpeed;
    }

    public void update(Player player) {
        moveTowardsPlayer(player);
        determineFacing(player.getX());
        this.updateAnimation();
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

    private boolean willCollide(int newX, int newY) {
        Rectangle futureRect = new Rectangle(newX + 10, newY + 10, 24, 26);
        return collisionChecker.checkCollision(futureRect);
    }

    private void updateAnimation() {
        animationCounter++;
        if (animationCounter >= getCurrentAnimationSpeed()) {
            animationCounter = 0;
            currentFrame++;
            if (currentAnimation.equals("WALK")) {
                if (currentFrame >= zombieSprite.getWalkFrames().length) {
                    currentFrame = 0;
                }
            }
        }
    }

    private void determineFacing(int playerX) {
        // 如果zombie在player左边，则朝向右边，否则朝向左边
        setFacingRight(getX() < playerX);
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public BufferedImage getCurrentFrame() {
        return zombieSprite.getWalkFrames()[currentFrame];
    };

    public void takeDamage(int damage){
        setCurrentHP(getCurrentHP() - damage);
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(getX() + 10, getY() + 10, 24, 26);
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

        //zombie hit box for debugging purpose
//        g.setColor(Color.RED);
//        g.fillOval(x + width / 2, y + height / 2, 5, 5);
//        g.drawRect(getX() + 10, getY() + 10, 24, 26);

        //添加生命值
        this.addHP(g,0,0, 44,7, Color.RED);
    }

}
