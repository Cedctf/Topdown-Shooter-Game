// new zombie path finder
// no usage and unfinished

/*
package Zombie;

import java.awt.Point;
import java.util.*;

public class AStarPathFinder {

    private static final int[] DX = {-1, 1, 0, 0}; // 上下左右
    private static final int[] DY = {0, 0, -1, 1};

    public List<Point> findPath(int[][] grid, Point start, Point goal) {
        int rows = grid.length;
        int cols = grid[0].length;
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        boolean[][] closedList = new boolean[rows][cols];
        Node[][] nodes = new Node[rows][cols];

        Node startNode = new Node(start, 0, heuristic(start, goal));
        openList.add(startNode);
        nodes[start.x][start.y] = startNode;

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            if (current.point.equals(goal)) {
                return reconstructPath(nodes, goal);
            }

            closedList[current.point.x][current.point.y] = true;

            for (int i = 0; i < 4; i++) {
                int newX = current.point.x + DX[i];
                int newY = current.point.y + DY[i];

                if (isValid(newX, newY, rows, cols) && grid[newX][newY] == 0 && !closedList[newX][newY]) {
                    int g = current.g + 1;
                    int h = heuristic(new Point(newX, newY), goal);
                    Node neighbor = nodes[newX][newY];

                    if (neighbor == null || g < neighbor.g) {
                        Node newNode = new Node(new Point(newX, newY), g, h);
                        newNode.parent = current;
                        nodes[newX][newY] = newNode;
                        openList.add(newNode);
                    }
                }
            }
        }

        return Collections.emptyList(); // 如果没有路径
    }

    private int heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private boolean isValid(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private List<Point> reconstructPath(Node[][] nodes, Point goal) {
        List<Point> path = new ArrayList<>();
        Node current = nodes[goal.x][goal.y];
        while (current != null) {
            path.add(current.point);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static class Node {
        Point point;
        int g; // 从起点到当前节点的实际成本
        int h; // 从当前节点到目标节点的估计成本
        int f; // g + h 的总评估成本
        Node parent;

        Node(Point point, int g, int h) {
            this.point = point;
            this.g = g;
            this.h = h;
            this.f = g + h; // 计算 f 值
        }
    }
}
*/