public class AVLNode {
    AVLNode left, right; // ���k�l��
    byte bf; // balance factor, 1��ܥ��l�����,0��ܤ@�˰�,-1��ܥk�l�����
    int lsize = 1; // ���l��`�I��+1
    int size = 1; // �Ҧ��l�`�I��+1
    String key;
    Object data;
    int x, y, width, height; // for rendering
}
