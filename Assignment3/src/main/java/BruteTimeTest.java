import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Aleksei Grishkov on 13.03.2016.
 *
 * A load test for {@link BruteCollinearPoints}
 */
public class BruteTimeTest {
    public static void main(String[] args) {
        int N = 512;
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            points[i] = new Point(StdRandom.uniform(32768), StdRandom.uniform(32768));
        }
        Stopwatch stopwatch = new Stopwatch();
        new BruteCollinearPoints(points);
        System.out.println(stopwatch.elapsedTime());
    }
}
