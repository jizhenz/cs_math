public class OptimalTree {
    public static void main(String[] argv) {
        // M[i]�����צbM[i]�MM[i+1]
        int[] frequency = {4, 2, 1, 3, 5, 2, 1};
        String[] name = {"A","B","C","D","E","F","G"};
        find(frequency, name);
    }
    public static void find(int[] f, String[] name) {
        int n = f.length; // number of Matrix Chain
        // �ݭn0�Mn+1�ӳB�z���@��S���l�𪺱��p
        int[][] cost = new int[n+2][n+2]; // cost[i][j]���i~j���̤p����
        // best[i][j]��ܤ��Ϊ���m(i~best[i][j]-1) * (best[i][j]+1 ~ j)
        // �Ҧpbest[0][3]=2���ܪ��M[0~1] * M[3~3]
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
            cost[i][i-1] = 0; // null cost ��0
        }
        for (int j = 1; j < n; j++) { // �p��Ҧ����׬�j+1������
            for (int i = 1; i <= n - j; i++) {
                // �p���i��(i+j)���̤p����
                // �i�઺��k������(i,k-1),�A��(k+1,i+j)
                // k=i�ɪ��node i��root,�B�u���@�ӥk�l��(i+1,i+j)
                // k=i+j�ɪ��node i+j��root,�B�u���@�ӥ��l��(i,i+j-1)
                for (int k = i; k <= i + j; k++) {
                    int t = cost[i][k-1] + cost[k+1][i+j];
                    if (t < cost[i][i+j]) {
                        cost[i][i+j] = t;
                        best[i][i+j] = k;
                    }
                }
                // �⧹��,�A�p���ܦ���Ӥl��᪺����
                // �]���h�F�@�h,�ҥHi~i+j���Ҧ��`�I���׳�+1
                // ���y�ܻ�, i~i+j���C�@��frequency���n�A�[�@��
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