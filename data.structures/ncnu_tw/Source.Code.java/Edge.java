/**
 * Author: «\¦°ª@, Shiuh-Sheng Yu
 *     Department of Informaton Management
 *     National Chi Nan University
 * Purpose: data structure for Graph.java
 * Since 2009/01/19
 */
public class Edge implements Comparable {
    int x, y, cost;
    Edge next;
    public Edge(int x, int y, int cost, Edge next) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.next = next;
    }
    public int compareTo(Object t) {
        return cost - ((Edge)t).cost;
    }
}