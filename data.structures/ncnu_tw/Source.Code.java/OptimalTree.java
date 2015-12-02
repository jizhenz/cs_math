public class OptimalTree {
    public static void main(String[] argv) {
        // M[i]的維度在M[i]和M[i+1]
        int[] frequency = {4, 2, 1, 3, 5, 2, 1};
        String[] name = {"A","B","C","D","E","F","G"};
        find(frequency, name);
    }
    public static void find(int[] f, String[] name) {
        int n = f.length; // number of Matrix Chain
        // 需要0和n+1來處理有一邊沒有子樹的情況
        int[][] cost = new int[n+2][n+2]; // cost[i][j]表示i~j的最小成本
        // best[i][j]表示分割的位置(i~best[i][j]-1) * (best[i][j]+1 ~ j)
        // 例如best[0][3]=2的話表示M[0~1] * M[3~3]
        //          2 
        //        /  \                
        //     (0-1) (3)
        int[][] best = new int[n+1][n+1];
        for (int i = 1; i <= n; i++) {
            for (int j = i+1; j <= n+1; j++) {
                cost[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 1; i <= n; i++) {
            cost[i][i] = f[i-1];
        }
        for (int i = 1; i <= n; i++) {
            cost[i][i-1] = 0; // null cost 為0
        }
        for (int j = 1; j < n; j++) { // 計算所有長度為j+1的成本
            for (int i = 1; i <= n - j; i++) {
                // 計算由i到(i+j)的最小成本
                // 可能的算法為先算(i,k-1),再算(k+1,i+j)
                // k=i時表示node i為root,且只有一個右子樹(i+1,i+j)
                // k=i+j時表示node i+j為root,且只有一個左子樹(i,i+j-1)
                for (int k = i; k <= i + j; k++) {
                    int t = cost[i][k-1] + cost[k+1][i+j];
                    if (t < cost[i][i+j]) {
                        cost[i][i+j] = t;
                        best[i][i+j] = k;
                    }
                }
                // 算完後,再計算變成兩個子樹後的成本
                // 因為多了一層,所以i~i+j的所有節點高度都+1
                // 換句話說, i~i+j的每一個frequency都要再加一次
                for (int k = i; k <= i+j; cost[i][i+j] += f[k-1], k++) ;
            }
        }
        System.out.println("cost = "+cost[1][n]);
        order(best, 1, n, name);
    }
    public static void order(int[][] best, int i, int j, String[] name) {
        if (i == j) {
            System.out.print(name[i-1]);
        } else if (i < j) {
            System.out.print(name[best[i][j]-1]);
            System.out.print('(');
            order(best, i, best[i][j]-1, name);
            System.out.print(')');
            System.out.print('(');
            order(best, best[i][j]+1, j, name);
            System.out.print(')');
        }
    }
}