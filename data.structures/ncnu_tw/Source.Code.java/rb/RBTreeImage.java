import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
public class RBTreeImage extends RBTree {
    static int scale = 50;
    static int fontSize = 24;
    int snapLen = 0;
    BufferedImage[] snaps = new BufferedImage[10];
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
    protected boolean reStructure(RBTreeNode parent, RBTreeNode deleted, RBTreeNode next, String key) {
        if (super.reStructure(parent, deleted, next, key)) {
            getSnapShot();
            return true;
        }
        return false;
    }
    protected void LLRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        getSnapShot();
        super.LLRotate(gggp, ggp, gp, p);
        getSnapShot();
    }
    protected void LRRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        getSnapShot();
        super.LRRotate(gggp, ggp, gp, p);
        getSnapShot();
    }
    protected void RRRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        getSnapShot();
        super.RRRotate(gggp, ggp, gp, p);
        getSnapShot();
    }
    protected void RLRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        getSnapShot();
        super.RLRotate(gggp, ggp, gp, p);
        getSnapShot();
    }
    public BufferedImage[] getSnaps() {
        BufferedImage[] tmp = new BufferedImage[snapLen];
        for (int i = 0; i < snapLen; i++) {
            tmp[i] = snaps[i];
        }
        return tmp;
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
        g.setFont(new  Font("細明體", Font.BOLD, fontSize));
        draw(g, root);
    }
    public synchronized void draw(Graphics g, RBTreeNode t) {
        if (t != null) {
            if (t.isRed) {
                g.setColor(Color.red);
            }
            g.drawString(t.key,t.x*scale-scale+fontSize, t.y*scale-fontSize);
            g.setColor(Color.white);
            if (t.left != null) {
                g.drawLine(t.x*scale-scale+fontSize,t.y*scale-scale/2+fontSize,t.left.x*scale-scale+2*fontSize, t.left.y*scale-scale/2-fontSize);
                draw(g, t.left);
            }
            if (t.right != null) {
                g.drawLine(t.x*scale-scale+2*fontSize,t.y*scale-scale/2+fontSize,t.right.x*scale-scale+fontSize,t.right.y*scale-scale/2-fontSize);
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
    private void calWidth(RBTreeNode t) { // 計算樹的寬度
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
    private void calPosition(RBTreeNode t, int y, int boundary, boolean atLeft) {
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