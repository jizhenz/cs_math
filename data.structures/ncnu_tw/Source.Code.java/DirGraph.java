/* Program Name: DirGraph.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2006/03/03
 * Purpose: Directed Graph Algorithm(AOE)
 */
public class DirGraph {
    class HeadNode {
        int count; // in edge count
        int invertCount; // out edge count
        int early; // earliest activity time
        int late; // latest activity time
        Node link; // to adjacent list
        Node invert; // to inverse list
    }
    class Node {
        int vertex;
        int duration;
        Node link;
    }
    int n; // number of vertex in this graph
    HeadNode[] graph; // adjacent list for all vertex
    public void showEarly() {
        for (int i = 0; i < n; i++) {
            System.out.print(graph[i].early+" ");
        }
        System.out.println();
    }
    public void showLate() {
        for (int i = 0; i < n; i++) {
            System.out.print(graph[i].late+" ");
        }
        System.out.println();
    }
    public void showCritical() {
        for (int i = 0; i < n; i++) {
            if (graph[i].late == graph[i].early) {
                for (Node ptr = graph[i].link; ptr != null; ptr = ptr.link) {
                    if (graph[ptr.vertex].late == graph[ptr.vertex].early) {
                        System.out.print(i+"==>"+ptr.vertex+",");
                    }
                }
            }
        }
        System.out.println();
    }
    public DirGraph(int vertex) {
        n = vertex;
        graph = new HeadNode[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new HeadNode();
        }
    }
    public void time() {
        int top = 0;
        int check = 0;
        System.out.println("Computing early time");
        do {
            System.out.println("checking "+check);
            for (Node ptr = graph[check].link; ptr != null; ptr = ptr.link) {
                // 如果找到更長的路徑
                if (graph[ptr.vertex].early < graph[check].early+ptr.duration) {
                    graph[ptr.vertex].early = graph[check].early+ptr.duration;
                }
                // 少了一個predecessor
                graph[ptr.vertex].count--;
                if (graph[ptr.vertex].count == 0) {
                    // push to stack
                    // 利用已經沒有用處的count來實作link list stack
                    graph[ptr.vertex].count = top;
                    top = ptr.vertex;
                }
            }
            showEarly();
            // pop next element for check
            check = top;
            top = graph[top].count;
        } while (check != 0);
        System.out.println("Computer late time");
        for (int i = 0; i < n; i++) {
            graph[i].late = graph[n-1].early;
        }
        // calculate late time
        check = n - 1;
        top = n - 1;
        do {
            System.out.println("checking "+check);
            for (Node ptr = graph[check].invert; ptr != null; ptr = ptr.link) {
                if (graph[ptr.vertex].late > graph[check].late - ptr.duration) {
                    graph[ptr.vertex].late = graph[check].late - ptr.duration;
                }
                graph[ptr.vertex].invertCount--;
                if (graph[ptr.vertex].invertCount == 0) {
                    // push to stack
                    // 利用已經沒有用處的invertCount來實作link list stack
                    graph[ptr.vertex].invertCount = top;
                    top = ptr.vertex;
                }
            }
            showLate();
            // pop next element for check
            check = top;
            top = graph[top].invertCount;
        } while (check != n - 1);
        System.out.println("Critical path is ");
        showCritical();
    }
    public void add(int x, int y, int cost) {
        // add node to adjacent list
        Node tt = new Node();
        tt.vertex = y;
        tt.duration = cost;
        tt.link = graph[x].link;
        graph[x].link = tt;
        graph[y].count++;
        // add node to invert list
        tt = new Node();
        tt.vertex = x;
        tt.duration = cost;
        tt.link = graph[y].invert;
        graph[y].invert = tt;
        graph[x].invertCount++;
    }
    public static void main(String[] argv) {
        System.out.println("Activity on Edge, Figure 6.41");
        DirGraph g = new DirGraph(9);
        g.add(0, 3, 5);
        g.add(0, 2, 4);
        g.add(0, 1, 6);
        g.add(1, 4, 1);
        g.add(2, 4, 1);
        g.add(3, 5, 2);
        g.add(4, 7, 7);
        g.add(4, 6, 9);
        g.add(5, 7, 4);
        g.add(6, 8, 2);
        g.add(7, 8, 4);
        g.time();
    }
}