/**
 * Program Name: Set.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2008/01/02
 * Purpose: Set find and union
 */
import java.io.*;
public class Set {
    private int[] root;
    public Set(int n) {
        root = new int[n];
        for (int i = 0; i < n; i++) {
            root[i] = -1;
        }
    }
    public int parent(int x) {
        int set1, lead, trail;
        for (set1 = x; root[set1] >= 0; set1 = root[set1]);
        // 順手壓扁此集合
        for (trail = x; trail != set1; trail = lead) {
            lead = root[trail];
            root[trail] = set1;
        }
        return set1;
    }
    /**
     * @return true if x,y in same set, otherwise union this two sets and return false
     */
    public boolean findUnion(int x, int y) {
        int set1 = parent(x);
        int set2 = parent(y);
        if (set1 == set2) return true;
        int setNum = root[set1] + root[set2];
        // 成員比較多的當作新的root
        // 因為用負數代表成員數,因此判斷上要反過來
        if (root[set1] > root[set2]) {
            root[set1] = set2;
            root[set2] = setNum;
        } else {
            root[set2] = set1;
            root[set1] = setNum;
        }
        return false;
    }
}