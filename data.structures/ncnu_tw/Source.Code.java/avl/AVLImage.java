import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
public class AVLImage extends AVL {
    static int scale = 50;
    static int fontSize = 18;
    int snapLen = 0;
    BufferedImage[] snaps = new BufferedImage[10];
    public synchronized void inOrder() {
        inOrder(root, 0);
    }
    private void inOrder(AVLNode t, int depth) {
        if (t != null) {
            inOrder(t.left, depth+1);
            System.out.println(t.data+" at "+depth+" bf="+t.bf+", x="+t.x+", y="+t.y+", width="+t.width);
            inOrder(t.right, depth+1);
        }
    }
    public void insert(String key, String data) {
        snapLen = 0;
        if (root != null) {
            getSnapShot();
        }
        super.insert(key, data);
        getSnapShot();
    }
    public void delete(String key) {
        snapLen = 0;
        if (root != null) {
            getSnapShot();
        }
        super.delete(key);
        getSnapShot();
    }
    public BufferedImage[] getSnaps() {
        BufferedImage[] tmp = new BufferedImage[snapLen];
        for (int i = 0; i < snapLen; i++) {
            tmp[i] = snaps[i];
        }
        return tmp;
    }
    protected void leftRotate(AVLNode grandParent, AVLNode parent) {
        if (snapLen <= 1) {
            getSnapShot();
        }
        super.leftRotate(grandParent, parent);
        getSnapShot();
    }
    protected void rightRotate(AVLNode grandParent, AVLNode parent) {
        if (snapLen <= 1) {
            getSnapShot();
        }
        super.rightRotate(grandParent, parent);
        getSnapShot();
    }
    private void getSnapShot() {
        if (root == null) {
            BufferedImage canvas = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
            snaps[snapLen++] = canvas;
            return;
        }
        render();
        BufferedImage canvas = new BufferedImage(root.width*scale+2*fontSize, (root.height+1)*scale, BufferedImage.TYPE_3BYTE_BGR);
        snaps[snapLen++] = canvas;
        Graphics g = canvas.getGraphics();
        g.setFont(new  Font("標楷體", Font.PLAIN, fontSize));
        draw(g, root);
    }
    public synchronized void draw(Graphics g, AVLNode t) {
        if (t != null) {
            g.drawString(t.key,t.x*scale-scale+fontSize, t.y*scale-fontSize);
            g.drawString(Integer.toString(t.bf),t.x*scale-scale+fontSize, t.y*scale-2*fontSize);
            if (t.left != null) {
                g.drawLine(t.x*scale-scale+fontSize,t.y*scale-scale/2+fontSize,t.left.x*scale-scale+2*fontSize, t.left.y*scale-scale/2-fontSize*3/2);
                draw(g, t.left);
            }
            if (t.right != null) {
                g.drawLine(t.x*scale-scale+2*fontSize,t.y*scale-scale/2+fontSize,t.right.x*scale-scale+fontSize,t.right.y*scale-scale/2-fontSize*3/2);
                draw(g, t.right);
            }
        }
    }
    public synchronized void render() {
        if (root == null) {
            return;
        }
        calWidth(root);
        if (root.left == null) {
            root.x = 1;
        } else {
            root.x = root.left.width + 1; // 讓root.x介於左右子樹之間
        }
        root.height = 1;
        root.y = 1;
        calPosition(root.left, root.y+1, root.x - 1, true); // 左子樹須 <= root.x - 1
        calPosition(root.right, root.y+1, root.x + 1, false); // 右子樹須 >= root.x +1
        if (root.left != null) {
            root.height += root.left.height;
        }
        if (root.right != null && root.right.height >= root.height) {
            root.height = root.right.height + 1;
        }
    }
    private void calWidth(AVLNode t) { // 計算樹的寬度
        t.width = 1;
        if (t.right != null || t.left != null) {
            if (t.left != null) {
                calWidth(t.left);
                t.width += t.left.width;
            }
            if (t.right != null) {
                calWidth(t.right);
                t.width += t.right.width;
            }
        }
    }
    /**
     * @param t, 要計算位置的節點
     * @param y, 此節點的y座標
     * @param boundary, t整棵樹的邊界
     * @param atLeft, true表示在邊界左側, false表示在邊界右側
     */
    private void calPosition(AVLNode t, int y, int boundary, boolean atLeft) {
        if (t != null) {
            t.y = y;
            t.x = boundary; // 設定在邊界上
            t.height = 1;
            if (t.right == null && t.left == null) {
                return;
            } else if (t.right != null && t.left == null) {
                if (atLeft) { // 必須 <= boundary
                    t.x = boundary - t.right.width; // 沒有左子樹,所以設定到最左邊
                }
                calPosition(t.right, y + 1, t.x + 1, false); // 右子樹需 >= tx + 1
                t.height = t.right.height + 1;
            } else if (t.left != null && t.right == null) {
                if (!atLeft) { // 必須 >= boundary
                    t.x = boundary + t.left.width; // 沒有右子樹, 所以設定到最右邊
                }
                calPosition(t.left, y + 1, t.x - 1, true); // 左子樹需 <= tx - 1
                t.height = t.left.height + 1;
            } else {
                if (atLeft) { // 必須 <= boundary
                    t.x = boundary - t.right.width; // t.x向左,以空出右子樹的寬度
                } else { // 必須 >= boundary
                    t.x = boundary + t.left.width; // t.x向右,以空出左子樹的寬度
                }
                calPosition(t.right, y + 1, t.x + 1, false);  // 左子樹需 <= tx - 1
                calPosition(t.left, y + 1, t.x - 1, true); // 右子樹需 >= tx + 1
                t.height = t.right.height + 1;
                if (t.height <= t.left.height) {
                    t.height = t.left.height + 1;
                }
            }
        }
    }
}