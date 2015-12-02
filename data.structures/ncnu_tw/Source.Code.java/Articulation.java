/* Program Name: Articulation.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2006/02/20
 * Purpose: Articulation Points
 */
import java.io.*;
public class Articulation {
    int n; // number of vertex in this graph
    Node[] graph; // adjacent list for all vertex
    int num; // current dfn
    int[] dfn; // each vertex's depth first search number
    int[] low; // earliest ancestor through back edge
    public Articulation(int vertex) {
        n = vertex;
        graph = new Node[n];
        dfn = new int[n];
        low = new int[n];
    }
    public void init() {
        for (int i = 0; i < n; i++) {
            dfn[i] = low[i] = -1;
        }
        num = 0;
    }
    public void articulation() {
        init();
        articulation(0, -1);
    }
    private void articulation(int check, int parent) {
        int childCount = 0;
        dfn[check] = low[check] = num++;
        for (Node adj = graph[check]; adj != null; adj = adj.link) {
            int w = adj.vertex;
            if (dfn[w] < 0) { // w is a child of check
                childCount++;
                articulation(w, check);
                // check可由w回到更上層?
                low[check] = (low[check] < low[w]) ? low[check] : low[w];
                if (parent >=0 && low[w] >= dfn[check]) { // 如果check斷掉,w就沒有路回到上層了
                    System.out.println("Articulation point "+check+" dfn="+dfn[check]+" low["+w+"]="+low[w]);
                }
            } else if (w != parent) { // w is a grand parent of check(back edge)
                low[check] = (low[check] < dfn[w]) ? low[check] : dfn[w];
            }
        }
        if (parent < 0 && childCount > 1) { // root has more than one child
            System.out.println("Articulation point "+check+", root has "+childCount+" children");
        }
    }
    // add nodes to adjacent list
    public void add(int x, int y, int cost) {
        Node tt = new Node();
        tt.vertex = y;
        tt.link = graph[x];
        graph[x] = tt;
        tt = new Node();
        tt.vertex = x;
        tt.link = graph[y];
        graph[y] = tt;
    }
    public static void main(String[] argv) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(argv[0]));
        Articulation g = new Articulation(Integer.parseInt(in.readLine()));
        String s;
        while ((s = in.readLine()) != null) {
            int sep1 = s.indexOf(" ");
            int sep2 = s.indexOf(" ", sep1 + 1);
            g.add(Integer.parseInt(s.substring(0, sep1)), Integer.parseInt(s.substring(sep1+1,sep2)), Integer.parseInt(s.substring(sep2+1,s.length())));
        }
        g.articulation();
    }
}