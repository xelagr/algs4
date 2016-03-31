import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A mutable data type that represents a set of points in an unit square.
 * The implementation uses 2d-tree.
 * <p>
 * Created by Aleksei Grishkov on 28.03.2016.
 */
public class KdTree {

    private Node root;
    private final RectHV rootRect;
    private int size;

    // construct an empty set of points
    public KdTree() {
        rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkForNull(p, "A point cannot be null");
        root = insert(root,  p, rootRect, true);
    }

    private Node insert(Node curNode, Point2D p, RectHV rect, boolean orientationByX) {
        if (curNode == null) {
            size++;
            return new Node(p, rect);
        }
        Point2D curPoint = curNode.point;
        int cmp = orientationByX ? Double.compare(p.x(), curPoint.x()) : Double.compare(p.y(), curPoint.y());
        if (cmp < 0) curNode.lb = insert(curNode.lb, p, lbRect(orientationByX, curPoint, rect), !orientationByX);
        else if (cmp > 0 || (cmp == 0 && !curPoint.equals(p)))
            curNode.rt = insert(curNode.rt, p, rtRect(orientationByX, curPoint, rect), !orientationByX);
        return curNode;
    }

    private RectHV lbRect(boolean orientationByX, Point2D p, RectHV rect) {
        return orientationByX ? getLeftRect(p, rect) : getBottomRect(p, rect);
    }

    private RectHV rtRect(boolean orientationByX, Point2D p, RectHV rect) {
        return orientationByX ? getRightRect(p, rect) : getTopRect(p, rect);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkForNull(p, "A point cannot be null");
        return contains(root, p, true);
    }

    private boolean contains(Node current, Point2D p, boolean divideByX) {
        if (current == null) return false;
        int cmp = divideByX ? Double.compare(p.x(), current.point.x()) : Double.compare(p.y(), current.point.y());
        if (cmp < 0) return contains(current.lb, p, !divideByX);
        else if (cmp > 0) return contains(current.rt, p, !divideByX);
        else return current.point.equals(p) || contains(current.rt, p, !divideByX);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    private void draw(Node curNode, boolean divideByX) {
        if (curNode == null) return;
        Point2D curPoint = curNode.point;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(curPoint.x(), curPoint.y());

        StdDraw.setPenRadius();
        RectHV rect = curNode.rect;
        if (divideByX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(curPoint.x(), rect.ymin(), curPoint.x(), rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), curPoint.y(), rect.xmax(), curPoint.y());
        }
        draw(curNode.lb, !divideByX);
        draw(curNode.rt, !divideByX);
    }

    private RectHV getLeftRect(Point2D p, RectHV rect) {
        return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
    }

    private RectHV getRightRect(Point2D p, RectHV rect) {
        return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
    }

    private RectHV getBottomRect(Point2D p, RectHV rect) {
        return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
    }

    private RectHV getTopRect(Point2D p, RectHV rect) {
        return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
    }

    private List<Point2D> getPoints() {
        List<Point2D> points = new ArrayList<>();
        fillPoints(root, points);
        return points;
    }

    private void fillPoints(Node curNode, List<Point2D> points) {
        if (curNode == null) return;
        points.add(curNode.point);
        fillPoints(curNode.lb, points);
        fillPoints(curNode.rt, points);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        checkForNull(rect, "A rectangle cannot be null");
        return root != null ? range(root, rect, new ArrayList<>()) : new ArrayList<>();
    }

    private List<Point2D> range(Node node, RectHV rect, List<Point2D> points) {
        if (rect.contains(node.point)) points.add(node.point);
        if (node.lb != null && rect.intersects(node.lb.rect)) range(node.lb, rect, points);
        if (node.rt != null && rect.intersects(node.rt.rect)) range(node.rt, rect, points);
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkForNull(p, "A point cannot be null");
        return nearest(p, root, true, 1.0, root.point);
    }

    private Point2D nearest(Point2D p, Node node, boolean divideByX, double minDist, Point2D champion) {
        if (node != null) {
            double dist = node.point.distanceTo(p);
            if (dist < minDist) {
                minDist = dist;
                champion = node.point;
            }
            int cmp = divideByX ? Double.compare(p.x(), node.point.x()) : Double.compare(p.y(), node.point.y());
            if (cmp < 0) {
                nearest(p, node.lb, !divideByX, minDist, champion);
                if (node.rt.rect.distanceTo(p) < minDist) {
                    nearest(p, node.rt, !divideByX, minDist, champion);
                }
            }
            else {
                nearest(p, node.rt, !divideByX, minDist, champion);
                if (node.lb.rect.distanceTo(p) < minDist) {
                    nearest(p, node.lb, !divideByX, minDist, champion);
                }
            }
        }
        return champion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        List<Point2D> pointList = new ArrayList<>();
        pointList.add(new Point2D(0.5, 0.2));
        pointList.add(new Point2D(0.5, 0.2));
        pointList.add(new Point2D(0.5, 0.1));
        pointList.add(new Point2D(0.2, 0.5));
        pointList.add(new Point2D(0.1, 0.3));
        pointList.add(new Point2D(0.9, 0.9));

        HashSet<Point2D> pointSet = new HashSet<>(pointList);

        for (Point2D point : pointList) {
            kdTree.insert(point);
            StdOut.printf("Inserted: %8.6f %8.6f\n", point.x(), point.y());
        }
        System.out.println();

        assert kdTree.size() == pointSet.size() : String.format("kdTree.size(%d) != pointSet.size(%d)", kdTree.size, pointSet.size());
        List<Point2D> points = kdTree.getPoints();
        assert points.size() == pointSet.size() : String.format("kdTree.size(%d) != pointSet.size(%d)", kdTree.size, pointSet.size());
        for (Point2D point : points) {
            StdOut.printf("Contains: %8.6f %8.6f\n", point.x(), point.y());
        }
        System.out.println();

        for (Point2D point : pointSet) {
            assert kdTree.contains(point);
            StdOut.printf("Found: %8.6f %8.6f\n", point.x(), point.y());
        }

        Point2D point = new Point2D(0.5, 0.3);
        pointSet.add(point);
        assert !kdTree.contains(point) : String.format("kdTree contains point %s that was not added to the tree", point);
    }

    private class Node {
        private Point2D point;  // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node

        Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    private void checkForNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }
}
