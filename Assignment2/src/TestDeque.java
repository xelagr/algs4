/*
 * Copyright 2001-2016, Deutsche Bank AG. All Rights Reserved.
 * Confidential and Proprietary Information of Deutsche Bank.
 *
 * @author: Alexey Grishkov
 * Created: 27.02.2016
 */

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestDeque {

    private static int N = 10;
    private Deque<Integer> deque;

    @Before
    public void setUp() {
        deque = new Deque<>();
    }

    @Test
    public void testAddFirstRemoveLast() {
        for (int i = 0; i < N; i++) {
            deque.addFirst(i);
        }

        assertEquals(N, deque.size());
        int j = N;
        for (Integer integer : deque) {
            assertEquals(--j, integer.intValue());
        }

        for (int i = 0; i < N; i++) {
            assertEquals(i, deque.removeLast().intValue());
        }

        assertEquals(0, deque.size());
    }

    @Test
    public void testAddLastRemoveFirst() {
        for (int i = 0; i < N; i++) {
            deque.addLast(i);
        }

        assertEquals(N, deque.size());
        int j = 0;
        for (Integer integer : deque) {
            assertEquals(j++, integer.intValue());
        }

        for (int i = 0; i < N; i++) {
            assertEquals(i, deque.removeFirst().intValue());
        }

        assertEquals(0, deque.size());
    }
}
