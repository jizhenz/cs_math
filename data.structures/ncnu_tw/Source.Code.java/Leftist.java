/**
 * Author: «\¦°ª@, Shiuh-Sheng Yu
 *     Department of Informaton Management
 *     National Chi Nan University
 * Purpose: Min Leftist tree for Priority Queue
 * Since 2009/01/19
 */
public class Leftist {
    class LeftistNode {
        LeftistNode left, right;
        int s;
        Comparable data;
    }
    LeftistNode root;
    public void add(Comparable d) {
        LeftistNode tt = new LeftistNode();
        tt.data = d;
        root = merge(root, tt);
    }
    public Comparable deleteMin() {
        LeftistNode tt = root;
        root = merge(root.right, root.left);
        return tt.data;
    }
    public boolean isEmpty() {
        return root == null;
    }
    public void merge(Leftist x) {
        root = merge(root, x.root);
    }
    private LeftistNode merge(LeftistNode x, LeftistNode y) {
        if (x == null) return y;
        if (y == null) return x;
        LeftistNode tt;
        if (x.data.compareTo(y.data) > 0) { // let x always smaller than y
            tt = x;
            x = y;
            y = tt;
        }
        x.right = merge(x.right, y);
        if (x.left == null || x.left.s < x.right.s) { // oops, rightist
            // so make the tree leftist by exchanging left and right
            tt = x.left;
            x.left = x.right;
            x.right = tt;
        }
        if (x.right == null || x.left == null)
            x.s = 0;
        else
            x.s = (x.left.s > x.right.s) ? x.right.s + 1 : x.left.s + 1;
        return x;
    }
}