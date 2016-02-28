/*
 * Copyright 2001-2016, Deutsche Bank AG. All Rights Reserved.
 * Confidential and Proprietary Information of Deutsche Bank.
 *
 * @author: Alexey Grishkov
 * Created: 27.02.2016
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
* A double-ended queue or deque is a generalization of a stack and a queue that supports
* adding and removing items from either the front or the back of the data structure
* */
public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkNullItem(item);
        Node oldFirst = first;
        first = new Node(item, null, oldFirst);
        if (oldFirst != null) {
            oldFirst.prev = first;
        }
        if (isEmpty()) {
            last = first;
        }
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        checkNullItem(item);
        Node oldLast = last;
        last = new Node(item, oldLast, null);
        if (oldLast != null) {
            oldLast.next = last;
        }
        if (isEmpty()) {
            first = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkEmptyDequeOnRemoval();
        Node oldFirst = first;
        first = oldFirst.next;
        if (first != null) {
            first.prev = null;
        } else {
            last = null;
        }
        size--;
        return oldFirst.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkEmptyDequeOnRemoval();
        Node oldLast = last;
        last = oldLast.prev;
        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }

        size--;
        return oldLast.item;
    }

    private void checkNullItem(Item item) {
        if (item == null) {
            throw new NullPointerException("An item must be not null");
        }
    }

    private void checkEmptyDequeOnRemoval() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove an item from the empty deque");
        }
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no more elements in the deque");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private class Node {
        private Item item;
        private Node prev;
        private Node next;

        Node(Item item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    // unit testing
    public static void main(String[] args) {

    }

}
