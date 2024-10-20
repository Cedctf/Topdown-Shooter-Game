package Main;

import Collision.CollisionChecker;
import Map.*;
import Map.Map;
import Player.Player;
import Event.*;
import Sound.Sound;
import Zombie.Zombie.Zombie;
import Zombie.Devil.Devil;
import Zombie.ZombieDog.ZombieDog;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

import static Menu.PlaySubpanel.getSelectedDifficulty;

public class GameApplication extends JFrame{

    // 双缓存
    private Image offScreenImage = null;

    //地图
    Map map = new Map(this);

    // 初始化玩家
    Player player = new Player(this, map);
    // 怪物列表
    private final ArrayList<Zombie> zombies = new ArrayList<>();
    private final ArrayList<Devil> devils = new ArrayList<>();
    private final ArrayList<ZombieDog> zombieDogs = new ArrayList<>();

    // 初始化相机
    Camera camera = new Camera(TileManager.screenWidth, TileManager.screenHeight, TileManager.getWidth(), TileManager.getHeight());


    //初始化积分系统
    private ScoreManager scoreManager = new ScoreManager();
    public int currentScore;
    public String highestScoreStr;
    public int highestScore;

    // 初始化移动
    KeyHandler keyHandler = new KeyHandler(player);
    MouseHandler mouseHandler = new MouseHandler(player, camera);

    private final CollisionChecker collisionChecker = new CollisionChecker(map);

    public Timer spawnTimer = new Timer();
    private int zombieKillCount = 0;
    private int devilKillCount = 0;
    private int zombieDogKillCount = 0;

    private javax.swing.Timer gameTimer;

    public void launch() {
        // 设置尺寸
        this.setSize(TileManager.screenWidth, TileManager.screenHeight);
        // 窗口居中
        this.setLocationRelativeTo(null);
        // 窗口可视
        this.setVisible(true);
        // 关闭事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 固定大小
        this.setResizable(false);
        // 标题
        this.setTitle("Test");

        // 加入键盘监听
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);

        // 设置自定义鼠标光标图标
        setCustomCursor();

        //定义僵尸可生成范围
        map.initializeSpawnableAreas();



        //设置游戏难度
        String selectedDifficulty = getSelectedDifficulty();
        if (selectedDifficulty.equals("Easy")) {
            easyMode();
        }
        else if (selectedDifficulty.equals("Medium")) {
            mediumMode();
        }
        else if (selectedDifficulty.equals("Hard")) {
            hardMode();
        }


        Sound.ingameTheme.playMusic();
        Sound.ingameTheme.setVolume(0.8f);
        Sound.ingameTheme.loop();

        gameTimer = new javax.swing.Timer(1000 / 60, e -> gameLoop());
        gameTimer.start();
    }

    private void gameLoop() {
        if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
            player.update();
            Iterator<Zombie> zombieIterator = zombies.iterator();
            while (zombieIterator.hasNext()) {
                Zombie zombie = zombieIterator.next();
                zombie.update(player);
                if (zombie.getCurrentHP() <= 0) {
                    Sound.zombieDeathSound.playSoundEffect();
                    zombieIterator.remove();
                    zombieKillCount++;
                }
            }

            Iterator<Devil> devilIterator = devils.iterator();
            while (devilIterator.hasNext()) {
                Devil devil = devilIterator.next();
                devil.update(player);
                if (devil.getCurrentHP() <= 0) {
                    Sound.devilDeathSound.playSoundEffect();
                    devilIterator.remove();
                    devilKillCount++;
                }
            }

            Iterator<ZombieDog> zombieDogIterator = zombieDogs.iterator();
            while (zombieDogIterator.hasNext()) {
                ZombieDog zombieDog = zombieDogIterator.next();
                zombieDog.update(player);
                if (zombieDog.getCurrentHP() <= 0) {
                    Sound.zombieDogDeathSound.playSoundEffect();
                    zombieDogIterator.remove();
                    zombieDogKillCount++;
                }
            }

            camera.update(player.getX(), player.getY());
            Sound.ingameTheme.resume(); // Resume the music when the game is not paused
        } else {
            Sound.ingameTheme.pause(); // Pause the music when the game is paused
        }
        repaint();

        collisionChecker.checkCollisions(player, zombies, devils, zombieDogs);

        // 计算当前分数并与最高分数比较
        currentScore = zombieKillCount + 5 * devilKillCount + 2 * zombieDogKillCount;
        highestScoreStr = scoreManager.getProperty("highestScore");
        highestScore = (highestScoreStr != null) ? Integer.parseInt(highestScoreStr) : 0;
    }

    private void easyMode(){
        // 定时生成 Zombie
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
                    map.createZombie(zombies);
                    map.createZombieDog(zombieDogs);
                }
            }
        }, 0, 3000);

        // 定时生成 Devil
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
                    map.createDevil(devils);
                }
            }
        }, 0, 30000);
    }

    private void mediumMode(){
        // 定时生成 Zombie
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
                    map.createZombie(zombies);
                    map.createZombieDog(zombieDogs);
                }
            }
        }, 0, 2000);

        // 定时生成 Devil
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
                    map.createDevil(devils);
                }
            }
        }, 0, 16000);
    }

    private void hardMode(){
        // 定时生成 Zombie
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
                    map.createZombie(zombies);
                    map.createZombieDog(zombieDogs);
                }
            }
        }, 0, 1000);

        // 定时生成 Devil
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
                    map.createDevil(devils);
                }
            }
        }, 0, 10000);
    }

    public void cancelSpawnTimer() {
        if (spawnTimer != null) {
            spawnTimer.cancel();
            spawnTimer = null;
        }
    }

    private void setCustomCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage("src/main/resources/Weapon/crosshair.png");
        Point cursorHotSpot = new Point(0, 0);
        Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Custom Cursor");
        this.setCursor(customCursor);
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(TileManager.getWidth(), TileManager.getHeight());
        }
        Graphics gImage = offScreenImage.getGraphics();

        // 绘制地图
        map.draw(gImage);
        //绘制碰撞箱
        //map.drawCollisionAreas(gImage);
        // 绘制角色
        player.paintSelf(gImage);

        // 绘制怪物
        synchronized (zombies) {
            for (Zombie zombie : zombies) {
                zombie.paintSelf(gImage);
            }
        }
        synchronized (zombieDogs) {
            for (ZombieDog zombieDog : zombieDogs) {
                zombieDog.paintSelf(gImage);
            }
        }
        synchronized (devils) {
            for (Devil devil : devils) {
                devil.paintSelf(gImage);
            }
        }

        // 显示暂停状态
        if (KeyHandler.isPaused()) {
            gImage.setColor(new Color(0, 0, 0, 150)); // 半透明背景
            gImage.fillRect(0, 0, TileManager.screenWidth + camera.getX(), TileManager.screenHeight + camera.getY());
            gImage.setColor(Color.WHITE);
            gImage.setFont(new Font("Arial", Font.BOLD, 50));
            gImage.drawString("Paused", TileManager.screenWidth / 2 - 100 + camera.getX(), TileManager.screenHeight / 2 + camera.getY());
            //绘制当前分数和最高分数
            gImage.setFont(new Font("Arial", Font.BOLD, 30));
            gImage.drawString("HighestScore: " + highestScore, TileManager.screenWidth / 2 - 100 + camera.getX(), TileManager.screenHeight / 2 + 50 + camera.getY());
            gImage.drawString("Score: " + currentScore, TileManager.screenWidth / 2 - 100 + camera.getX(), TileManager.screenHeight / 2 + 100 + camera.getY());
            // 绘制击杀计数
            gImage.setColor(Color.WHITE);
            gImage.setFont(new Font("Arial", Font.BOLD, 10));
            gImage.drawString("Zombie Kills: " + zombieKillCount, 10 + camera.getX(), 50 + camera.getY());
            gImage.drawString("Devil Kills: " + devilKillCount, 10 + camera.getX(), 70 + camera.getY());
            gImage.drawString("ZombieDog Kills: " + zombieDogKillCount, 10 + camera.getX(), 90 + camera.getY());
        }

        if (player.getCurrentHP() <= 0) {
            KeyHandler.endGame();
            Sound.ingameTheme.stop();
            gImage.setColor(new Color(0, 0, 0, 150)); // 半透明背景
            gImage.fillRect(0, 0, TileManager.screenWidth + camera.getX(), TileManager.screenHeight + camera.getY());
            gImage.setColor(Color.WHITE);
            gImage.setFont(new Font("Arial", Font.BOLD, 50));
            gImage.drawString("End Game", TileManager.screenWidth / 2 - 100 + camera.getX(), TileManager.screenHeight / 2 + camera.getY());
            // 绘制分数
            gImage.setFont(new Font("Arial", Font.BOLD, 30));
            // 计算当前分数并与最高分数比较
            if (currentScore >= highestScore) {
                scoreManager.setProperty("highestScore", String.valueOf(currentScore));
                gImage.drawString("Congratulations! Highest Score In The Game Achieved!", TileManager.screenWidth / 2 - 400 + camera.getX(), 100 + camera.getY());
                gImage.drawString("HighestScore: " + highestScore, TileManager.screenWidth / 2 - 100 + camera.getX(), TileManager.screenHeight / 2 + 50 + camera.getY());
            } else {
                gImage.drawString("Score: " + currentScore, TileManager.screenWidth / 2 - 30 + camera.getX(), TileManager.screenHeight / 2 + 50 + camera.getY());
            }
        }

        // 添加到窗口
        g.drawImage(offScreenImage, -camera.getX(), -camera.getY(), null);
    }

    public static void execute() {
        SwingUtilities.invokeLater(() -> {
            KeyHandler.reset();
            GameApplication game = new GameApplication();
            game.launch();
        });
    }
}