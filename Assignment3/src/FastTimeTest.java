import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Aleksei Grishkov on 13.03.2016.
 *
 * A load test for {@link FastCollinearPoints}
 */
public class FastTimeTest {
    public static void main(String[] args) {
        int N = 4096;
        int maxY = 8;
        int maxX = N / maxY;
        boolean[][] grid = new boolean[maxX][maxY];
        Point[] points = new Point[N];
        StdRandom.setSeed(100);
        for (int i = 0; i < N;) {
            int x = StdRandom.uniform(maxX);
            int y = StdRandom.uniform(maxY);
            if (!grid[x][y]) {
                grid[x][y] = true;
                points[i++] = new Point(x, y);
            }
        }
        Stopwatch stopwatch = new Stopwatch();
        new FastCollinearPoints(points);
        System.out.println(stopwatch.elapsedTime());
    }
}
