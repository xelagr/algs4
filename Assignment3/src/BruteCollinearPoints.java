import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 07.03.16.
 */
public class BruteCollinearPoints {

    List<LineSegment> lineSegments = new ArrayList<>();

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
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (j == i) continue;
                for (int k = 0; k < points.length; k++) {
                    if (k == j) continue;
                    for (int l = 0; l < points.length; l++) {
                        if (l == k) continue;
                        Point p1 = points[i];
                        Point p2 = points[j];
                        Point p3 = points[k];
                        Point p4 = points[l];
                        if (equals(p1, p2)) {
                            throw new IllegalArgumentException("Equal points are not allowed");
                        }
                        double slope1 = p1.slopeTo(p2);
                        double slope2 = p1.slopeTo(p3);
                        double slope3 = p1.slopeTo(p4);
                        int p1Top2 = p1.compareTo(p2);
                        int p2Top3 = p2.compareTo(p3);
                        int p3Top4 = p3.compareTo(p4);
                        if (slope1 == slope2 && slope2 == slope3 &&
                                ((p1Top2 < 0 && p2Top3 < 0 && p3Top4 < 0))) {
                            System.out.printf("%d, %d, %d, %d - > %s, %s, %s, %s\n", i, j, k, l, p1, p2, p3, p4);
                            lineSegments.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }
    }

    private boolean equals(Point p1, Point p2) {
        return p1.compareTo(p2) == 0;
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
