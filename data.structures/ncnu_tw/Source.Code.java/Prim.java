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
        // �Q��MinHeap�ӱƧ�
        MinHeap heap = new MinHeap();
        // �]�wboolean array�O�������I�w�g�bTV��
        boolean[] TV = new boolean[n];
        // ���[�J�Ĥ@���I
        TV[0] = true;
        int newNode = 0, found = 0, totalCost = 0;
        while (found < n - 1) {
            // �ˬd��[�J�I���Ҧ���
            for (Edge tt = adjList[newNode]; tt != null; tt = tt.next)
                if (!(TV[tt.x] && TV[tt.y])) heap.add(tt); // �p�G�Y�@�ݤ��bTV��
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