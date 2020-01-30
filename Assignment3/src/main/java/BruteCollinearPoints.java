import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 07.03.16.
 *
 * A brute force algorithm, which:
 * 1. examines 4 points at a time
 * 2. checks whether they all lie on the same line segment
 * 3. returns all such line segments
 */
public class BruteCollinearPoints {

    private List<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("Points array should be not null");
        }
        for (Point point : points) {
            if (point == null) {
                throw new NullPointerException("There should not be a null point");
            }
        }
        lineSegments = new ArrayList<>(points.length);
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            for (int j = 0; j < points.length; j++) {
                if (j == i) continue;
                Point p2 = points[j];
                if (p1.compareTo(p2) == 0) {
                    throw new IllegalArgumentException("Equal points are not allowed");
                }
                double slope1 = p1.slopeTo(p2);
                if (p1.compareTo(p2) >= 0) continue;
                for (int k = 0; k < points.length; k++) {
                    if (k == j) continue;
                    Point p3 = points[k];
                    double slope2 = p1.slopeTo(p3);
                    if (slope1 != slope2 || p2.compareTo(p3) >= 0) continue;
                    for (int l = 0; l < points.length; l++) {
                        if (l == k) continue;
                        Point p4 = points[l];
                        double slope3 = p1.slopeTo(p4);
                        if (slope2 == slope3 && p3.compareTo(p4) < 0) {
//                            System.out.printf("%d, %d, %d, %d - > %s, %s, %s, %s\n", i, j, k, l, p1, p2, p3, p4);
                            lineSegments.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }
}
