/* Program Name: Deap.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2006/04/17
 * Purpose: Deap data structure
 */
public class Deap {
    int[] deap;
    int n = 1;
    public Deap(int maxSize) {
        deap = new int[maxSize];
    }
    private boolean inMaxHeap(int i) {
        while (i > 3) {
            i /= 2;
        }
        if (i == 2) return false;
        return true;
    }
    private int maxPartner(int pos) {
        int offset = 1;
        int i = pos;
        while (i > 3) {
            i /= 2;
            offset *= 2;
        }
        if ((pos + offset) > n) return (pos+offset)/2;
        return pos + offset;
    }
    private int minPartner(int pos) {
        int offset = 1;
        int i = pos;
        while (i > 3) {
            i /= 2;
            offset *= 2;
        }
        return pos - offset;
    }
    private void minInsert(int at, int key) {
        for (int parent; (parent = at / 2) != 1 && key < deap[parent]; deap[at] = deap[parent], at = parent) ;
        deap[at] = key;
    }
    private void maxInsert(int at, int key) {
        for (int parent; (parent = at / 2) != 1 && key > deap[parent]; deap[at] = deap[parent], at = parent) ;
        deap[at] = key;
    }
    public int deleteMax() {
        int i, j;
        int key;
        if (n >= 3) { // if more than 2 elements
            key = deap[3];
        } else {
            n--;
            return deap[2];
        }
        int x = deap[n--];
        // while i has child, move larger to i
        for (i = 3; 2*i <= n; deap[i] = deap[j], i = j) {
            j = i * 2;
            if (j+1 <= n) {
                if (deap[j] < deap[j+1]) {
                    j++;
                }
            }
        }
        // try to put x at leaf i
        // find biggest at min partner
        j = minPartner(i);
        int biggest = j;
        if (2*j <= n) {
            biggest = 2*j;
            if (((2*j + 1) <= n) && (deap[2*j] < deap[2*j+1])) {
                biggest++;
            }
        }
        if (x < deap[biggest]) {
            // x can't put at i, must change with deap[biggest]
            deap[i] = deap[biggest];
            minInsert(biggest, x);
        } else {
            maxInsert(i, x);
        }
        return key;
    }
    public int deleteMin() {
        int i, j, key = deap[2], x = deap[n--];
        // while i has child, move smaller to i
        for (i = 2; 2*i <= n; deap[i] = deap[j], i = j) {
            j = i * 2;
            if (j+1 <= n && deap[j] > deap[j+1]) {
                j++;
            }
        }
        // try to put x at leaf i
        j = maxPartner(i);
        if (x > deap[j]) {
            deap[i] = deap[j];
            maxInsert(j, x);
        } else {
            minInsert(i, x);
        }
        return key;
    }
    public void insert(int x) {
        n++;
        if (n == deap.length) {
            System.out.println("The heap is full");
            System.exit(1);
        }
        if (n == 2) {
            deap[2] = x;
            return;
        }
        if (inMaxHeap(n)) {
            int i = minPartner(n);
            if (x < deap[i]) {
                deap[n] = deap[i];
                minInsert(i, x);
            } else {
                maxInsert(n, x);
            }
        } else {
            int i = maxPartner(n);
            if (x > deap[i]) {
                deap[n] = deap[i];
                maxInsert(i, x);
            } else {
                minInsert(n, x);
            }
        }
    }
    public void print() {
        int levelNum = 2;
        int thisLevel = 0;
        int gap = 8;
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < gap-1; j++) {
                System.out.print(" ");
            }
            if (thisLevel != 0) {
                for (int j = 0; j < gap-1; j++) {
                    System.out.print(" ");
                }
            }
            if (Integer.toString(deap[i]).length() == 1) {
                System.out.print(" ");
            }
            System.out.print(deap[i]);
            thisLevel++;
            if (thisLevel == levelNum) {
                System.out.println();
                thisLevel = 0;
                levelNum *= 2;
                gap/=2;
            }
        }
        System.out.println();
        if (thisLevel != 0) {
            System.out.println();
        }
    }
    public static void main(String[] argv) {
        Deap a = new Deap(1024);
        int[] data = {4,65,8,9,48,55,10,19,20,30,15,25,50};
        for (int i = 0; i < data.length; i++) { 
            a.insert(data[i]);
        }
        System.out.println("initial");
        a.print();
        System.out.println("delete Min");
        a.deleteMin();
        a.print();
        System.out.println("delete Min");
        a.deleteMin();
        a.print();
        System.out.println("delete Min");
        a.deleteMin();
        a.print();
        System.out.println("delete Max");
        a.deleteMax();
        a.print();
        System.out.println("delete Max");
        a.deleteMax();
        a.print();
        System.out.println("delete Max");
        a.deleteMax();
        a.print();
    }
}