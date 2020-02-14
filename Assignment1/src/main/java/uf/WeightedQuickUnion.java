package uf;

import java.util.Arrays;

public class WeightedQuickUnion implements UF{
    private int[] id;
    private int[] sz;

    public WeightedQuickUnion(int n) {
        id = new int[n];
        sz = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    @Override
    public void union(int p, int q) {
        p = root(p);
        q = root(q);
        if (p == q) return;
        if (sz[p] < sz[q]) {
            id[p] = q;
            sz[q] += sz[p];
        }
        else {
            id[q] = p;
            sz[p] += sz[q];
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    private int root(int i) {
        while(i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    @Override
    public String toString() {
        return "sz: " + Arrays.toString(sz) + ", id: " + Arrays.toString(id);
    }
}
