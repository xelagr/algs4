import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by alex on 08.03.16.
 *
 *  A fast sort-based algorithm, which:
 * 1. checks whether 4 or more points lie on the same line segment
 * 2. returns all such maximum line segments without duplicates
 */
public class FastCollinearPoints {

    private List<LineSegment> lineSegments;

    public FastCollinearPoints(Point[] points) {
//        DebugMode.on();
        validatePoints(points);
        lineSegments = new ArrayList<>(points.length);
        Point[] sortedPoints = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
            Point basePoint = points[i];
            DebugMode.printf("Base point: %s\n", basePoint);

            Arrays.sort(sortedPoints, basePoint.slopeOrder());
            DebugMode.printf("Sorted points: %s\n", () -> Arrays.toString(sortedPoints));

            double[] slopes = getSortedSlopes(i, points);
            DebugMode.printf("Sorted slopes: %s\n", () -> Arrays.toString(slopes));

            int sameSlopes = 1;
            double firstSlope = slopes[0];
            for (int j = 1; j < slopes.length; j++) {
                if (firstSlope == slopes[j]) {
                    sameSlopes++;
                }
                else {
                    if (sameSlopes >= 3) {
                        addSegment(basePoint, sortedPoints, j - sameSlopes - 1, j - 1);
                    }
                    sameSlopes = 1;
                    firstSlope = slopes[j];
                }
            }
            if (sameSlopes >= 3) {
                addSegment(basePoint, sortedPoints, slopes.length - sameSlopes - 1, slopes.length - 1);
            }
            DebugMode.println();
        }
    }

    private void addSegment(Point basePoint, Point[] sortedPoints, int segmentStart, int segmentEnd) {
        swap(sortedPoints, 0, segmentStart);
        Arrays.sort(sortedPoints, segmentStart, segmentEnd+1);
        if (basePoint.compareTo(sortedPoints[segmentStart]) == 0) {
            LineSegment lineSegment = new LineSegment(basePoint, sortedPoints[segmentEnd]);
            DebugMode.printf("Added a segment: %s\n", () -> lineSegment);
            lineSegments.add(lineSegment);
            swap(sortedPoints, 0, segmentStart);
        }
        else {
            swap(sortedPoints, 0, findBasePointIndex(basePoint, sortedPoints, segmentStart, segmentEnd));
        }
    }

    private int findBasePointIndex(Point basePoint, Point[] sortedPoints, int segmentStart, int segmentEnd) {
        for (int k = segmentStart+1; k <= segmentEnd; k++) {
            if (basePoint.compareTo(sortedPoints[k]) == 0) {
                return k;
            }
        }
        throw new RuntimeException("Should never reach there - is there something wrong with the algorithm?");
    }

    private void validatePoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("Points array should be not null");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException("There should not be a null point");
            }
            for (int j = i+1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Equal points are not allowed");
                }
            }
        }
    }

    private double[] getSortedSlopes(int basePointIndex, Point[] points) {
        Point basePoint = points[basePointIndex];
        double[] slopes = new double[points.length];
        for (int j = 0; j < points.length; j++) {
            double slopeTo = basePoint.slopeTo(points[j]);
            slopes[j] = slopeTo;
        }
        Arrays.sort(slopes);
        return slopes;
    }

    private void swap(Point[] points, int from, int to) {
        Point tmp = points[from];
        points[from] = points[to];
        points[to] = tmp;
    }

    private static class DebugMode {
        private static boolean debugMode = false;

        private static void on() {
            debugMode = true;
        }

        private static void off() {
            debugMode = false;
        }

        private static void printf(String format, Object... args) {
            if (debugMode) StdOut.printf(format, args);
        }

        // TODO think about a more elegant way of converting suppliers to objects
        private static void printf(String format, Supplier... args) {
            if (debugMode) {
                Object[] objects = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    objects[i] = args[i].get();
                }
                StdOut.printf(format, objects);
            }
        }

        private static void println() {
            if (debugMode) StdOut.println();
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
