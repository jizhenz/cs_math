/**
 * Program Name: AVL.java
 * Author: Shiuh-Sheng Yu,
 *         Department of Information Management,
 *         National Chi Nan University, Taiwan
 * Since: 2006/02/02
 * Toolkit: JDK
 */
public class AVL {
    protected AVLNode root;
    private AVLNode[] stack = new AVLNode[100]; // �Ψ��x�sinsert��delete�Ҹg�L�����|
    public synchronized Object search(int k) {
        if (root == null || root.size <= k) {
            return null;
        }
        AVLNode t = root;
        while (t.lsize != k + 1) {
            if (t.lsize > k + 1) {
                t = t.left;
            } else {
                k -= t.lsize;
                t = t.right;
            }
        }
        return t.data;
    }
    public synchronized Object search(String key) {
        int rel;
        AVLNode t = root;
        while (t != null) {
            if ((rel = t.key.compareTo(key)) > 0) {
                t = t.left;
            } else if (rel < 0) {
                t = t.right;
            } else {
                return t.data;
            }
        }
        return null;
    }
    public int getSize() {
        return (root == null) ? 0 : root.size;
    }
    public synchronized void delete(String key) {
        if (search(key) == null) { // no such key
            return;
        }
        AVLNode deleted = root; // target to be deleted
        AVLNode parent = null; // parent of actual deleted
        int top = 0;
        while (deleted != null) {
            deleted.size--;
            if (deleted.left != null && deleted.right != null) {
                stack[top++] = parent; // �O��Ҧ��g�L��two children node
                stack[top++] = deleted;
            }
            if (deleted.key.compareTo(key) > 0) { // �ؼЦb����
                deleted.lsize--;
                parent = deleted;
                deleted = deleted.left;
            } else if (deleted.key.compareTo(key) < 0) { // �ؼЦb�k��
                parent = deleted;
                deleted = deleted.right;
            } else {
                break;
            }
        }
        AVLNode actual = deleted; // �ΨӨ��N���`�I,�N���ڤW�n�R���`�I
        if (deleted.left != null && deleted.right != null) { //�n�R���`�I����Ӥl��,������Xreplace node
            // ��X���l��̤j��
            actual = deleted.left;
            parent = deleted;
            deleted.lsize--;
            actual.size--;
            if (actual.left != null && actual.right != null) {
                stack[top++] = parent; // �O��Ҧ��g�L��two children node
                stack[top++] = actual;
            }
            while (actual.right != null) {
                parent = actual;
                actual = actual.right;
                actual.size--;
                if (actual.left != null && actual.right != null) {
                    stack[top++] = parent; // �O��Ҧ��g�L��two children node
                    stack[top++] = actual;
                }
            }
        }
        if (actual != deleted) { // ���N�Ҧ�
            deleted.key = actual.key;
            deleted.data = actual.data;
            // �]�wreplace���`�I��link
            if (parent.left == actual) {
                parent.left = actual.left;
                parent.bf--;
            } else {
                parent.right = actual.left;
                parent.bf++;
            }
        } else if (parent == null) { // �R��ڸ`�I
            if (deleted.right != null) {
                root = deleted.right;
            } else {
                root = deleted.left;
            }
            return;
        } else if (parent.left == deleted) { // �R�쥪�l��
            if (deleted.right != null) {
                parent.left = deleted.right;
            } else {
                parent.left = deleted.left;
            }
            parent.bf--;
        } else { // �R��k�l��
            if (deleted.right != null) {
                parent.right = deleted.right;
            } else {
                parent.right = deleted.left;
            }
            parent.bf++;
        }
        // rotate���M�i���Ťl��,���O�l�𪺰��פ@�w�|���C,�]���n�@�����W�B�z
        while (top > 0) {
            AVLNode checking = stack[--top];
            stack[top] = null;
            AVLNode checkingParent = stack[--top];
            stack[top] = null;
            if (checking.key.compareTo(actual.key) >= 0) { // actual�b���l��.�ѩ�replace���p�U,actual.key�|���deleted,�]���i��|�۵�
                // (1)checking.left�ѥ����ܤ�����,���|�Y���, �]������delete
                // (2)�ѩ�O�R���ʧ@,checking.left�i��b�e���Q�R��,����checking.bf�]�ݭn�վ�
                if (checking.left != null && checking.left.bf != 0) {
                    return;
                }
                if (checking != parent) { // �קK���⦸. �]��parent�i��O��Ӧ�two children�ӳQ���stack��
                    checking.bf--;
                }
                if (checking.bf < -1) {
                    rightRotate(checkingParent, checking);
                }
            } else { // actual�b�k�l��
                // (1)checking.left�ѥ����ܤ�����,���|�Y���, �]������delete
                // (2)�ѩ�O�R���ʧ@,checking.right�i��b�e���Q�R��,����checking.bf�]�ݭn�վ�
                if (checking.right != null && checking.right.bf != 0) {
                    return;
                }
                if (checking != parent) { // �קK���⦸. �]��parent�i��O��Ӧ�two children�ӳQ���stack��
                    checking.bf++;
                }
                if (checking.bf > 1) {
                    leftRotate(checkingParent, checking);
                }
            }
        }
    }
    public synchronized void insert(String key, Object d) {
        if (root == null) {
            root = new AVLNode();
            root.data = d;
            root.key = key;
            return;
        }
        if (search(key) != null) { // already exists
            return;
        }
        int top = 0;
        AVLNode grandParent = null;
        AVLNode parent = root;
        while (true) {
            parent.size++;
            if (parent.key.compareTo(key) > 0) { // ��쥪�l��
                parent.lsize++;
                if (parent.left == null) {
                    parent.left = new AVLNode();
                    parent.left.data = d;
                    parent.left.key = key;
                    parent.bf++;
                    if (parent.bf == 0) {
                        return;
                    }
                    break;
                } else {
                    stack[top++] = grandParent; // �����g�L�����|
                    stack[top++] = parent;
                    grandParent = parent;
                    parent = parent.left;
                }
            } else if (parent.key.compareTo(key) < 0) {
                if (parent.right == null) {
                    parent.right = new AVLNode();
                    parent.right.data = d;
                    parent.right.key = key;
                    parent.bf--;
                    if (parent.bf == 0) {
                        return;
                    }
                    break;
                } else {
                    stack[top++] = grandParent; // �����g�L�����|
                    stack[top++] = parent;
                    grandParent = parent;
                    parent = parent.right;
                }
            }
        }
        // �u�n���ũ�rotate�N�i�H���C�𪺰���,�]���̦hrotate�@���Y�i
        while (top > 0) { // �ˬd�Ҧ����L���`�I
            AVLNode checking = stack[--top];
            stack[top] = null;
            AVLNode checkingParent = stack[--top];
            stack[top] = null;
            if (checking.key.compareTo(key) > 0) { // �[��checking����
                if (++checking.bf == 0) {
                    return;
                } else if (checking.bf > 1) {
                    leftRotate(checkingParent, checking);
                    return;
                }
            } else { // �[��checking�k��
                if (--checking.bf == 0) {
                    return;
                } else if (checking.bf < -1) {
                    rightRotate(checkingParent, checking);
                    return;
                }
            }
        }
    }
    private static void fixSize(AVLNode parent) {
        if (parent.left != null && parent.right != null) {
            parent.size = parent.left.size + parent.right.size + 1;
            parent.lsize = parent.left.size + 1;
        } else if (parent.left != null) {
            parent.size = parent.left.size + 1;
            parent.lsize = parent.left.size + 1;
        } else if (parent.right != null) {
            parent.size = parent.right.size + 1;
            parent.lsize = 1;
        } else {
            parent.size = parent.lsize = 1;
        }
    }
    protected void rightRotate(AVLNode grandParent, AVLNode parent) {
        AVLNode child, grandChild, cl, cr;
        boolean RR = false;
        child = parent.right;
        if (child.bf == 0) { // �o�ͦbdelete���S���p
            // ��left child of grandChild��null, �N�����RL
            AVLNode gc = child.left;
            if (gc.right == null) {
                RR = true;
            } else {
                RR = false;
            }
        } else if (child.bf == -1) {
            RR = true;
        } else {
            RR = false;
        }
        if (RR) { // RR rotation
            // when insert or (delete and C.bf = -1)
            //       GP                  GP
            //       |                   |
            //       P        ==>        C
            //      /  \                / \
            //     ?   C               P  CR
            //        / \             / \
            //       CL CR           ?  CL
            // when delete and C.bf = 0
            //       GP                  GP
            //       |                   |
            //       P        ==>        C
            //         \                / \
            //         C               P  CR
            //        / \               \
            //       CL CR              CL
            parent.right = child.left;
            child.left = parent;
            if (child.bf == -1) {
                parent.bf = child.bf = 0;
            } else { // child.bf = 0 ��, �@�w��CL,CR�B�S��?
                parent.bf = -1;
                child.bf = 1;
            }
            fixSize(parent);
            fixSize(child);
            if (grandParent == null) {
                root = child;
            } else {
                if (grandParent.key.compareTo(child.key) > 0) {
                    grandParent.left = child;
                } else {
                    grandParent.right = child;
                }
            }
        } else { // RL rotation. �U��GCL or GCR�i�ೣ��
            // When insert of (delete and C.bf = 1)
            //     GP                   GP
            //     |                    |
            //     P           ==>      GC
            //   /    \               /     \
            //   ?    C              P       C
            //       /  \           /  \    /  \
            //      GC   ?         ?  GCL GCR  ?
            //     /  \    
            //    GCL GCR  
            // When delete and C.bf = 0, then GCR can't be null, otherwise C unbalance
            //     GP                    GP
            //     |                     |
            //     P           ==>       GC
            //   /    \                /     \
            //   ?    C               P       C
            //       /   \           /  \    /  \
            //      GC    ?         ?  GCL GCR  ?
            //     /  \   |                     |
            //    GCL GCR ?                     ?
            grandChild = child.left;
            parent.right = grandChild.left;
            child.left = grandChild.right;
            grandChild.left = parent;
            grandChild.right = child;
            grandChild.lsize += parent.lsize;
            // ����parent size;
            fixSize(parent);
            fixSize(child);
            fixSize(grandChild);
            if (grandParent == null) {
                root = grandChild;
            } else {
                if (grandParent.key.compareTo(grandChild.key) > 0) {
                    grandParent.left = grandChild;
                } else {
                    grandParent.right = grandChild;
                }
            }
            if (child.bf == 1) {
                switch(grandChild.bf) {
                case 1: // ��GCL. �S��GCR, �άOGCR��GCL�G�@�I
                    parent.bf = 0;
                    child.bf = -1;
                    break;
                case 0: // ��GCR�MGCL, �B�@�˰�
                    parent.bf = child.bf = 0;
                    break;
                case -1: // ��GCR, �S��GCL, ��GCL��GCR�G�@�I
                    parent.bf = 1;
                    child.bf = 0;
                }
                grandChild.bf = 0;
            } else { // when deleted
                parent.bf = 0;
                child.bf = -1;
                grandChild.bf = -1;
            }
        }
    }
    protected void leftRotate(AVLNode grandParent, AVLNode parent) {
        AVLNode child, grandChild;
        boolean LL = false;
        child = parent.left;
        if (child.bf == 0) { // �o�ͦbdelete���S���p
            // ��right child of grandChild��null��,�N�����LR, �_�h������
            AVLNode gc = child.right;
            if (gc.left == null) {
                LL = true;
            } else {
                LL = false;
            }
        } else if (child.bf == 1) {
            LL = true;
        } else {
            LL = false;
        }
        if (LL) { // LL roation. �U�ϥi��S��CR(�P�ɨS��?), ��CL��CR���@�I
            // when insert or (delete and C.bf = 1);
            //         GP              GP
            //         |               |
            //         P 2     ==>     C
            //        / \             /  \
            //       C 1 ?           CL   P
            //      / \                  / \
            //     CL  CR               CR ?
            // after rotation C.bf =0, P.bf = 0
            //
            // when delete and C.bf = 0
            //         GP              GP
            //         |               |
            //         P       ==>     C
            //        /               /  \
            //       C               CL   P
            //      / \                  /  
            //     CL  CR               CR  
            // after rotaion C.bf = -1, p.bf = 1
            parent.left = child.right;
            child.right = parent;
            if (child.bf == 1) {
                parent.bf = child.bf = 0;
            } else { // ��CL,CR�B�S��?
                parent.bf = 1;
                child.bf = -1;
            }
            fixSize(parent);
            fixSize(child);
            if (grandParent == null) {
                root = child;
            } else {
                if (grandParent.key.compareTo(child.key) > 0) {
                    grandParent.left = child;
                } else {
                    grandParent.right = child;
                }
            }
        } else { // LR roation. �U��GCL or GCR�i�ೣ��
            // When insert or (delete and C.bf = -1)
            //     GP                  GP
            //     |                   |
            //     P                   GC
            //    /   \              /    \
            //   C     ?    ==>     C       P
            //  /  \               /  \    /  \
            // ?   GC             ?  GCL  GCR  ?
            //     / \
            //   GCL  GCR
            // When delete, if C.bf = 0 then GCL can't be null, otherwise C unbalance
            //     GP                  GP
            //     |                   |
            //     P                   GC
            //    /   \              /    \
            //   C     ?    ==>     C       P
            //  /  \               /  \    /  \
            // ?   GC             ?  GCL  GCR  ?
            // |   /  \           |
            // ?  GCL  GCR        ?
            grandChild = child.right;
            parent.left = grandChild.right;
            child.right = grandChild.left;
            grandChild.right = parent;
            grandChild.left = child;
            fixSize(parent);
            fixSize(child);
            fixSize(grandChild);
            if (grandParent == null) {
                root = grandChild;
            } else {
                if (grandParent.key.compareTo(grandChild.key) > 0) {
                    grandParent.left = grandChild;
                } else {
                    grandParent.right = grandChild;
                }
            }
            if (child.bf == -1) {
                switch(grandChild.bf) {
                case 1: // ��GCL. GCR�i��S���Τ�GCL�G�@�I
                    parent.bf = -1;
                    child.bf = 0;
                    break;
                case 0: // GCR�MGCL�����B�@�˰�
                    parent.bf = child.bf = 0;
                    break;
                case -1: // ��GCR. GCL�S���άO��GCR�G�@�I
                    parent.bf = 0;
                    child.bf = 1;
                }
                grandChild.bf = 0;
            } else { // when deleted
                child.bf = 1;
                parent.bf = 0;
                grandChild.bf = 1;
            }
        }
    }
}