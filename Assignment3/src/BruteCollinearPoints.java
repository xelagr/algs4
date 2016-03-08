/**
 * Created by alex on 07.03.16.
 */
public class BruteCollinearPoints {

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
    }

    // the number of line segments
    public int numberOfSegments() {
        return 0;
    }

    // the line segments
    public LineSegment[] segments() {
        return null;
    }
}
