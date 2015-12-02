/**
 * A heap is a complete binary tree, so we can use an array to represent the tree.
 * The number of root is 1, and left child of root is 2, the right child of root is 3.
 * For node i, 2*i and 2*i+1 are its left and right child respectively.
 * A min heap is a heap with root be the smallest of the tree.
 * For example:
 *         B(1)
 *      /     \
 *     E(2)    H(3)
 *   /  \     /  \
 * G(4) F(5) J(6) K(7)
 * This implementation use 0..n-1 instead of 1..n, so you must read the code carefully.
 */
public class MinHeap {
    Comparable[] data; // Comparable is an interface defined in java.lang
    private int size;
    public MinHeap() {
        data = new Comparable[100];
    }
    /**
     * @param data array of references to Comparable objects.
     * @param n the number of valid ojbects in data
     */
    public MinHeap(Comparable[] data, int n) {
        this.data = data;
        size = n;
        adjust(); // bottom up construction
    }
    public void merge(MinHeap x) {
        if (x == null || x.size == 0) return;
        if (size + x.size > data.length) {
            Comparable[] tmp = new Comparable[size+x.size];
            System.arraycopy(data, 0, tmp, 0, size);
            data = tmp;
        }
        System.arraycopy(x.data, 0, data, size, x.size);
        size += x.size;
        adjust();
    }
    private void adjust() {
        // �ѭ˼ƲĤG�h�}�l�վ�
        for (int k = size/2; k >= 1; k--) {
            // down heap
            Comparable v = data[k-1]; // the last element to remove
            int parent = k, child;
            for (; parent <= size / 2; parent=child) {
                child = parent<<1;
                // �p�G���k�l��B�񥪤l��p����,��ܥk�l��
                if (child < size && data[child-1].compareTo(data[child])>0) {
                    child++;
                }
                if (v.compareTo(data[child-1]) <= 0) break;
                data[parent-1] = data[child-1];
            }
            data[parent-1] = v;
        }
    }
    public void add(Comparable e) {
        if (size >= data.length) { // expand array size
            Comparable[] tmp = new Comparable[size*2]; // double the capacity
            System.arraycopy(data, 0, tmp, 0, size);
            data = tmp;
        }
        int check = ++size; // check is the position to insert
        int parent = check/2;
        while (parent > 0) {
            if (e.compareTo(data[parent-1]) >= 0) break; // parent is smaller, so stop here
            data[check-1] = data[parent-1]; // otherwise, move parent down
            check = parent; // look up higher level
            parent /= 2;
        }
        data[check-1] = e;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public Comparable deleteMin() {
        if (size < 1) { // HEAP�w�g�ŤF
            return null;
        }
        // ���Xroot
        Comparable rel = data[0];
        Comparable v = data[--size]; // try to remove the last element
        // ��root���U���leaf�Τ����洫����
        int parent = 1, child;
        for (; parent <= size/2; parent=child) {
            child = parent<<1;
            // child��parent�����l��
            // �p�G���k�l��,�B�񥪤l��p,�]�wsmall���k�l��
            if (child < size && data[child-1].compareTo(data[child]) > 0) {
                child++;
            }
            if (v.compareTo(data[child-1]) <= 0) break; // v is smaller than child, then stop here
            // �p�G���`�I��l�`�I�j,�h�ݭn�洫
            data[parent-1] = data[child-1];
        }
        data[parent-1] = v;
        return rel;
    }
}