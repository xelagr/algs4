import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Aleksei Grishkov on 28.02.2016.
 */
public class Subset {

    public static void main(String[] args) {
        if (args.length != 1) {
            StdOut.println("Usage: Subset <number>");
            return;
        }
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}