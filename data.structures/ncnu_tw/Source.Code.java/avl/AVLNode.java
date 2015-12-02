public class AVLNode {
    AVLNode left, right; // 左右子樹
    byte bf; // balance factor, 1表示左子樹較高,0表示一樣高,-1表示右子樹較高
    int lsize = 1; // 左子樹節點數+1
    int size = 1; // 所有子節點數+1
    String key;
    Object data;
    int x, y, width, height; // for rendering
}
