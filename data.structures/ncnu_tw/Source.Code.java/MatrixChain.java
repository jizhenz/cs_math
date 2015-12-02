public class MatrixChain {
    public static void main(String[] argv) {
        // M[i]的維度在M[i]和M[i+1]
        int[] chain = {4, 2, 3, 1, 2, 2, 3};
        String[] name = {"A","B","C","D","E","F"};
        find(chain, name);
    }
    public static void find(int[] r, String[] name) {
        int n = r.length - 1; // number of Matrix Chain
        int[][] cost = new int[n][n]; // cost[i][j]表示i~j的最小成本
        // best[i][j]表示分割的位置(i~best[i][j]) * (best[i][j]+1 ~ j)
        // 例如best[0][2]=0的話表示M[0] * M[1~2]
        int[][] best = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < n; i++) {
            cost[i][i] = 0;
        }
        for (int j = 1; j < n; j++) { // 計算所有長度為j+1的成本
            for (int i = 0; i < n - j; i++) {
                // 計算由i到(i+j)的最小成本
                // 可能的算法為先算(i,k-1),再算(k,i+j)
                // 然後將這兩個相乘, 此時的成本為r[i]*r[k]*r[i+j+1]
                for (int k = i + 1; k <= i + j; k++) {
                    int t = cost[i][k-1] + cost[k][i+j] + r[i]*r[k]*r[i+j+1];
                    if (t < cost[i][i+j]) {
                        cost[i][i+j] = t;
                        best[i][i+j] = k-1;
                    }
                }
            }
        }
        System.out.println("cost = "+cost[0][n-1]);
        order(best, 0, n-1, name);
    }
    public static void order(int[][] best, int i, int j, String[] name) {
        if (i == j) {
            System.out.print(name[i]);
        } else {
            System.out.print('(');
            order(best, i, best[i][j], name);
            order(best, best[i][j]+1, j, name);
            System.out.print(')');
        }
    }
}