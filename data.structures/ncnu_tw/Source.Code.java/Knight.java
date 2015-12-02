/**
 * Author: Shiuh-Sheng Yu
 *         Department of Information Management
 *         National Chi Nan University
 * Purpose: Find a sequence for knight to visit all positions from start point
 *          Using J. C. Warnsdorff's algorithm in 1823
 * Since: 2006/12/03
 */
public class Knight {
    static int[] dir = {-25,-23,25,23,14,10,-14,-10};
    public static void main(String[] argv) {
        int[] board = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                       -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,
                       -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                       -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int start = 26, steps = 0;
        if (argv.length >= 2) {
            start = Integer.parseInt(argv[0]) * 12 + Integer.parseInt(argv[1]) + 26;
            if (!(start >= 0 && start < board.length && board[start] == 0)) {
                System.out.println("Please input coordinates between (0,0) and (7,7)");
                System.exit(0);
            }
        }
        while (start != 0) {
            int min = 10, minAt = 0, next;
            for (int j = 0; j < 8; j++) { // check eight directions
                if (board[next = start + dir[j]] == 0) { // count exits at next
                    int exit = 0;
                    for (int k = 0; k < 8; k++) {
                        if (board[next + dir[k]] == 0) exit++;
                    }
                    if (exit < min) { // found next position with fewest exit
                        min = exit;
                        minAt = next;
                    }
                }
            }
            System.out.println("("+(start/12-2)+","+(start%12-2)+")");
            board[start] = ++steps;
            start = minAt;
        }
    }
}