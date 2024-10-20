package Player;

import Collision.CollisionChecker;
import GameObject.GameObject;
import Main.GameApplication;
import Map.Map;
import Sound.Sound;
import Weapons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

    //移动
    public boolean up, down, left, right;

    // 角色朝向
    private boolean facingRight = true;

    //角色射击
    public boolean isShooting = false;

    //角色换弹
    public boolean isReloading = false;

    // 玩家spriteSheet
    private PlayerSprite playerSprite;
    private int currentFrame = 0;
    private int animationCounter = 0;
    private String currentAnimation = "IDLE";

    //角色武器
    private Weapon currentWeapon;

    // 每个动画状态的动画速度
    private final int idleAnimationSpeed = 10;
    private final int reloadAnimationSpeed = 30;
    private final int shootAnimationSpeed = 20;
    private final int walkAnimationSpeed = 10;

    private Map map;

    private CollisionChecker collisionChecker;

    public Player(GameApplication gameApplication, Map map) {
        super(gameApplication);
        playerSprite = new PlayerSprite("src/main/resources/Player/player.png");
        this.map = map;
        this.collisionChecker = new CollisionChecker(map);
        setX(100);
        setY(100);
        setSpeed(3);
        setHP(300);
        setCurrentHP(300);
        setCurrentWeapon(new Pistol(this));
    }

    public void updateWeaponPosition(){
        Point offset = currentWeapon.getOffset();
        currentWeapon.setX(getX() + offset.x);
        currentWeapon.setY(getY() + offset.y);
    }

    public void updateWeaponAngle(int mouseX, int mouseY) {
        double angle = Math.atan2(mouseY - getY(), mouseX - getX());
        currentWeapon.setAngle(angle);
    }

    private void setAnimation(String animation) {
        if (!currentAnimation.equals(animation)) {
            currentAnimation = animation;
            currentFrame = 0;
            animationCounter = 0;
        }
    }

    private int getCurrentAnimationSpeed() {
        return switch (currentAnimation) {
            case "IDLE" -> idleAnimationSpeed;
            case "RELOAD" -> reloadAnimationSpeed;
            case "SHOOT" -> shootAnimationSpeed;
            case "WALK" -> walkAnimationSpeed;
            default -> idleAnimationSpeed;
        };
    }

    public void update() {
        handleInput();
        updateAnimation();
        updateWeaponPosition();
        currentWeapon.update();
    }

    private void handleInput() {
        int deltaX = 0;
        int deltaY = 0;

        if (up) deltaY -= getSpeed();
        if (down) deltaY += getSpeed();
        if (left) deltaX -= getSpeed();
        if (right) deltaX += getSpeed();

        if (deltaX != 0 || deltaY != 0) {
            setAnimation("WALK");
            // Move only if no collision detected
            move(deltaX, deltaY);
        } else {
            setAnimation("IDLE");
        }
    }

    private void move(int deltaX, int deltaY) {
        // Calculate new horizontal position
        int newX = getX() + deltaX;
        Rectangle horizontalRectangle = new Rectangle(newX, getY(), 44, 46);
        // Check for horizontal collision
        if (!collisionChecker.checkCollision(horizontalRectangle)) {
            // Update the player's X position if no collision detected
            setX(newX);
        }

        // Calculate new vertical position
        int newY = getY() + deltaY;
        Rectangle verticalRectangle = new Rectangle(getX(), newY, 44, 46);
        // Check for vertical collision
        if (!collisionChecker.checkCollision(verticalRectangle)) {
            // Update the player's Y position if no collision detected
            setY(newY);
        }
    }


    private void updateAnimation() {
        animationCounter++;
        if (animationCounter >= getCurrentAnimationSpeed()) {
            animationCounter = 0;
            currentFrame++;
            switch (currentAnimation) {
                case "IDLE":
                    if (currentFrame >= playerSprite.getIdleFrames().length) {
                        currentFrame = 0;
                    }
                    break;
                case "RELOAD":
                    if (currentFrame >= playerSprite.getReloadFrames().length) {
                        currentFrame = 0;
                    }
                    break;
                case "SHOOT":
                    if (currentFrame >= playerSprite.getShootFrames().length) {
                        currentFrame = 0;
                    }
                    break;
                case "WALK":
                    if (currentFrame >= playerSprite.getWalkFrames().length) {
                        currentFrame = 0;
                        Sound.playerWalkingSound.playSoundEffect();
                    }
                    break;
            }
        }
    }

    public BufferedImage getCurrentFrame() {
        return switch (currentAnimation) {
            case "WALK" -> playerSprite.getWalkFrames()[currentFrame];
            case "RELOAD" -> playerSprite.getReloadFrames()[currentFrame];
            case "SHOOT" -> playerSprite.getShootFrames()[currentFrame];
            default -> playerSprite.getIdleFrames()[currentFrame];
        };
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public void takeDamage(int damage){
        setCurrentHP(getCurrentHP() - damage);
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(getX(), getY(), 44, 46); // Adjust width and height as per sprite size
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

        //添加生命值
        this.addHP(g,0,0, 44,7, Color.GREEN);

        //添加子弹条
        g.setColor(Color.BLACK);
        g.fillRect(getX() + 1, getY() + 42, 44, 5);
        g.setColor(Color.GRAY);
        g.fillRect(getX() + 1, getY() + 42, (int)(44 * (double)getCurrentWeapon().getAmmo() / getCurrentWeapon().getMaxAmmo()), 5);

        getCurrentWeapon().paintSelf(g);

//        g.setColor(Color.GREEN);
//        g.fillOval(x + width / 2, y + height / 2, 5, 5);
//        g.drawRect(x, y + 1, width, height);
    }
}