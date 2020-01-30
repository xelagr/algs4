import edu.princeton.cs.algs4.StdOut;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by Aleksei Grishkov on 28.02.2016.
 */
public class RandomizedQueueTest {

    private int N = 10;
    private RandomizedQueue<Integer> queue;

    @Before
    public void setUp() {
        queue = new RandomizedQueue<>();
        for (int i = 0; i < N; i++) {
            queue.enqueue(i);
        }
    }

    @Test
    public void basicTest() {
        for (int i = 0; i < N; i++) {
            StdOut.printf("%d ", queue.dequeue());
        }
        assertEquals(0, queue.size());
    }

    @Test
    public void testIterator() {
        for (int integer : queue) {
            StdOut.printf("%d ", integer);
            for (int integer2 : queue) {
                StdOut.printf("%d ", integer2);
            }
        }
        assertEquals(N, queue.size());
        StdOut.println();
        for (int integer : queue) {
            StdOut.printf("%d ", integer);
        }
    }
}
