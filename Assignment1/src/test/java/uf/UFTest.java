package uf;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class UFTest {

    @Test
    public void testQuickFind8() {
       test8(new QuickFind(8));
    }

    @Test
    public void testQuickUnion8() {
        test8(new QuickUnion(8));
    }

    @Test
    public void testQuickUnion10() {
        test10(new QuickUnion(10));
    }

    @Test
    public void testWeightedQuickUnion10() {
        test10(new WeightedQuickUnion(10));
    }

    private void test10(UF uf) {
        uf.union(4, 3);
        uf.union(3, 8);
        uf.union(6, 5);
        uf.union(9, 4);
        assertTrue(uf.connected(3, 9));
        uf.union(2, 1);
        uf.union(5, 0);
        assertTrue(uf.connected(6, 0));
        uf.union(7, 2);
        assertTrue(uf.connected(1, 7));
        assertFalse(uf.connected(1, 3));
        assertFalse(uf.connected(3, 0));
        uf.union(6, 1);
        uf.union(7, 3);
        assertTrue(uf.connected(1, 3));
        assertTrue(uf.connected(3, 0));
        System.out.println(uf);
    }

    private void test8(UF uf) {
        uf.union(1, 4);
        uf.union(4, 5);
        uf.union(2, 3);
        uf.union(2, 6);
        uf.union(3, 6);
        uf.union(3, 7);

        assertTrue(uf.connected(1, 5));
        assertTrue(uf.connected(2, 7));
        assertFalse(uf.connected(0, 4));
        assertFalse(uf.connected(1, 7));
        System.out.println(uf);

        uf.union(2, 5);
        assertTrue(uf.connected(5, 7));
        System.out.println(uf);
    }
}
