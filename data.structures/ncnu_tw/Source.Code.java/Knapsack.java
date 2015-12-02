/* Program Name: Knapsack.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2006/03/03
 * Purpose: Directed Graph Algorithm(AOE)
 */
public class Knapsack {
    int[] size, value;
    String[] name;
    public Knapsack(int[] size, int[] value, String[] name) {
        this.size = size;
        this.value = value;
        this.name = name;
    }
    public void eval(int bagSize) {
        int[] cost = new int[bagSize+1];
        int[] best = new int[bagSize+1];
        for (int i = 0; i <= bagSize; i++) {
            best[i] = -1;
        }
        for (int j = 0; j < size.length; j++) {
            for (int i = 1; i <= bagSize; i++) {
                if (i >= size[j] && cost[i] < cost[i-size[j]]+value[j]) {
                    cost[i] = cost[i-size[j]]+value[j];
                    best[i] = j;
                }
            }
        }
        System.out.print("BagSize "+bagSize+",Cost "+cost[bagSize]+":");
        for (int at = bagSize; best[at] >= 0; at -= size[best[at]]) {
            System.out.print(name[best[at]]+" ");
        }
        System.out.println();
    }
    public static void main(String[] argv) {
        int[] size = {3,5,7,11,13};
        int[] value = {7,12,17,27,33};
        String[] name = {"A", "B", "C", "D", "E"};
        Knapsack pro = new Knapsack(size,value,name);
        for (int i = 5; i<120; i++) {
            pro.eval(i);
        }
    }
}