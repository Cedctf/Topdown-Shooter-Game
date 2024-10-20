package Map;

import GameObject.GameObject;
import Main.GameApplication;
import Zombie.Zombie.*;
import Zombie.Devil.*;
import Zombie.ZombieDog.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map extends TileManager {

    GameApplication gameApplication;
    public int[][] mapTileNum;

    private Random random = new Random();

    private boolean[][] spawnableAreas;

    public Map(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        loadMapIndexLocation();
        loadTileImages();
    }

    private void loadMapIndexLocation() {
        mapTileNum = loadMap("/map1/map1source.txt");
    }

    private void loadTileImages() {
        // Initialize tiles array in the parent class
        tile = new Tile[100];

        // Load specific tiles for this map
        tile[0] = getTileImage(0, "MainTile", "/map1/MainTile.png", false);
        tile[1] = getTileImage(1, "roadTile", "/map1/road.png", false);
        tile[2] = getTileImage(2, "roadTopRight", "/map1/road-corner-bottom-right.png", false);
        tile[3] = getTileImage(3, "roadTopLeft", "/map1/road-corner-bottom-left.png", false);
        tile[4] = getTileImage(4, "roadBottomRight", "/map1/roadLeftCorner.png", false);
        tile[5] = getTileImage(5, "roadBottomLeft", "/map1/road-corner-top-left.png", false);
        tile[6] = getTileImage(6, "roadBottom", "/map1/roadBottom.png", false);
        tile[7] = getTileImage(7, "roadLeft", "/map1/road-side-Left.png", false);
        tile[8] = getTileImage(8, "roadRight", "/map1/road-side-right.png", false);
        tile[9] = getTileImage(9, "roadTop", "/map1/roadTop.png", false);
        tile[10] = getTileImage(10, "BottomLeftWithSign", "/map1/BottomLeftWithSign.png", true);
        tile[11] = getTileImage(11, "BottomRightWithSign", "/map1/BottomRightWithSign.png", true);
        tile[12] = getTileImage(12, "TopRightWithSign", "/map1/TopRightWithSign.png", true);
        tile[13] = getTileImage(13, "TopLeftWithSign", "/map1/topLeftWithSign.png", true);
        tile[14] = getTileImage(14, "SideBarrel", "/map1/SideBarrel.png", true);
        tile[15] = getTileImage(15, "LeftWithSign", "/map1/LeftWithSign.png", true);
        tile[16] = getTileImage(16, "RightWithSign", "/map1/RightWithSign.png", true);
        tile[17] = getTileImage(17, "PedestrainHorizontal", "/map1/road-pedestrian-horizontal.png", false);
        tile[18] = getTileImage(18, "PedestrainVertical", "/map1/road-pedestrian-vertical.png", false);
        tile[19] = getTileImage(19, "RoomFloor", "/map1/ROOM/room-floor-1.png", false);
        tile[20] = getTileImage(20, "LeftRoom", "/map1/ROOM/LeftRoom.png", true);
        tile[21] = getTileImage(21, "RightRoom", "/map1/ROOM/RightRoom.png", true);
        tile[22] = getTileImage(22, "RoomTopLeftCorner", "/map1/ROOM/room-wall-corner-top-left-0.png", true);
        tile[23] = getTileImage(23, "RoomTopRightCorner", "/map1/ROOM/room-wall-corner-top-right-0.png", true);
        tile[24] = getTileImage(24, "RoomWallMidNormal", "/map1/ROOM/room-wall-middle-0.png", true);
        tile[25] = getTileImage(25, "RoomWallMidSign", "/map1/ROOM/room-wall-middle-6.png", true);
        tile[26] = getTileImage(26, "CarLeft1", "/map1/CityCar/CarLeft/car1.png", true);
        tile[27] = getTileImage(27, "CarLeft2", "/map1/CityCar/CarLeft/car2.png", true);
        tile[28] = getTileImage(28, "CarLeft3", "/map1/CityCar/CarLeft/car3.png", true);
        tile[29] = getTileImage(29, "CarLeft4", "/map1/CityCar/CarLeft/car4.png", true);
        tile[30] = getTileImage(30, "CarLeft5", "/map1/CityCar/CarLeft/car5.png", true);
        tile[31] = getTileImage(31, "CarLeft6", "/map1/CityCar/CarLeft/car6.png", true);
        tile[32] = getTileImage(32, "CarLeft7", "/map1/CityCar/CarLeft/car7.png", true);
        tile[33] = getTileImage(33, "CarLeft8", "/map1/CityCar/CarLeft/car8.png", true);
        tile[34] = getTileImage(34, "CarLeft9", "/map1/CityCar/CarLeft/car9.png", true);
        tile[35] = getTileImage(35, "CarLeft10","/map1/CityCar/CarLeft/car10.png", true);
        tile[36] = getTileImage(36, "CarLeft11", "/map1/CityCar/CarLeft/car11.png", true);
        tile[37] = getTileImage(37, "CarLeft12", "/map1/CityCar/CarLeft/car12.png", true);
        tile[38] = getTileImage(38, "CarLeft13", "/map1/CityCar/CarLeft/car13.png", true);
        tile[39] = getTileImage(39, "CarLeft14", "/map1/CityCar/CarLeft/car14.png", true);
        tile[40] = getTileImage(40, "CarLeft15", "/map1/CityCar/CarLeft/car15.png", true);
        tile[41] = getTileImage(41, "CarLeft16", "/map1/CityCar/CarLeft/car16.png", true);
        tile[42] = getTileImage(42, "CarRight1", "/map1/CityCar/CarRight/1.png", true);
        tile[43] = getTileImage(43, "CarRight2", "/map1/CityCar/CarRight/2.png", true);
        tile[44] = getTileImage(44, "CarRight3", "/map1/CityCar/CarRight/3.png", true);
        tile[45] = getTileImage(45, "CarRight4", "/map1/CityCar/CarRight/4.png", true);
        tile[46] = getTileImage(46, "CarRight5", "/map1/CityCar/CarRight/5.png", true);
        tile[47] = getTileImage(47, "CarRight6", "/map1/CityCar/CarRight/6.png", true);
        tile[48] = getTileImage(48, "CarRight7", "/map1/CityCar/CarRight/7.png", true);
        tile[49] = getTileImage(49, "CarRight8", "/map1/CityCar/CarRight/8.png", true);
        tile[50] = getTileImage(50, "CarRight9", "/map1/CityCar/CarRight/9.png", true);
        tile[51] = getTileImage(51, "CarRight10", "/map1/CityCar/CarRight/10.png", true);
        tile[52] = getTileImage(52, "CarRight11", "/map1/CityCar/CarRight/11.png", true);
        tile[53] = getTileImage(53, "CarRight12", "/map1/CityCar/CarRight/12.png", true);
        tile[54] = getTileImage(54, "CarRight13", "/map1/CityCar/CarRight/13.png", true);
        tile[55] = getTileImage(55, "CarRight14", "/map1/CityCar/CarRight/14.png", true);
        tile[56] = getTileImage(56, "CarRight15", "/map1/CityCar/CarRight/15.png", true);
        tile[57] = getTileImage(57, "CarRight16", "/map1/CityCar/CarRight/16.png", true);
        tile[58] = getTileImage(58, "FenceLeft", "/map1/fence-left-collision.png", true);
        tile[59] = getTileImage(59, "FenceRight", "/map1/fence-right.png", true);
        tile[60] = getTileImage(60, "FenceMid", "/map1/fence-middle.png", true);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < maxWorldCol && worldRow < maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            // Check if tileNum is valid and the tile is initialized
            if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                int worldX = worldCol * tileSize;
                int worldY = worldRow * tileSize;

                g2.drawImage(tile[tileNum].image, worldX, worldY, tileSize, tileSize, null);
            } else {
                // Handle the case where the tile is not initialized
                System.out.println("Tile number " + tileNum + " at (" + worldCol + ", " + worldRow + ") is not initialized.");
            }

            worldCol++;

            if (worldCol == maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void drawCollisionAreas(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);

        for (int x = 0; x < maxWorldCol; x++) {
            for (int y = 0; y < maxWorldRow; y++) {
                int tileNum = mapTileNum[x][y];
                if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                    if (tile[tileNum].collision) {
                        int worldX = x * tileSize;
                        int worldY = y * tileSize;
                        g2.drawRect(worldX, worldY, tileSize, tileSize); // Draw rectangle around collision tiles
                    }
                }
            }
        }

        // Draw valid spawn areas
        g2.setColor(Color.GREEN);
        for (int x = 0; x < maxWorldCol; x++) {
            for (int y = 0; y < maxWorldRow; y++) {
                if (spawnableAreas[x][y]) {
                    int worldX = x * tileSize;
                    int worldY = y * tileSize;
                    g2.drawRect(worldX, worldY, tileSize, tileSize); // Draw rectangle around valid spawn areas
                }
            }
        }
    }

    public void initializeSpawnableAreas() {
        spawnableAreas = new boolean[maxWorldCol][maxWorldRow];
        for (int x = 0; x < maxWorldCol; x++) {
            for (int y = 0; y < maxWorldRow; y++) {
                int tileNum = mapTileNum[x][y];
                if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                    spawnableAreas[x][y] = !tile[tileNum].collision; // 生成点合法性依据碰撞标志
                }
            }
        }
    }

    public void createZombie(ArrayList<Zombie> zombies) {
        int x, y;
        do {
            // 生成随机位置
            x = random.nextInt(TileManager.getWidth());
            y = random.nextInt(TileManager.getHeight());
        } while (!isPositionValid(x,y));

        // 创建并设置僵尸位置
        Zombie zombie = new Zombie(gameApplication, this);
        zombie.setX(x);
        zombie.setY(y);
        zombies.add(zombie);
        System.out.println("Zombie created at: (" + x + ", " + y + ")");
    }

    public void createZombieDog(ArrayList<ZombieDog> zombieDogs) {
        int x, y;
        do {
            // 生成随机位置
            x = random.nextInt(TileManager.getWidth());
            y = random.nextInt(TileManager.getHeight());
        } while (!isPositionValid(x,y));

        // 创建并设置僵尸位置
        ZombieDog zombieDog = new ZombieDog(gameApplication, this);
        zombieDog.setX(x);
        zombieDog.setY(y);
        zombieDogs.add(zombieDog);
        System.out.println("ZombieDog created at: (" + x + ", " + y + ")");
    }

    public void createDevil(ArrayList<Devil> devils) {
        int x, y;
        do {
            // 生成随机位置
            x = random.nextInt(TileManager.getWidth());
            y = random.nextInt(TileManager.getHeight());
        } while (!isPositionValid(x,y));

        // 创建并设置僵尸位置
        Devil devil = new Devil(gameApplication, this);
        devil.setX(x);
        devil.setY(y);
        devils.add(devil);
        System.out.println("Devil created at: (" + x + ", " + y + ")");
    }

    // 检查生成点是否符合所有条件
    private boolean isPositionValid(int x, int y) {
        // 检查距离玩家
        if (GameObject.getDistanceToPlayer(x, y) >= 300 && GameObject.getDistanceToPlayer(x, y) <= 250) {
            return false;
        }

        // 检查是否在合法生成区域
        if (!spawnableAreas[x / tileSize][y / tileSize]) {
            return false;
        }

        // 检查是否距离不可生成区域的边界至少 50 格
        for (int i = 0; i < maxWorldCol; i++) {
            for (int j = 0; j < maxWorldRow; j++) {
                if (!spawnableAreas[i][j]) { // 如果是不可生成区域
                    int startX = i * tileSize;
                    int startY = j * tileSize;
                    int endX = startX + tileSize;
                    int endY = startY + tileSize;

                    // 检查生成点是否在不可生成区域的边界外 50 格
                    if (x < startX - 50 || x > endX + 50 || y < startY - 50 || y > endY + 50) {
                        continue; // 生成点在当前不可生成区域的边界外 50 格内，继续检查下一个不可生成区域
                    }
                    return false; // 生成点不满足距离不可生成区域边界 50 格的条件
                }
            }
        }
        return true; // 所有条件都符合
    }

    public static int getWidth() {
        return TileManager.getWidth();
    }

    public static int getHeight() {
        return TileManager.getHeight();
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public int[][] getMap1TileNum() {
        return mapTileNum;
    }

    public Tile[] getTile() {
        return tile;
    }
}

