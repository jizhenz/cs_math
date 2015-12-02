/* Program Name: Sort.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2006/03/20
 * Purpose: implementaion of various internal sorts, includes
 * recursive quick sort, stack median of 3 quick sort, merge sort,
 * heap sort, insertion sort, radix sort, counting sort, and shell sort
 */

public class Sort {
    static int[] stack = new int[100]; // remove recursion
    public static void shellSort(int[] data, int n) {
        int i, j, h, v;
        for (h = 1; h <= n/9; h= 3*h +1) ;
        for (; h > 0; h /= 3) {
            for (i = h; i < n; i++) {
                v = data[i]; j = i;
                while (j >= h && data[j-h] > v) {
                    data[j] = data[j-h];
                    j -= h;
                }
                data[j] = v;
            }
        }
    } 
    public static void radixSort(int[] data, int n) {
        // use radix = 256;
        // use counting sort for each round 
        int[] count = new int[257]; // count[i]表示比i小的元素有幾個
        int[] sorted = new int[n];
        // assume only 24 bits used
        // LSD(least significant digit) first
        for (int i = 0; i < 3; i++) {
            int bits = i*8;
            int mask = 0xff<<bits;
            for (int j = 0; j < 257; j++) {
                count[j] = 0;
            }
            for (int j = 0; j < n; j++) {
                int index = (data[j] & mask) >> bits;
                // 目前count[i]表示大小為i-1的元素個數
                count[index+1]++;
            }
            // 累加count
            for (int j = 0; j < 256; j++) {
                count[j+1] += count[j];
            }
            for (int j = 0; j < n; j++) {
                int index = (data[j] & mask) >> bits;
                sorted[count[index]++] = data[j];
            }
            for (int j = 0; j < n; j++) {
                data[j] = sorted[j];
            }
        }
    }
    public static void mergeSort(int[] data) {
        int[] tmp = new int[data.length];
        mergeSort(data, tmp, 0, data.length-1);
    }
    public static void mergeSort(int[] data, int[] tmp, int left, int right) {
        if (left >= right) {
            return;
        }
        int i, j, k;
        int mid = (left+right)/2;
        mergeSort(data, tmp, left, mid);
        mergeSort(data, tmp, mid+1, right);
        for (k = i = left, j = mid+1; i <= mid && j <= right; ) {
            if (data[i] <= data[j]) {
                tmp[k++] = data[i++];
            } else {
                tmp[k++] = data[j++];
            }
        }
        while (i <= mid) {
            tmp[k++] = data[i++];
        }
        while (j <= right) {
            tmp[k++] = data[j++];
        }
        for (i = left; i <= right; i++) {
            data[i] = tmp[i];
        }
    }
    public static void heapSort(int[] data, int n) {
        // bottom up construction
        // 由倒數第二層開始調整 
        for (int k = n/2; k >= 1; k--) {
            // down heap
            int v = data[k-1];
            int parent = k, child;
            for (; parent <= n / 2; parent=child) {
                child = parent<<1;
                // 如果有右子樹且比左子樹大的話,選擇右子樹
                if (child < n && data[child-1] < data[child]) {
                    child++;
                }
                if (v >= data[child-1]) break;
                data[parent-1] = data[child-1];
            }
            data[parent-1] = v;
        }
        // sorting
        for (; n > 1;) {
            int v = data[n-1];
            data[--n] = data[0];
            int parent = 1, child;
            for (; parent <= n / 2; parent=child) {
                child = parent<<1;
                // 如果有右子樹且比左子樹大的話,選擇右子樹
                if (child < n && data[child-1] < data[child]) {
                    child++;
                }
                if (v >= data[child-1]) break;
                data[parent-1] = data[child-1];
            }
            data[parent-1] = v;
        }
    }
    public static void quickSort2(int[] data, int l, int r) {
        int i, j, tmp, v;
        if (r > l) {
            v = data[l];
            i = l;
            j = r + 1;
            for (;;) {
                while (i<r && data[++i] < v) ; // 假設data[r+1]一定 >= v
                while (data[--j] > v) ; // data[l] 一定 == v
                if (i >= j) break;
                tmp = data[i];
                data[i] = data[j];
                data[j] = tmp;
            }
            // data[j] <= v
            tmp = data[j];
            data[j] = data[l];
            data[l] = tmp;
            quickSort2(data, l, j-1);
            quickSort2(data, j+1, r);
        }
    }
    public static void check(int[] data) {
        for (int i = 1; i < data.length; i++) {
            if (data[i-1] > data[i]) {
                System.out.println("error");
                return;
            }
        }
    }
    public static void insertionSort(int[] data, int n) {
        for (int j = 1; j < n; j++) {
            int tmp = data[j];
            int k = j-1;
            for (; k >= 0 && data[k] > tmp; k--) {
                data[k+1] = data[k];
            }
            data[k+1] = tmp;
        }
    }
    public static void quickSort(int[] data, int n) {
        int top = 0;
        int l = 0;
        int r = n - 1;
        int tmp, mid;
        for (;;) {
            while (r - l >= 16) {
                // use median of three
                mid = (l+r) / 2;
                // sort 3 elements
                if (data[l] > data[r]) {
                    tmp = data[l];
                    data[l] = data[r];
                    data[r] = tmp;
                }
                if (data[l] > data[mid]) {
                    tmp = data[l];
                    data[l] = data[mid];
                    data[mid] = tmp;
                }
                if (data[mid] > data[r]) {
                    tmp = data[mid];
                    data[mid] = data[r];
                    data[r] = tmp;
                }
                // swap mid with r - 1
                int i = l, j = r - 1;
                tmp = data[mid];
                data[mid] = data[j];
                data[j] = tmp;
                // data[l]<=tmp, data[r]>=tmp,data[r-1]=tmp,
                // swap l+1 <==> r - 2
                for (;;) {
                    while (data[++i] < tmp) ;
                    while (data[--j] > tmp) ;
                    if (i >= j) break;
                    int kk = data[i];
                    data[i] = data[j];
                    data[j] = kk;
                }
                // data[i] >= tmp(data[mid])
                // data[j] <= tmp
                if (i == j) {
                    i++; j--;
                }
                if (j - l > r - i) { // push left(larger) part to stack
                    stack[top++] = l;
                    stack[top++] = j;
                    l = i;
                } else {
                    stack[top++] = i;
                    stack[top++] = r;
                    r = j;
                }
            }
            if (top == 0) break;
            r = stack[--top];
            l = stack[--top];
        }
        insertionSort(data, n);
    }
    public static void main(String[] argv) {
        long start, stop;
        int testSize = 1000000;
        int[] data1 =  new int[testSize];
        int[] data2 = new int[testSize];
        int[] data3 = new int[testSize];
        int[] data4 = new int[testSize];
        int[] data5 = new int[testSize];
        int[] data6 = new int[testSize];
        for (int i = 0; i < testSize; i++) {
            data6[i] = data5[i] = data4[i] = data3[i] = data2[i] = data1[i] = (int)(Math.random()*10000000);
        }
        start = System.currentTimeMillis();
        mergeSort(data1);
        stop = System.currentTimeMillis();
        System.out.println("merge sort use "+(stop-start));
        check(data1);
        start = System.currentTimeMillis();
        radixSort(data2, testSize);
        stop = System.currentTimeMillis();
        System.out.println("radix sort use "+(stop-start));
        check(data2);
        start = System.currentTimeMillis();
        quickSort2(data3, 0, testSize-1);
        stop = System.currentTimeMillis();
        System.out.println("recursive quick sort use "+(stop-start));
        check(data3);
        start = System.currentTimeMillis();
        quickSort(data4, testSize);
        stop = System.currentTimeMillis();
        System.out.println("median3 quick sort use "+(stop-start)+"ms");
        check(data4);
        start = System.currentTimeMillis();
        heapSort(data5, testSize);
        stop = System.currentTimeMillis();
        System.out.println("heap sorted use "+(stop-start)+"ms");
        check(data5);
        System.out.flush();
        start = System.currentTimeMillis();
        shellSort(data6, testSize);
        stop = System.currentTimeMillis();
        System.out.println("shell random use "+(stop-start)+"ms");
        check(data6);
    }
}