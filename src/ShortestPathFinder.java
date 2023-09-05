import java.awt.*;
import java.util.*;
import java.util.List;

public class ShortestPathFinder {
    public static List<Point> findShortestPath(int n, Point start, Point end) {
        int[][] grid = new int[n][n];
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();

        queue.add(start);
        grid[start.x][start.y] = 1; // Mark the start as visited

        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} }; // Up, Down, Left, Right

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(end)) {
                // Reconstruct the path from end to start
                List<Point> path = new ArrayList<>();
                Point node = end;
                while (!node.equals(start)) {
                    path.add(node);
                    node = parentMap.get(node);
                }
                path.add(start);
                Collections.reverse(path);
                return path;
            }

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (isValidMove(n, newX, newY) && grid[newX][newY] == 0) {
                    Point neighbor = new Point(newX, newY);
                    queue.add(neighbor);
                    grid[newX][newY] = 1; // Mark as visited
                    parentMap.put(neighbor, current);
                }
            }
        }

        // No path found
        return null;
    }

    private static boolean isValidMove(int n, int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    public static void main(String[] args) {
        int n = 5;
        Point start = new Point(0, 0);
        Point end = new Point(3, 2);

        List<Point> shortestPath = findShortestPath(n, start, end);

        if (shortestPath != null) {
            System.out.println("Shortest Path:");
            for (Point point : shortestPath) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }
        } else {
            System.out.println("No path found.");
        }
    }
}
