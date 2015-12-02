/* Program Name: Prim.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2007/03/24
 * Purpose: Demo Prim's algorithm for minimun spanning tree
 */
import java.io.*;
public class Prim {
    int n; // number of vertex in this graph
    Edge[] adjList;
    public Prim(int vertex) {
        n = vertex;
        adjList = new Edge[n];
    }
    public void add(int x, int y, int cost) {
        // add to adjacent list
        Edge e1 = new Edge(x,y,cost,adjList[x]);
        adjList[x] = e1;
        Edge e2 = new Edge(x,y,cost,adjList[y]);
        adjList[y] = e2;
    }
    public void span() { //using Prim's algorithm
        // 利用MinHeap來排序
        MinHeap heap = new MinHeap();
        // 設定boolean array記錄那些點已經在TV裡
        boolean[] TV = new boolean[n];
        // 先加入第一個點
        TV[0] = true;
        int newNode = 0, found = 0, totalCost = 0;
        while (found < n - 1) {
            // 檢查剛加入點的所有邊
            for (Edge tt = adjList[newNode]; tt != null; tt = tt.next)
                if (!(TV[tt.x] && TV[tt.y])) heap.add(tt); // 如果某一端不在TV內
            Edge min;
            do {
                min = (Edge)heap.deleteMin();
            } while (min != null && TV[min.x] && TV[min.y]);
            if (min == null) break;
            found++;
            totalCost += min.cost;
            newNode = min.x;
            if (TV[min.x]) newNode = min.y;
            TV[newNode] = true;
            System.out.println(min.x+", "+min.y+", "+min.cost+" added");
        }
        if (found == n - 1)
            System.out.println("total cost = "+totalCost);
        else
            System.out.println("No spanning tree exists");
    }
    public static void main(String[] argv) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(argv[0]));
        Prim g = new Prim(Integer.parseInt(in.readLine()));
        String s;
        while ((s = in.readLine()) != null) {
            int sep1 = s.indexOf(" ");
            int sep2 = s.indexOf(" ", sep1 + 1);
            g.add(Integer.parseInt(s.substring(0, sep1)), Integer.parseInt(s.substring(sep1+1,sep2)), Integer.parseInt(s.substring(sep2+1,s.length())));
        }
        g.span();
    }
}