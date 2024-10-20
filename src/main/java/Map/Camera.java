package Map;

public class Camera {
    private int x;
    private int y;
    private int width;
    private int height;
    private int mapWidth;
    private int mapHeight;

    public Camera(int width, int height, int mapWidth, int mapHeight) {
        this.width = width;
        this.height = height;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void update(int targetX, int targetY) {
        x = targetX - width / 2;
        y = targetY - height / 2;

        // 确保相机不会超出地图边界
        x = Math.max(0, Math.min(x, mapWidth - width));
        y = Math.max(0, Math.min(y, mapHeight - height));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}