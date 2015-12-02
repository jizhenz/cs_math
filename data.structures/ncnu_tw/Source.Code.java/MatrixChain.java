public class MatrixChain {
    public static void main(String[] argv) {
        // M[i]�����צbM[i]�MM[i+1]
        int[] chain = {4, 2, 3, 1, 2, 2, 3};
        String[] name = {"A","B","C","D","E","F"};
        find(chain, name);
    }
    public static void find(int[] r, String[] name) {
        int n = r.length - 1; // number of Matrix Chain
        int[][] cost = new int[n][n]; // cost[i][j]���i~j���̤p����
        // best[i][j]��ܤ��Ϊ���m(i~best[i][j]) * (best[i][j]+1 ~ j)
        // �Ҧpbest[0][2]=0���ܪ��M[0] * M[1~2]
        int[][] best = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < n; i++) {
            cost[i][i] = 0;
        }
        for (int j = 1; j < n; j++) { // �p��Ҧ����׬�j+1������
            for (int i = 0; i < n - j; i++) {
                // �p���i��(i+j)���̤p����
                // �i�઺��k������(i,k-1),�A��(k,i+j)
                // �M��N�o��Ӭۭ�, ���ɪ�������r[i]*r[k]*r[i+j+1]
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