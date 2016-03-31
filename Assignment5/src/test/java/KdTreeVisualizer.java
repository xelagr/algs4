/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;
import java.util.TreeSet;

public class KdTreeVisualizer {

    public static void main(String[] args) throws InterruptedException {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.show(0);
        Set<Point2D> points = new TreeSet<>();
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.mousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                Point2D p = new Point2D(x, y);
                if (!points.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    if (rect.contains(p)) {
                        StdOut.printf("%8.6f %8.6f\n", x, y);
                        kdtree.insert(p);
                        points.add(p);
                        StdDraw.clear();
                        kdtree.draw();
                        System.out.println(kdtree.size());
                    }
                }
            }
            StdDraw.show(50);
        }

    }
}
