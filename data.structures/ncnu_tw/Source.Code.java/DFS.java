/* Program Name: DFS.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since 2007/03/08
 */
import java.io.*;
interface Bucket {
    void add(int n);
    int remove();
    boolean isEmpty();
}
class Stack implements Bucket {
    int top = 0;
    int[] data = new int[1000];
    public boolean isEmpty() {
        return (top <= 0) ? true : false;
    }
    public void add(int n) {
        data[top++] = n;
    }
    public int remove() {
        return data[--top];
    }
}
class Queue implements Bucket {
    int size, head, tail;
    int[] data = new int[1000];
    public boolean isEmpty() {
        return (size == 0) ? true : false;
    }
    public void add(int n) {
        data[tail++] = n;
        tail %= 1000;
        size++;
    }
    public int remove() {
        int tmp = data[head++];
        head %= 1000;
        size--;
        return tmp;
    }
}
class Node {
   int vertex;
   Node next;
}
public class DFS {
    int n; // number of vertex in this graph
    Node[] graph; // adjacent list for all vertex
    public DFS(int numv) {
        n = numv;
        graph = new Node[n];
    }
    public void traversal(Bucket b) {
        boolean[] visited = new boolean[n];
        b.add(0);
        visited[0] = true;
        while (!b.isEmpty()) {
            int v = b.remove();
            for (Node tt = graph[v]; tt != null; tt = tt.next) {
                if (!visited[tt.vertex]) {
                     System.out.print(tt.vertex+" ");
                     visited[tt.vertex] = true;
                     b.add(tt.vertex);
                }
            }
        }
        System.out.println();
    }
    // add nodes to adjacent list
    public void add(int x, int y) {
        Node tt = new Node();
        tt.vertex = y;
        tt.next = graph[x];
        graph[x] = tt;
        tt = new Node();
        tt.vertex = x;
        tt.next = graph[y];
        graph[y] = tt;
    }
    public static void main(String[] argv) throws Exception {
        String s;
        BufferedReader in = new BufferedReader(new FileReader(argv[0]));
        DFS g = new DFS(Integer.valueOf(in.readLine()));
        while ((s = in.readLine()) != null) {
            int sep = s.indexOf(' ');
            g.add(Integer.valueOf(s.substring(0, sep)), Integer.valueOf(s.substring(sep+1, s.length())));
        }
        g.traversal(new Stack());
        g.traversal(new Queue());
    }
}