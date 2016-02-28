/*
 * Copyright 2001-2016, Deutsche Bank AG. All Rights Reserved.
 * Confidential and Proprietary Information of Deutsche Bank.
 *
 * @author: Alexey Grishkov
 * Created: 27.02.2016
 */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
* A randomized queue is similar to a stack or queue,
* except that the item removed is chosen uniformly at random from items in the data structure.
* */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("An item must be not null");
        }
        if (N == q.length) {
            resize(N * 2);
        }
        q[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        checkEmptyDeque();
        if (N == q.length / 4) {
            resize(q.length / 2);
        }
        int i = StdRandom.uniform(N);
        Item item = q[i];
        q[i] = null;
        swap(i, --N);
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        checkEmptyDeque();
        int i = StdRandom.uniform(N);
        return q[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void swap(int i, int j) {
        Item tmp = q[i];
        q[i] = q[j];
        q[j] = tmp;
    }

    private void resize(int newLength) {
        Item[] oldQueue = q;
        q = (Item[]) new Object[newLength];
        System.arraycopy(oldQueue, 0, q, 0, N);
    }

    private void checkEmptyDeque() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove an item from the empty deque");
        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] q;
        private int N;

        RandomizedQueueIterator() {
            q = (Item[]) new Object[RandomizedQueue.this.N];
            System.arraycopy(RandomizedQueue.this.q, 0, q, 0, RandomizedQueue.this.N);
            StdRandom.shuffle(q);
        }

        @Override
        public boolean hasNext() {
            return N < q.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no more elements in the deque");
            }
            return q[N++];
        }
    }

    // unit testing
    public static void main(String[] args) {

    }
}
