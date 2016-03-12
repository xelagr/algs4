import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by alex on 08.03.16.
 */
public class FastCollinearPoints {

    private List<LineSegment> lineSegments = new ArrayList<>();

    //TODO get rid of duplicates
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("Points array should be not null");
        }
        for (Point point : points) {
            if (point == null) {
                throw new NullPointerException("There should not be a null point");
            }
        }
        for (int i = 0; i < points.length; i++) {
            List<Double> slopes = new ArrayList<>(points.length-1);
            Point p = points[i];
            for (int j = 0; j < points.length; j++) {
                if (i == j) continue;
                double slopeTo = p.slopeTo(points[j]);
                slopes.add(slopeTo);
                StdOut.printf("%s -> %s : %f", p, points[j], slopeTo);
            }
            StdOut.println();
            Point[] sortedPoints = Arrays.copyOf(points, points.length);
            Arrays.sort(sortedPoints, points[i].slopeOrder());
            StdOut.println(Arrays.toString(sortedPoints));
            slopes.sort(null);
            StdOut.println(slopes);
            int sameSlopes = 1;
            double firstSlope = slopes.get(0);
            int maxSlopes = 1;
            Point segmentEnd = null;
            for (int j = 1; j < slopes.size(); j++) {
                if (firstSlope == slopes.get(j)) {
                    sameSlopes++;
                }
                else {
                    if (sameSlopes >= 3 && sameSlopes > maxSlopes) {
                        maxSlopes = sameSlopes;
                        segmentEnd = sortedPoints[j-1];
//                        lineSegments.add(new LineSegment(p, sortedPoints[j-1]));
                    }
                    sameSlopes = 1;
                    firstSlope = slopes.get(j);
                }
            }
            if (segmentEnd != null) {
                lineSegments.add(new LineSegment(p, segmentEnd));
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
