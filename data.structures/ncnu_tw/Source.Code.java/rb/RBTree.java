/**
 * Program Name: RBTree.java
 * Author: Shiuh-Sheng Yu,
 *         Department of Information Management,
 *         National Chi Nan University, Taiwan
 * Since: 2006/02/07
 * Toolkit: JDK
 */
public class RBTree {
    protected RBTreeNode root;
    public synchronized Object search(String key) {
        int rel;
        RBTreeNode t = root;
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
    // root234 = checking node;
    // parent is parent of root234
    // next is where q goes
    // return true is if rotation has been done
    protected boolean reStructure(RBTreeNode parent, RBTreeNode root234, RBTreeNode next, String key) {
        RBTreeNode p=null; // lower part of checking 234 node;
        RBTreeNode q=null; // next 234 node;
        RBTreeNode r=null; // 234 sibling of q
        p = root234;
        if (next.isRed) { // next and root234 are the same 234 node, so find next 234 node
            p = next;
            if (next.key.compareTo(key) > 0) {
                q = next.left;
            } else if (next.key.compareTo(key) < 0) {
                q = next.right;
            } else {
                //q = next.right;
                return false;
            }
        } else {
            q = next;
        }
        // check if q is a 2-node
        // if q is a 2-node, got a sibling to parent, parent to q
        if (q!=null && (q.left == null || q.left.isRed==false) && (q.right == null || q.right.isRed==false)) {
            // find nearest sibling
            int rcount = 2;
            int pcount = 2;
            if (root234.left != null && root234.left.isRed) {
                pcount++;
            }
            if (root234.right != null && root234.right.isRed) {
                pcount++;
            }
            // find q's sibling r
            if (pcount == 2) {
                //       p
                //     /   \
                //    q     r
                if (p.left == q) {
                    r = p.right;
                } else {
                    r = p.left;
                }
            } else { // when pcount == 3 or 4
                if (p.isRed) { // p is lower of 234 node
                    //   root234
                    //   //
                    //   p
                    //  /  \
                    // q    r
                    if (p.left == q) {
                        r = p.right;
                    } else {
                        r = p.left;
                    }
                } else {
                    //    p(root234)
                    //  /   \\
                    //  q    ?
                    //      /  \
                    //     r
                    if (p.left == q) {
                        r = p.right.left;
                    } else {
                        r = p.left.right;
                    }
                }
            }
            if (r == null) { // should not happen
                System.out.println("error?");
                return false;
            }
            // check r is a 2, 3, or 4 node
            if (r.left != null && r.left.isRed) {
                rcount++;
            }
            if (r.right != null && r.right.isRed) {
                rcount++;
            }
            if (rcount == 2) { // q & r are both 2 node, merge it
                if (pcount == 2) { // case 1
                    //       p               p
                    //      /  \    ==>    //  \\
                    //     q   r           q   r
                    r.isRed = true;
                    q.isRed = true;
                } else { // rcount = 2, and pcount = 3, or pcount = 4
                    if (p.isRed == false) { // pcount == 3
                        RBTreeNode tmp;
                        if (p.right == q) { // case 2
                            //            p            tmp
                            //         //   \         /   \
                            //        tmp   q ==>    ?    p
                            //       /  \               // \\
                            //      ?    r             r     q
                            tmp = p.left;
                            p.left = r;
                            tmp.right = p;
                            if (parent == null) {
                                root = tmp;
                            } else {
                                if (parent.left == p) {
                                    parent.left = tmp;
                                } else {
                                    parent.right = tmp;
                                }
                            }
                        } else { // case 3
                            //          p            tmp
                            //         / \\         /   \
                            //        q   tmp ==>   p   ?
                            //           /  \     // \\   
                            //          r    ?    q   r  
                            tmp = p.right;
                            p.right = r;
                            tmp.left = p;
                            if (parent == null) {
                                root = tmp;
                            } else {
                                if (parent.left == p) {
                                    parent.left = tmp;
                                } else {
                                    parent.right = tmp;
                                }
                            }
                        }
                        tmp.isRed = false;
                        p.isRed = false;
                        q.isRed = true;
                        r.isRed = true;
                    } else { // rcount=2,p is red, case 4
                        // when pcount == 3
                        //          root234        root234
                        //          /   \\         /   \
                        //         ?     p    ==>  ?    p
                        //              /  \          // \\
                        //             r   q         r     q
                        // when pcount == 4
                        //    root234             root234
                        //     //   \\            /     \\
                        //     p     ?  ==>       p      ?
                        //   /  \   / \        //   \\
                        //  q    r ?  ?        r     q
                        // or
                        //    root234             root234
                        //     //   \\            //    \
                        //     ?     p  ==>       ?      p
                        //   /  \   / \                // \\
                        //  ?   ?   q  r               r   q
                        p.isRed = false;
                        r.isRed = true;
                        q.isRed = true;
                    }
                }
            } else if (rcount == 3) {
                if (pcount == 2) {
                    RBTreeNode tmp;
                    if (r.right != null && r.right.isRed) {
                        if (p.left == q) { // case 5
                            //       p              r
                            //      /  \    ==>     / \
                            //     q    r          p   y
                            //         / \\       // \
                            //        tmp  y     q  tmp
                            tmp = r.left;
                            r.left = p;
                            p.right = tmp;
                            q.isRed = true;
                            r.right.isRed = false;
                            if (parent == null) {
                                root = r;
                            } else {
                                if (parent.left == p) {
                                    parent.left = r;
                                } else {
                                    parent.right = r;
                                }
                            }
                        } else { // p.right == q,case 6
                            //       p                y
                            //     /   \    ==>     /   \
                            //    r     q          r     p
                            //   / \\             / \   / \\
                            //  tmp y           tmp t1 t2  q
                            //     / \
                            //    t1 t2
                            RBTreeNode y = r.right;
                            RBTreeNode t1 = y.left;
                            RBTreeNode t2 = y.right;
                            y.left = r;
                            y.right = p;
                            r.right = t1;
                            p.left = t2;
                            q.isRed = true;
                            y.isRed = false;
                            if (parent == null) {
                                root = y;
                            } else {
                                if (parent.left == p) {
                                    parent.left = y;
                                } else {
                                    parent.right = y;
                                }
                            }
                        }
                    } else if (r.left != null && r.left.isRed) {
                        // rcount=3,pcount=2
                        if (p.left == q) { // case 7
                            //       p                   y
                            //     /    \      ==>      /  \
                            //     q     r             p    r
                            //        //   \         // \  / \
                            //        y    tmp       q  t1 t2 tmp
                            //       /  \
                            //      t1  t2
                            RBTreeNode y = r.left;
                            RBTreeNode t1 = y.left;
                            RBTreeNode t2 = y.right;
                            y.right = r;
                            y.left = p;
                            p.right = t1;
                            r.left = t2;
                            q.isRed = true;
                            y.isRed = false;
                            if (parent == null) {
                                root = y;
                            } else {
                                if (parent.left == p) {
                                    parent.left = y;
                                } else {
                                    parent.right = y;
                                }
                            }
                        } else { // p.right == q,case 8
                            //       p                   r
                            //     /   \      ==>      /  \
                            //     r     q             y   p
                            //   // \                     / \\
                            //   y   tmp                 tmp q
                            tmp = r.right;
                            r.right = p;
                            p.left = tmp;
                            r.left.isRed = false;
                            q.isRed = true;
                            if (parent == null) {
                                root = r;
                            } else {
                                if (parent.left == p) {
                                    parent.left = r;
                                } else {
                                    parent.right = r;
                                }
                            }
                        }
                    } else {
                        System.out.println("error!!!");
                    }
                } else { // in rcount==3, pcount == 3 or 4;
                    if (p.isRed) { // pcount could be 3 or 4
                        if (r.left != null && r.left.isRed) {
                            if (p.left == q) { // case 9
                                //     root234         root234
                                //     //   \\          //   \\
                                //     p               y
                                //    /  \           /   \
                                //   q    r         p     r
                                //      // \       // \  /  \
                                //      y   ?     q   t1 t2  ?
                                //     / \
                                //    t1  t2
                                RBTreeNode y = r.left;
                                RBTreeNode t1 = y.left;
                                RBTreeNode t2 = y.right;
                                y.left = p;
                                y.right = r;
                                p.right = t1;
                                r.left = t2;
                                q.isRed = true;
                                p.isRed = false;
                                if (root234.left == p) {
                                    root234.left = y;
                                } else {
                                    root234.right = y;
                                }
                            } else { // p.right == q, case 10
                                //     root234         root234
                                //     //   \          //   \
                                //     p               r
                                //    /  \           /   \
                                //   r    q         t2    p
                                //  // \                /  \\
                                //  t2 t1              t1   q
                                RBTreeNode t1 = r.right;
                                RBTreeNode t2 = r.left;
                                r.right = p;
                                p.left = t1;
                                r.isRed = true;
                                p.isRed = false;
                                q.isRed = true;
                                t2.isRed = false;
                                if (root234.left == p) {
                                    root234.left = r;
                                } else {
                                    root234.right = r;
                                }
                            }
                        } else { // r right is red
                            // rcount=3, pcount=3 or 4
                            if (p.left == q) { // case 11
                                //     root234         root234
                                //     //   \          // \
                                //     p               r
                                //    /  \           /   \
                                //   q    r         p     y
                                //       / \\      // \  
                                //      t1  y     q   t1
                                RBTreeNode t1 = r.left;
                                p.right = t1;
                                r.left = p;
                                r.right.isRed = false;
                                p.isRed = false;
                                q.isRed = true;
                                r.isRed = true;
                                if (root234.left == p) {
                                    root234.left = r;
                                } else {
                                    root234.right = r;
                                }
                            } else { // case 12
                                //     root234        root234
                                //     //   \         //
                                //     p              y
                                //    /  \          /   \
                                //   r   q         r     p
                                //  / \\          /  \  /  \\
                                // ?   y          ?  t1 t2  q
                                //    / \
                                //   t1 t2
                                RBTreeNode y = r.right;
                                RBTreeNode t1 = y.left;
                                RBTreeNode t2 = y.right;
                                y.left = r;
                                y.right = p;
                                r.right = t1;
                                p.left = t2;
                                p.isRed = false;
                                q.isRed = true;
                                if (root234.left == p) {
                                    root234.left = y;
                                } else {
                                    root234.right = y;
                                }
                            }
                        } // end r left is red    
                    } else { // p is black, rcount ==3, pcount==3
                        if (r.left != null && r.left.isRed) {
                            if (p.left == q) { // case 13
                                //       p              y
                                //     /  \\          /   \\
                                //     q   ?          p    ?
                                //        / \       // \  /  \
                                //        r         q  t1 r
                                //      // \             / \
                                //      y   ?           t2 ?
                                //     / \
                                //     t1 t2
                                RBTreeNode y = r.left;
                                RBTreeNode t1 = y.left;
                                RBTreeNode t2 = y.right;
                                y.right = p.right;
                                y.left = p;
                                r.left = t2;
                                p.right = t1;
                                q.isRed = true;
                                y.isRed = false;
                                if (parent == null) {
                                    root = y;
                                } else {
                                    if (parent.left == p) {
                                        parent.left = y;
                                    } else {
                                        parent.right = y;
                                    }
                                }
                            } else { // p.right = q, case 14
                                //       p           r
                                //     //  \       //  \
                                //    y    q      y     p
                                //   / \         / \   /  \\
                                //     r            t1 t2  q
                                //    // \    
                                //   t1  t2   
                                RBTreeNode y = p.left;
                                RBTreeNode t1 = r.left;
                                RBTreeNode t2 = r.right;
                                r.left = y;
                                r.right = p;
                                p.left = t2;
                                y.right = t1;
                                t1.isRed = false;
                                q.isRed = true;
                                if (parent == null) {
                                    root = r;
                                } else {
                                    if (parent.left == p) {
                                        parent.left = r;
                                    } else {
                                        parent.right = r;
                                    }
                                }
                            }
                        } else { // r.right is red
                            // rcount=3, pcount=3
                            if (p.left == q) { // case 15
                                //     p               r
                                //    / \\           /  \\
                                //   q    ?         p    ?
                                //       / \       //\   / \
                                //      r         q  t1 t2
                                //     / \\
                                //    t1  t2
                                RBTreeNode t1 = r.left;
                                RBTreeNode t2 = r.right;
                                r.right = p.right;
                                r.left = p;
                                p.right.left = t2;
                                p.right = t1;
                                q.isRed = true;
                                t2.isRed = false;
                                if (parent == null) {
                                    root = r;
                                } else {
                                    if (parent.left == p) {
                                        parent.left = r;
                                    } else {
                                        parent.right = r;
                                    }
                                }
                            } else { // case 16
                                //     p              y
                                //    // \          //  \
                                //   ?   q          ?    p
                                //  / \            / \  / \\
                                //     r             r  t2 q
                                //    / \\            \
                                //   ?   y            t1
                                //      / \
                                //     t1 t2
                                RBTreeNode y = r.right;
                                RBTreeNode t1 = y.left;
                                RBTreeNode t2 = y.right;
                                y.left = p.left;
                                y.right = p;
                                p.left = t2;
                                r.right = t1;
                                y.isRed = false;
                                q.isRed = true;
                                if (parent == null) {
                                    root = y;
                                } else {
                                    if (parent.left == p) {
                                        parent.left = y;
                                    } else {
                                        parent.right = y;
                                    }
                                }
                            } // end p.left == q;
                        } // end p is black
                    } // end r.right is red
                } // end pcount == 3 or 4;
            } else if (rcount == 4) {
                if (pcount == 2) {
                    if (p.left == q) { // case 17
                        //     p             y
                        //   /   \         /   \
                        //  q    r        p      r
                        //      // \\    // \   / \\
                        //      y   ?    q  t1 t2
                        //     / \
                        //    t1  t2
                        RBTreeNode y = r.left;
                        RBTreeNode t1 = y.left;
                        RBTreeNode t2 = y.right;
                        y.left = p;
                        y.right = r;
                        p.right = t1;
                        r.left = t2;
                        y.isRed = false;
                        q.isRed = true;
                        if (parent == null) {
                            root = y;
                        } else {
                            if (parent.left == p) {
                                parent.left = y;
                            } else {
                                parent.right = y;
                            }
                        }
                    } else { // case 18
                        //     p             y
                        //   /   \         /   \
                        //   r    q        r    p
                        //  // \\        // \   / \\
                        //  ?   y        ?  t1 t2  q
                        //     / \
                        //    t1  t2
                        RBTreeNode y = r.right;
                        RBTreeNode t1 = y.left;
                        RBTreeNode t2 = y.right;
                        y.left = r;
                        y.right = p;
                        r.right = t1;
                        p.left = t2;
                        y.isRed = false;
                        q.isRed = true;
                        if (parent == null) {
                            root = y;
                        } else {
                            if (parent.left == p) {
                                parent.left = y;
                            } else {
                                parent.right = y;
                            }
                        }
                    } // end p.left == q
                } else { // pcount == 3 or 4, in rcount = 4
                    if (p.isRed) {
                        if (p.left == q) { // case 19
                            //    root234       root234
                            //     //             //
                            //    p               y
                            //   / \            /   \
                            //   q  r          p     r
                            //    // \\      // \   / \\
                            //    y          q  t1 t2
                            //   / \
                            //  t1  t2
                            RBTreeNode y = r.left;
                            RBTreeNode t1 = y.left;
                            RBTreeNode t2 = y.right;
                            y.left = p;
                            y.right = r;
                            p.right = t1;
                            r.left = t2;
                            q.isRed = true;
                            p.isRed = false;
                            if (root234.left == p) {
                                root234.left = y;
                            } else {
                                root234.right = y;
                            }
                        } else { // p.right == q, case 20
                            //    root234         root234
                            //     //               //
                            //     p                y
                            //     / \            /    \
                            //    r   q           r     p
                            //   // \\          // \   / \\
                            //       y             t1 t2  q
                            //      / \
                            //     t1  t2
                            RBTreeNode y = r.right;
                            RBTreeNode t1 = y.left;
                            RBTreeNode t2 = y.right;
                            y.left = r;
                            y.right = p;
                            r.right = t1;
                            p.left = t2;
                            p.isRed = false;
                            q.isRed = true;
                            if (root234.left == p) {
                                root234.left = y;
                            } else {
                                root234.right = y;
                            }
                        } // end p.left == q
                    } else { //p is black, pcount must be 3, in rcount=4
                        if (p.left == q) { // case 21
                            //     p              y
                            //    / \\          /   \\
                            //   q   ?         p     ?
                            //      / \       // \  / \
                            //      r     ==> q t1  r
                            //     // \\           / \\
                            //     y              t2
                            //    / \
                            //    t1 t2
                            RBTreeNode y = r.left;
                            RBTreeNode t1 = y.left;
                            RBTreeNode t2 = y.right;
                            y.left = p;
                            y.right = p.right;
                            p.right = t1;
                            r.left = t2;
                            y.isRed = false;
                            q.isRed = true;
                            if (parent == null) {
                                root = y;
                            } else {
                                if (parent.left == p) {
                                    parent.left = y;
                                } else {
                                    parent.right = y;
                                }
                            }
                        } else { // pcount=3,rcount=4,case 22
                            //      p              y
                            //    // \           //  \
                            //    ?  q          ?      p
                            //   / \           / \    / \\
                            //     r      ==>     r  t2  q
                            //   // \\          // \
                            //       y             t1
                            //      / \
                            //     t1 t2
                            RBTreeNode y = r.right;
                            RBTreeNode t1 = y.left;
                            RBTreeNode t2 = y.right;
                            y.left = p.left;
                            y.right = p;
                            r.right = t1;
                            p.left = t2;
                            y.isRed = false;
                            q.isRed = true;
                            if (parent == null) {
                                root = y;
                            } else {
                                if (parent.left == p) {
                                    parent.left = y;
                                } else {
                                    parent.right = y;
                                }
                            }
                        }// end p.left == q
                    } // end p is black
                } // end pcount
            } // end if rcount=2,3,4
            return true;
        } // end if q is 2 node;
        return false;
    }
    public synchronized void delete(String key) {
        if (search(key) == null) {
            return;
        }
        RBTreeNode target = root; // target to be deleted
        RBTreeNode parent = null; // parent, grand parent and grand grand parent of actual target
        RBTreeNode gp = null;
        while (target != null) {
            RBTreeNode next = null; // below target in RBTree
            if (target.key.compareTo(key) > 0) { // 目標在左邊
                next = target.left;
            } else if (target.key.compareTo(key) < 0) { // 目標在右邊
                next = target.right;
            } else {
                break;
            }
            if (next == null) {
                return; // not found
            }
            if (target.isRed == false) { // a new 234 node found
                // if target is a leaf of 234 node
                if ((target.left == null || (target.left.isRed && target.left.left==null && target.left.right==null)) && (target.right == null || (target.right.isRed && target.right.left == null && target.right.right == null))) {
                    if (next == target.left) { // 目標在左邊
                        if (next.key.compareTo(key) == 0) {
                            target.left = null;
                        }
                    } else if (next == target.right) { // 目標在右邊
                        if (next.key.compareTo(key) == 0) {
                            target.right = null;
                        }
                    } else {
                        if (target.left != null) {
                            target.key = target.left.key;
                            target.data = target.left.data;
                            target.left = null;
                        } else if (target.right != null) {
                            target.key = target.right.key;
                            target.data = target.right.data;
                            target.right = null;
                        } else {
                            if (parent != null) {
                                if (parent.left == target) {
                                    parent.left = null;
                                } else {
                                    parent.right = null;
                                }
                            } else {
                                root = null;
                            }
                        }
                    }
                    return;
                } else { // target is not a leaf, then check next level
                    if (reStructure(parent, target, next, key)) {
                        // parent may changed, how to solve?
                        // don't worry, check taret if enough
                        continue;
                    }
                }
            } // otherwise just to traverse down
            if (next == target.left) { // 目標在左邊
                gp = parent;
                parent = target;
                target = target.left;
            } else if (next == target.right) { // 目標在右邊
                gp = parent;
                parent = target;
                target = target.right;
            } else {
                break;
            }
        }
        if (target == null) {
            return;
        }
        RBTreeNode actual = target; // 用來取代的節點,代表實際上要刪的節點
        
        // actual can't be leaf now
        if (target.left != null && target.right != null) { //要刪的節點有兩個子樹,必須找出replace node
            actual = target.left;
            while (actual.right != null) {
                actual = actual.right;
            }
            String actualKey = actual.key;
            if (target.isRed) { // target 不是root234, 倒回去一層找
                reStructure(gp, parent, target, actualKey);
            } else {
                reStructure(parent, target, target.left, actualKey);
            }
            // 找出左子樹最大的
            parent = target;
            actual = target.left;
            // actual 為 black, 且不是234 leaf
            while (actual.right != null) {
                //if (actual.isRed==false && !(actual.left == null || (actual.left.isRed && actual.left.left==null && actual.left.right==null)) && (actual.right == null || (actual.right.isRed && actual.right.left == null && actual.right.right == null))) {
                if (actual.isRed==false) {
                    if (reStructure(parent, actual, actual.right, key)) {
                        continue;
                    }
                }
                parent = actual;
                actual = actual.right;
            }
        }
        if (actual != target) { // 取代模式
            target.key = actual.key;
            target.data = actual.data;
            // 設定replace父節點的link
            if (parent.left == actual) {
                if (actual.right != null) {
                    parent.left = actual.right;
                } else {
                    parent.left = actual.left;
                }
                if (parent.left != null) {
                    parent.left.isRed = false;
                }
            } else {
                if (actual.right != null) {
                    parent.right = actual.right;
                } else {
                    parent.right = actual.left;
                }
                if (parent.right != null) {
                    parent.right.isRed = false;
                }
            }
        } else if (parent == null) { // 刪到根節點
            if (actual.right != null) {
                root = actual.right;
            } else {
                root = actual.left;
            }
            if (root != null) {
                root.isRed = false;
            }
        } else if (parent.left == actual) { // 刪到左子樹
            if (actual.right != null) {
                parent.left = actual.right;
            } else {
                parent.left = actual.left;
            }
            if (parent.left != null) {
                parent.left.isRed = false;
            }
        } else { // 刪到右子樹
            if (actual.right != null) {
                parent.right = actual.right;
            } else {
                parent.right = actual.left;
            }
            if (parent.right != null) {
                parent.right.isRed = false;
            }
        }
    }
    protected void LLRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        RBTreeNode gpRight = gp.right;
        gp.right = ggp;
        ggp.left = gpRight;
        gp.isRed = false;
        ggp.isRed = true;
        if (gggp != null) {
            if (gggp.left == ggp) {
                gggp.left = gp;
            } else {
                gggp.right = gp;
            }
        } else {
            root = gp;
        }
    }
    protected void RRRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        RBTreeNode gpLeft = gp.left;
        gp.left = ggp;
        ggp.right = gpLeft;
        gp.isRed = false;
        ggp.isRed = true;
        if (gggp != null) {
            if (gggp.left == ggp) {
                gggp.left = gp;
            } else {
                gggp.right = gp;
            }
        } else {
            root = gp;
        }
    }
    protected void LRRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        RBTreeNode pRight = p.right;
        RBTreeNode pLeft = p.left;
        p.left = gp;
        p.right = ggp;
        gp.right = pLeft;
        ggp.left = pRight;
        p.isRed = false;
        ggp.isRed = true;
        if (gggp != null) {
            if (gggp.left == ggp) {
                gggp.left = p;
            } else {
                gggp.right = p;
            }
        } else {
            root = p;
        }
    }
    protected void RLRotate(RBTreeNode gggp, RBTreeNode ggp, RBTreeNode gp, RBTreeNode p) {
        RBTreeNode pRight = p.right;
        RBTreeNode pLeft = p.left;
        p.left = ggp;
        p.right = gp;
        gp.left = pRight;
        ggp.right = pLeft;
        p.isRed = false;
        ggp.isRed = true;
        if (gggp != null) {
            if (gggp.left == ggp) {
                gggp.left = p;
            } else {
                gggp.right = p;
            }
        } else {
            root = p;
        }
    }
    public synchronized void insert(String key, Object d) {
        if (root == null) {
            root = new RBTreeNode();
            root.data = d;
            root.key = key;
            return;
        }
        if (search(key) != null) { // already exists
            return;
        }
        RBTreeNode gp = null;
        RBTreeNode ggp = null;
        RBTreeNode gggp = null;
        RBTreeNode parent = root;
        while (true) {
            // change color if both side are red. Correspond to 2-3-4 Tree with 3 item in a node
            if (parent.left != null && parent.left.isRed && parent.right != null && parent.right.isRed) {
                parent.left.isRed = false;
                parent.right.isRed = false;
                if (parent != root) {
                    parent.isRed = true;
                }
                if (parent.isRed && gp != null && gp.isRed) {    
                    if (ggp.left == gp && gp.left == parent) { // LL
                        LLRotate(gggp, ggp, gp, parent);
                    } else if (ggp.right == gp && gp.right == parent) { // RR
                        RRRotate(gggp, ggp, gp, parent);
                    } else if (ggp.left == gp && gp.right == parent) { // LR
                        LRRotate(gggp, ggp, gp, parent);
                        if (parent.key.compareTo(key) > 0) { // 要到左子樹 
                            //      ggp           p     <--ggp
                            //      /            /  \
                            //      gp          gp  ggp   
                            //       \           \
                            //        p          dest   <--p
                            //       /
                            //      dest
                            RBTreeNode dest = gp.right;
                            ggp = parent;
                            parent = dest;
                            continue;
                        } else {
                            //      ggp           p     <--ggp
                            //      /            /  \
                            //      gp          gp  ggp   
                            //       \              /
                            //        p            dest   <--p
                            //         \
                            //         dest
                            RBTreeNode dest = ggp.left;
                            gp = ggp;
                            ggp = parent;
                            parent = dest;
                            continue;
                        }
                    } else { // RL
                        RLRotate(gggp, ggp, gp, parent);
                        if (parent.key.compareTo(key) > 0) { // 要到左子樹
                            //      ggp             p     <--ggp
                            //        \            /  \
                            //        gp          ggp  gp   
                            //       /              \
                            //      p               dest   <--p
                            //      /
                            //    dest
                            RBTreeNode dest = ggp.right;
                            gp = ggp;
                            ggp = parent;
                            parent = dest;
                            continue;
                        } else {
                            //      ggp             p     <--ggp
                            //        \            /  \
                            //        gp          ggp  gp   
                            //       /                /
                            //      p               dest   <--p
                            //       \
                            //       dest
                            RBTreeNode dest = gp.left;
                            ggp = parent;
                            parent = dest;
                            continue;
                        }
                    }
                }
            }
            if (parent.key.compareTo(key) > 0) { // 放到左子樹
                if (parent.left == null) { // 走到leaf
                    parent.left = new RBTreeNode();
                    parent.left.data = d;
                    parent.left.key = key;
                    parent.left.isRed = true;
                    if (parent.isRed) { // need rotation
                        if (gp.left == parent) { // LL
                            LLRotate(ggp, gp, parent, parent.left);
                        } else { // RL
                            RLRotate(ggp, gp, parent, parent.left);
                        }
                    }
                    return;
                } else {
                    gggp = ggp;
                    ggp = gp;
                    gp = parent;
                    parent = parent.left;
                }
            } else { // 放到右子樹
                if (parent.right == null) {
                    parent.right = new RBTreeNode();
                    parent.right.data = d;
                    parent.right.key = key;
                    parent.right.isRed = true;
                    if (parent.isRed) { // need rotation
                        if (gp.right == parent) { // RR
                            RRRotate(ggp, gp, parent, parent.right);
                        } else { // LR
                            LRRotate(ggp, gp, parent, parent.right);
                        }
                    }
                    return;
                } else {
                    gggp = ggp;
                    ggp = gp;
                    gp = parent;
                    parent = parent.right;
                }
            } // end if left or right
        } // end while
    }
}