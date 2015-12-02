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
        g.setFont(new  Font("�ө���", Font.BOLD, fontSize));
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
            root.x = root.left.width + 1; // ��root.x���󥪥k�l�𤧶�
        }
        root.height = 1;
        root.y = 1;
        calPosition(root.left, root.y+1, root.x - 1, true); // ���l�� <= root.x - 1
        calPosition(root.right, root.y+1, root.x + 1, false); // �k�l�� >= root.x +1
        if (root.left != null) {
            root.height += root.left.height;
        }
        if (root.right != null && root.right.height >= root.height) {
            root.height = root.right.height + 1;
        }
    }
    private void calWidth(RBTreeNode t) { // �p��𪺼e��
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
     * @param t, �n�p���m���`�I
     * @param y, ���`�I��y�y��
     * @param boundary, t��ʾ����
     * @param atLeft, true��ܦb��ɥ���, false��ܦb��ɥk��
     */
    private void calPosition(RBTreeNode t, int y, int boundary, boolean atLeft) {
        if (t != null) {
            t.y = y;
            t.x = boundary; // �]�w�b��ɤW
            t.height = 1;
            if (t.right == null && t.left == null) {
                return;
            } else if (t.right != null && t.left == null) {
                if (atLeft) { // ���� <= boundary
                    t.x = boundary - t.right.width; // �S�����l��,�ҥH�]�w��̥���
                }
                calPosition(t.right, y + 1, t.x + 1, false); // �k�l��� >= tx + 1
                t.height = t.right.height + 1;
            } else if (t.left != null && t.right == null) {
                if (!atLeft) { // ���� >= boundary
                    t.x = boundary + t.left.width; // �S���k�l��, �ҥH�]�w��̥k��
                }
                calPosition(t.left, y + 1, t.x - 1, true); // ���l��� <= tx - 1
                t.height = t.left.height + 1;
            } else {
                if (atLeft) { // ���� <= boundary
                    t.x = boundary - t.right.width; // t.x�V��,�H�ťX�k�l�𪺼e��
                } else { // ���� >= boundary
                    t.x = boundary + t.left.width; // t.x�V�k,�H�ťX���l�𪺼e��
                }
                calPosition(t.right, y + 1, t.x + 1, false);  // ���l��� <= tx - 1
                calPosition(t.left, y + 1, t.x - 1, true); // �k�l��� >= tx + 1
                t.height = t.right.height + 1;
                if (t.height <= t.left.height) {
                    t.height = t.left.height + 1;
                }
            }
        }
    }
}