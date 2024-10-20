package GameObject;

import Main.GameApplication;
import Player.Player;
import Weapons.*;

import java.awt.*;

public abstract class GameObject {

    //坐标
    private int x;
    private int y;
    //速度
    private int speed;
    //图片
    private Image img;
    //初始生命值
    private int HP;
    //当前生命值
    private int currentHP;
    //游戏界面
    public GameApplication gameApplication;
    //玩家
    public static Player player;
    //武器
    public Weapon weapon;

    public GameObject (GameApplication gameApplication){
        this.gameApplication = gameApplication;
    }

    public GameObject (Player player){
        this.player = player;
    }

    public GameObject (GameApplication gameApplication,int x,int y){
        this.gameApplication = gameApplication;
        this.x = x;
        this.y = y;
    }

    //返回矩形
    public abstract Rectangle getRec();

    //绘制元素
    public abstract void paintSelf(Graphics g);

    //添加生命值
    public void addHP(Graphics g, int difx, int dify, int width, int height, Color color){
        //g - 绘制生命值的画笔
        //difx & dify - 调整生命值位置
        //width & height - 调整生命值大小
        //color - 生命值的颜色

        //绘制外部轮廓
        g.setColor(Color.BLACK);
        g.fillRect(getX() - difx, getY() - dify, width, height);
        //生命条
        g.setColor(color);
        g.fillRect(getX() - difx, getY() - dify, (int)(width * (double)getCurrentHP() / getHP()), height);
    }

    public static double getDistanceToPlayer(int x, int y) {
        int playerX = player.getX();
        int playerY = player.getY();
        int dx = x - playerX;
        int dy = y - playerY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {return speed;}

    public void setSpeed(int speed) {this.speed = speed;}

    public Image getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = Toolkit.getDefaultToolkit().getImage(img);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public GameApplication getGameApplication() {
        return gameApplication;
    }
}
