package uf;

import java.util.Arrays;

public class QuickUnion implements UF{
    private int[] id;

    public QuickUnion(int n) {
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    @Override
    public void union(int p, int q) {
        p = root(p);
        q = root(q);
        id[p] = q;
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    private int root(int i) {
        while(i != id[i]) i = id[i];
        return i;
    }

    @Override
    public String toString() {
        return Arrays.toString(id);
    }
}
