/* Program Name: Kruskal.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2006/02/20
 * Purpose: Demo Kruskal's algorithm for minimun spanning tree
 */
import java.io.*;
public class Kruskal {
    int n; // number of vertex in this graph
    int nE; // number of edges
    Edge[] edges = new Edge[100]; // array of all edges
    public Kruskal(int vertex) {
        n = vertex;
    }
    public void add(int x, int y, int cost) {
        // add to all edges list
        Edge tmp = new Edge();
        tmp.x = x;
        tmp.y = y;
        tmp.cost = cost;
        if (nE >= edges.length) {
            Edge[] kk = new Edge[edges.length*2];
            System.arraycopy(edges,0,kk,0,nE+1);
            edges = kk;
        }
        edges[nE++] = tmp;
    }
    public void span() { //using Kruskal's algorithm
        // 利用MinHeap來排序
        MinHeap heap = new MinHeap(edges, nE);
        // 設定每一個節點為獨立的Set
        Set s = new Set(n);
        int totalCost = 0;
        // n個節點的minimum spannig tree有n-1個edge
        for (int found = 0; found < n - 1;) {
            Edge tmp = (Edge)heap.delete();
            if (tmp == null) {
                System.out.println("not connected");
                return;
            }
            if (!s.findUnion(tmp.x,tmp.y)) { // 加入此edge不會造成cycle
                totalCost += tmp.cost;
                found++;
                System.out.println("add "+tmp.x+", "+tmp.y+", "+tmp.cost);
                int setNum = root[set1] + root[set2];
            } else
                System.out.println("discard "+tmp.x+", "+tmp.y+", "+tmp.cost);
        }
        System.out.println("total cost = "+totalCost);
    }
    public static void main(String[] argv) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(argv[0]));
        Kruskal g = new Kruskal(Integer.parseInt(in.readLine()));
        String s;
        while ((s = in.readLine()) != null) {
            int sep1 = s.indexOf(" ");
            int sep2 = s.indexOf(" ", sep1 + 1);
            g.add(Integer.parseInt(s.substring(0, sep1)), Integer.parseInt(s.substring(sep1+1,sep2)), Integer.parseInt(s.substring(sep2+1,s.length())));
        }
        g.span();
   }
}