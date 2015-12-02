/* Program Name: Sollin.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2007/03/26
 * Purpose: Demo Sollin's algorithm for minimun spanning tree
 */
import java.io.*;
public class Sollin {
    int n; // number of vertex in this graph
    Edge[] adjList;
    public Sollin(int vertex) {
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
    class Queue {
        int size, head, tail;
        int[] data = new int[1000];
        public void add(int x) {
            data[tail++] = x;
            if (tail >= data.length) tail = 0;
            size++;
        }
        public int delete() {
            if (size <= 0) return -1;
            int tmp = data[head++];
            if (head >= data.length) head = 0;
            size--;
            return tmp;
        }
    }
    public void span() { //using Sollin's algorithm
        Queue q = new Queue(); // roots to be processed
        Leftist[] heaps = new Leftist[n];
        int[] root = new int[n];
        for (int i = 0; i < n; i++) {
            q.add(i);
            root[i] = -1;
            heaps[i] = new Leftist();
            for (Edge tt = adjList[i]; tt != null; tt = tt.next) {
                heaps[i].add(tt);
            }
        }
        int found = 0, totalCost = 0, processing, set1, set2, lead, trail;
        Edge tmp;
        while (found < n - 1) {
            if ((processing = q.delete()) < 0) break;
            if (heaps[processing] == null) continue;
            if ((tmp = (Edge)heaps[processing].deleteMin()) == null) continue;
            for (set1 = tmp.x; root[set1] >= 0; set1 = root[set1])
                ;
            // 順手壓扁此集合
            for (trail = tmp.x; trail != set1; trail=lead) {
                lead = root[trail];
                root[trail] = set1;
            }
            for (set2 = tmp.y; root[set2] >=0 ; set2 = root[set2])
                ;
            // 壓扁set2
            for (trail = tmp.y; trail != set2; trail=lead) {
                lead = root[trail];
                root[trail] = set2;
            }
            if (set1 != set2) { // 加入此edge不會造成cycle
                totalCost += tmp.cost;
                found++;
                System.out.println(tmp.x+", "+tmp.y+", "+tmp.cost+" added");
                int setNum = root[set1] + root[set2];
                // 成員比較多的當作新的root
                // 因為用負數代表成員數,因此判斷上要反過來
                if (root[set1] > root[set2]) {
                    root[set1] = set2;
                    root[set2] = setNum;
                    heaps[set2].merge(heaps[set1]);
                    heaps[set1] = null;
                    q.add(set2);
                } else {
                    root[set2] = set1;
                    root[set1] = setNum;
                    heaps[set1].merge(heaps[set2]);
                    heaps[set2] = null;
                    q.add(set1);
                }
            } else {
                q.add(set1);
            }
        }
        if (found == n - 1)
            System.out.println("total cost = "+totalCost);
        else
            System.out.println("No spanning tree exists");
    }
    public static void main(String[] argv) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(argv[0]));
        Sollin g = new Sollin(Integer.parseInt(in.readLine()));
        String s;
        while ((s = in.readLine()) != null) {
            int sep1 = s.indexOf(" ");
            int sep2 = s.indexOf(" ", sep1 + 1);
            g.add(Integer.parseInt(s.substring(0, sep1)), Integer.parseInt(s.substring(sep1+1,sep2)), Integer.parseInt(s.substring(sep2+1,s.length())));
        }
        g.span();
    }
}