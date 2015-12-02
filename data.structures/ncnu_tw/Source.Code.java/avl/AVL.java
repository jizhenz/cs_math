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
    private AVLNode[] stack = new AVLNode[100]; // 用來儲存insert或delete所經過的路徑
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
                stack[top++] = parent; // 記住所有經過的two children node
                stack[top++] = deleted;
            }
            if (deleted.key.compareTo(key) > 0) { // 目標在左邊
                deleted.lsize--;
                parent = deleted;
                deleted = deleted.left;
            } else if (deleted.key.compareTo(key) < 0) { // 目標在右邊
                parent = deleted;
                deleted = deleted.right;
            } else {
                break;
            }
        }
        AVLNode actual = deleted; // 用來取代的節點,代表實際上要刪的節點
        if (deleted.left != null && deleted.right != null) { //要刪的節點有兩個子樹,必須找出replace node
            // 找出左子樹最大的
            actual = deleted.left;
            parent = deleted;
            deleted.lsize--;
            actual.size--;
            if (actual.left != null && actual.right != null) {
                stack[top++] = parent; // 記住所有經過的two children node
                stack[top++] = actual;
            }
            while (actual.right != null) {
                parent = actual;
                actual = actual.right;
                actual.size--;
                if (actual.left != null && actual.right != null) {
                    stack[top++] = parent; // 記住所有經過的two children node
                    stack[top++] = actual;
                }
            }
        }
        if (actual != deleted) { // 取代模式
            deleted.key = actual.key;
            deleted.data = actual.data;
            // 設定replace父節點的link
            if (parent.left == actual) {
                parent.left = actual.left;
                parent.bf--;
            } else {
                parent.right = actual.left;
                parent.bf++;
            }
        } else if (parent == null) { // 刪到根節點
            if (deleted.right != null) {
                root = deleted.right;
            } else {
                root = deleted.left;
            }
            return;
        } else if (parent.left == deleted) { // 刪到左子樹
            if (deleted.right != null) {
                parent.left = deleted.right;
            } else {
                parent.left = deleted.left;
            }
            parent.bf--;
        } else { // 刪到右子樹
            if (deleted.right != null) {
                parent.right = deleted.right;
            } else {
                parent.right = deleted.left;
            }
            parent.bf++;
        }
        // rotate雖然可平衡子樹,但是子樹的高度一定會降低,因此要一直往上處理
        while (top > 0) {
            AVLNode checking = stack[--top];
            stack[top] = null;
            AVLNode checkingParent = stack[--top];
            stack[top] = null;
            if (checking.key.compareTo(actual.key) >= 0) { // actual在左子樹.由於replace情況下,actual.key會放到deleted,因此可能會相等
                // (1)checking.left由平衡變不平衡,不會縮減高度, 因此完成delete
                // (2)由於是刪除動作,checking.left可能在前面被刪掉,此時checking.bf也需要調整
                if (checking.left != null && checking.left.bf != 0) {
                    return;
                }
                if (checking != parent) { // 避免做兩次. 因為parent可能是原來有two children而被放到stack內
                    checking.bf--;
                }
                if (checking.bf < -1) {
                    rightRotate(checkingParent, checking);
                }
            } else { // actual在右子樹
                // (1)checking.left由平衡變不平衡,不會縮減高度, 因此完成delete
                // (2)由於是刪除動作,checking.right可能在前面被刪掉,此時checking.bf也需要調整
                if (checking.right != null && checking.right.bf != 0) {
                    return;
                }
                if (checking != parent) { // 避免做兩次. 因為parent可能是原來有two children而被放到stack內
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
            if (parent.key.compareTo(key) > 0) { // 放到左子樹
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
                    stack[top++] = grandParent; // 紀錄經過的路徑
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
                    stack[top++] = grandParent; // 紀錄經過的路徑
                    stack[top++] = parent;
                    grandParent = parent;
                    parent = parent.right;
                }
            }
        }
        // 只要平衡或rotate就可以降低樹的高度,因此最多rotate一次即可
        while (top > 0) { // 檢查所有走過的節點
            AVLNode checking = stack[--top];
            stack[top] = null;
            AVLNode checkingParent = stack[--top];
            stack[top] = null;
            if (checking.key.compareTo(key) > 0) { // 加到checking左邊
                if (++checking.bf == 0) {
                    return;
                } else if (checking.bf > 1) {
                    leftRotate(checkingParent, checking);
                    return;
                }
            } else { // 加到checking右邊
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
        if (child.bf == 0) { // 發生在delete的特殊情況
            // 由left child of grandChild為null, 就不能用RL
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
            } else { // child.bf = 0 時, 一定有CL,CR且沒有?
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
        } else { // RL rotation. 下圖GCL or GCR可能都有
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
            // 改變parent size;
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
                case 1: // 有GCL. 沒有GCR, 或是GCR比GCL矮一點
                    parent.bf = 0;
                    child.bf = -1;
                    break;
                case 0: // 有GCR和GCL, 且一樣高
                    parent.bf = child.bf = 0;
                    break;
                case -1: // 有GCR, 沒有GCL, 或GCL比GCR矮一點
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
        if (child.bf == 0) { // 發生在delete的特殊情況
            // 當right child of grandChild為null時,就不能用LR, 否則不平衡
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
        if (LL) { // LL roation. 下圖可能沒有CR(同時沒有?), 或CL比CR高一點
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
            } else { // 有CL,CR且沒有?
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
        } else { // LR roation. 下圖GCL or GCR可能都有
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
                case 1: // 有GCL. GCR可能沒有或比GCL矮一點
                    parent.bf = -1;
                    child.bf = 0;
                    break;
                case 0: // GCR和GCL都有且一樣高
                    parent.bf = child.bf = 0;
                    break;
                case -1: // 有GCR. GCL沒有或是比GCR矮一點
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