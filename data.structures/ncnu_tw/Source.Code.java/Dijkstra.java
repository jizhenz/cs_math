/* Program Name: Dijkstra.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2006/05/03
 * Purpose: Single source all destination shortest path
 * using adjacent lists and Fibonacci Heap
 */
public class Dijkstra {
    class Node {
        int vertex;
        int cost;
        Node next;
    }
    int n; // number of vertex in this graph
    Node[] edge; // adjacent list for all vertex
    public Dijkstra(int vertex) {
        n = vertex;
        edge = new Node[n];
    }
    public void add(int x, int y, int cost) {
        // add node to adjacent list
        Node tt = new Node();
        tt.vertex = y;
        tt.cost = cost;
        tt.next = edge[x];
        edge[x] = tt;
    }
    public int[] shortestPath() {
        int[] distance = new int[n];
        FHeapNode[] fnode = new FHeapNode[n];
        for (int i = 1; i < n; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        FHeap heap = new FHeap();
        fnode[0] = heap.insert(0, 0);
        int w, newLen;
        while ((w = heap.deleteMin()) >= 0) {
            fnode[w] = null; // release memory
            for (Node tmp = edge[w]; tmp != null; tmp = tmp.next) {
                if ((newLen = distance[w] + tmp.cost) < distance[tmp.vertex]) {
                    distance[tmp.vertex] = newLen;
                    if (fnode[tmp.vertex] == null) {
                        fnode[tmp.vertex] = heap.insert(newLen, tmp.vertex);
                    } else {
                        heap.decrease(fnode[tmp.vertex], newLen);
                    }
                }
            }
            edge[w] = null; // release memory
        }
        return distance;
    }
    public static Dijkstra genGraph(int n, int density) {
        int link = n * density / 100;
        Dijkstra g = new Dijkstra(n);
        for (int i = 0; i < n; i++) {
            for (int j = link; j > 0; ) {
                int next = (int)(Math.random()*n); // 0 ~ n -1
                int cost = (int)(Math.random()*1000) + 1; // cost
                if (next != i) {
                    g.add(i, next, cost);
                    j--;
                }
            }
        }
        return g;
    }
    public static void main(String[] argv) {
        int[][] data = {{0, 4, 55},{0, 1, 50},{0, 2, 10},
            {1, 4, 10}, {1, 2, 15},
            {2, 0, 20}, {2, 3, 15},
            {3, 1, 15}, {3, 4, 35}, {3, 5, 10}};
        Dijkstra g = new Dijkstra(6);
        for (int i = 0; i < data.length; i++) {
            g.add(data[i][0], data[i][1], data[i][2]);
        }
        int[] shortest = g.shortestPath();
        for (int i = 1; i < shortest.length; i++) {
            System.out.println("distance to "+i+" = "+shortest[i]);
        }
        for (int n = 1000; n <= 3000; n+=1000) {
            for (int den = 10; den <= 50; den+=10) {
                g = genGraph(n, den);
                long start = System.currentTimeMillis();
                g.shortestPath();
                long stop = System.currentTimeMillis();
                System.out.println("n="+n+",den="+den+", time="+(stop-start)+"ms");
            }
        }
    }
}