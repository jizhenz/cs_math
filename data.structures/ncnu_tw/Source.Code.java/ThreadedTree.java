/**
 * Program: ThreadedTree.java
 * Author: Shiuh-Sheng Yu
 * Since: 2006/12/16
 * Purpose: Data Structure for Threaded Binary Search Tree
 */
public class ThreadedTree {
    class Node {
        int key;
        Node left, right;
        boolean leftThread, rightThread;
    }
    Node root;
    public ThreadedTree () {
        root = new Node();
        root.right = root.left = root;
        root.leftThread = true;
        root.key = Integer.MAX_VALUE;
    }
    public Node search(int key) {
        Node tmp = root.left;
        for (;;) {
            if (tmp.key < key) {
                if (tmp.rightThread) return null;
                tmp = tmp.right;
            } else if (tmp.key > key) {
                if (tmp.leftThread) return null;
                tmp = tmp.left;
            } else {
                return tmp;
            }
        }
    }
    public void delete(int key) {
        Node dest = root.left, p = root;
        for (;;) {
            if (dest.key < key) {
                if (dest.rightThread) return; // not found
                p = dest;
                dest = dest.right;
            } else if (dest.key > key) {
                if (dest.leftThread) return; // not found
                p = dest;
                dest = dest.left;
            } else {
                break; // bingo
            }
        }
        Node target = dest;
        if (!dest.rightThread && !dest.leftThread) { // dest has two children
            p = dest;
            // find largest node at left child
            target = dest.left;
            while (!target.rightThread) {
                p = target;
                target = target.right;
            }
            dest.key = target.key; // using replace mode
        }
        if (p.key >= target.key) {
            if (target.rightThread && target.leftThread) {
                //   p
                //  /
                //  t
                p.left = target.left;
                p.leftThread = true;
            } else if (target.rightThread) {
                //      p
                //     /
                //    t
                //   /
                // t.left
                Node largest = target.left;
                while (!largest.rightThread) {
                    largest = largest.right;
                }
                largest.right = p;
                p.left = target.left;
            } else {
                //      p
                //     /
                //    t
                //     \
                //     t.right
                Node smallest = target.right;
                while (!smallest.leftThread) {
                    smallest = smallest.left;
                }
                smallest.left = target.left;
                p.left = target.right;
            }
        } else {
            if (target.rightThread && target.leftThread) {
                //   p
                //    \
                //     t
                p.right = target.right;
                p.rightThread = true;
            } else if (target.rightThread) {
                //   p
                //    \
                //    t
                //   /
                // t.left
                Node largest = target.left;
                while (!largest.rightThread) {
                    largest = largest.right;
                }
                largest.right =  target.right;
                p.right = target.left;
            } else {
                //   p
                //    \
                //    t
                //     \
                //   t.right
                Node smallest = target.right;
                while (!smallest.leftThread) {
                    smallest = smallest.left;
                }
                smallest.left = p;
                p.right = target.right;
            }
        }
    }
    public void insert(int key) {
        Node p = root;
        for (;;) {
            if (p.key < key) {
                if (p.rightThread) break;
                p = p.right;
            } else if (p.key > key) {
                if (p.leftThread) break;
                p = p.left;
            } else {
                return; // redundant key
            }
        }
        Node tmp = new Node();
        tmp.key = key;
        tmp.rightThread = tmp.leftThread = true;
        if (p.key < key) { // insert to right side
            tmp.right = p.right;
            tmp.left = p;
            p.right = tmp;
            p.rightThread = false;
        } else {
            tmp.right = p;
            tmp.left = p.left;
            p.left = tmp;
            p.leftThread = false;
        }
    }
    public void print() {
        Node tmp = root.left;
        if (tmp == root) return;
        print(tmp);
        System.out.println(")");
    }
    private void print(Node tmp) {
        System.out.print("("+tmp.key);
        if (!tmp.leftThread) print(tmp.left);
        if (!tmp.leftThread && !tmp.rightThread) System.out.print(",");
        if (!tmp.rightThread) print(tmp.right);
        System.out.print(")");
    }
    public void list() {
        Node tmp = root, p;
        for (;;) {
            p = tmp;
            tmp = tmp.right;
            if (!p.rightThread) {
                while (!tmp.leftThread) {
                    tmp = tmp.left;
                }
            }
            if (tmp == root) break;
            System.out.print(tmp.key+" ");
        }
        System.out.println();
    }
    public static void main(String[] argv) {
        ThreadedTree t = new ThreadedTree();
        t.insert(5);
        t.insert(3);
        t.insert(1);
        t.insert(7);
        t.insert(9);
        t.insert(4);
        t.insert(2);
        t.insert(6);
        t.insert(8);
        t.print();
        t.delete(5);
        t.delete(7);
        t.print();
    }
}