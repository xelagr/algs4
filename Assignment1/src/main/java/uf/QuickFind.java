package uf;

import java.util.Arrays;

public class QuickFind implements UF {
    private int[] id;

    public QuickFind(int n) {
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    @Override
    // complexity O(N)
    public void union(int p, int q) {
        final int pid = id[p];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = id[q];
            }
        }
    }

    @Override
    // complexity O(1)
    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    @Override
    public String toString() {
        return Arrays.toString(id);
    }
}
