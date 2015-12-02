/**
 * Program Name: MazeShort.java
 * Author: 俞旭昇Shiuh-Sheng Yu
 *         Department of Information Management
 *         National Chi Nan University
 * Purpose: Use Queue to find the shortest path between two points in a maze
 * Since 2008/10/31
 */
import java.io.*;
import java.util.*;
public class MazeShort {
    int row, col;
    int[] data; // using 1D array to represent Maze
    public MazeShort(int r, int c, int[] d) {
        row = r;
        col = c;
        // data = surround d with 1
        data = new int[(r+2)*(c+2)];
        for (int i = 0, j = (r+1)*(c+2); i < c + 2; i++, j++) {
            data[i] = 1; // top 1's
            data[j] = 1; // botton 1's
        }
        for (int i = c+2, j=2*c+3; i < (r+1)*(c+2); i += c+2, j+= c+2) {
            data[i] = 1; // left 1's
            data[j] = 1; // right 1's
        }
        // copy original data
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                data[(i+1)*(c+2)+j+1] = d[i*c+j];
            }
        }
    }
    public void print() {
        for (int i = 0; i < row + 2; i++) {
            for (int j = 0; j < col + 2; j++) {
                System.out.print(data[i*(col+2)+j]+" ");
            }
            System.out.println();
        }
    }
    private void clear() {
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 1) data[i] = 0;
        }
    }
    public void findShortest(int sx, int sy, int ex, int ey) {
        int start, stop, check, next;
        start = (sx+1)*(col+2) + sy + 1;
        stop = (ex+1)*(col+2) + ey + 1;
        Queue q = new Queue(row*col);
        q.add(start);
        data[start] = -1; // mark visited
        int[] dir = {-(col+2), col+2, -1, 1}; // setup 4 directions
        while (!q.isEmpty()) {
            if ((check = q.remove()) == stop) {
                int len = printPath(stop);
                System.out.println("\nPath length="+len);
                clear(); // restore original data, to prevent potential errors in the future
                return;
            }
            for (int i = 0; i < dir.length; i++) {
                if (data[next = check+dir[i]] == 0) { // 沒檢查過
                    data[next] = check; // 記錄從那來, 兼標示此點檢查過
                    q.add(next);
                }
            }
        }
        System.out.println("not found");
    }
    private int printPath(int pos) {
        int len = 0;
        if (data[pos] > 0) { // 有上一步
            len = printPath(data[pos]) + 1; // 先印出上一步
            System.out.print("->");
        }
        System.out.print("("+(pos/(col+2)-1)+ "," + (pos%(col+2)-1)+")");
        return len;
    }
    public static void main(String[] argv) throws Exception {
        Scanner s = new Scanner(new FileInputStream(argv[0]));
        int r, c, sx, sy, ex, ey;
        r = s.nextInt();
        c = s.nextInt();
        sx = s.nextInt();
        sy = s.nextInt();
        ex = s.nextInt();
        ey = s.nextInt();
        int[] d = new int[r*c];
        for (int i = 0; i < r*c; i++) {
            d[i] = s.nextInt();
        }
        MazeShort ms = new MazeShort(r, c, d);
        ms.print();
        ms.findShortest(sx, sy, ex, ey);
    }
}
class Queue {
    private int head, tail, size;
    private int[] data;
    public Queue(int max) {
        data = new int[max];
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public void add(int x) {
        data[tail++] = x;
        tail %= data.length;
        size++;
    }
    public int remove() {
        int tmp = data[head++];
        head %= data.length;
        size--;
        return tmp;
    }
}