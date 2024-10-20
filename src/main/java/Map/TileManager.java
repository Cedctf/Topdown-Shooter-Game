package Map;

import Main.GameApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TileManager {

    GameApplication ga;

    public static final int tileSize = 32;
    public static final int maxScreenCol = 40;
    public static final int maxScreenRow = 25;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;

    //World map
    public static final int maxWorldCol = 80;
    public static final int maxWorldRow = 50;
    public static final int worldWidth = tileSize * maxWorldCol;
    public static final int worldHeight = tileSize * maxWorldRow;

    //From tile class to save tiles
    public Tile[] tile;

    //save the location from txt file
    public int mapTileNum[][];
    public int map2LayerTileNum[][];

    public TileManager(){
        mapTileNum = new int[maxWorldCol][maxWorldRow];
        map2LayerTileNum = new int[maxWorldCol][maxWorldRow];
        tile = new Tile[100];
    }

    public int[][] loadMap(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            int col = 0;
            int row = 0;

            while (col < maxWorldCol && row < maxWorldRow) {
                String line = br.readLine();
                String[] numbers = line.split(" ");

                while (col < maxWorldCol) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }

                if (col == maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading map: " + filePath);
        }
        return mapTileNum;
    }


    public int[][] loadLayer2Map(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            int col = 0;
            int row = 0;

            while (col < maxWorldCol && row < maxWorldRow) {
                String line = br.readLine();
                String[] numbers = line.split(" ");

                while (col < maxWorldCol) {
                    int num = Integer.parseInt(numbers[col]);
                    map2LayerTileNum[col][row] = num;
                    col++;
                }

                if (col == maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading map: " + filePath);
        }
        return map2LayerTileNum;
    }

    public Tile getTileImage(int index, String imageName, String imageUrl, boolean collision) {
        tile[index] = new Tile();
        tile[index].image = loadImage(imageName, imageUrl);
        tile[index].collision = collision;
        return tile[index];
    }

    public final static HashMap<String, BufferedImage> ImageDetails = new HashMap<>();

    public BufferedImage loadImage(String imageName, String imageUrl) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResourceAsStream(imageUrl));
            ImageDetails.put(imageName, img);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load image: " + imageUrl);
        }
        return img;
    }

    public static int getWidth () {
        return worldWidth;
    }

    public static int getHeight () {
        return worldHeight;
    }

}
