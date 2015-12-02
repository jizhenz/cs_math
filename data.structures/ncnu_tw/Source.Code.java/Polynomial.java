/**
 * Author: Shiuh-Sheng Yu
 *         Department of Information Management
 *         National Chi Nan University
 * Purpose: Using cicularly Linked List with head node to represent polynomials
 *          Using available list to increase speed
 * Since: 2006/12/03
 */
class PolyNode {
    int expo, coef;
    PolyNode link;
}
public class Polynomial {
    private static PolyNode avail;
    PolyNode tail;
    Polynomial() {
        tail = getNode(); // create dummy node
        tail.expo = -1;
        tail.link = tail;
    }
    PolyNode getNode() {
        if (avail == null) {
            return new PolyNode();
        }
        PolyNode tmp = avail;
        avail = avail.link;
        return tmp;
    }
    void attach(int expo, int coef) {
        PolyNode tmp = getNode();
        tmp.expo = expo;
        tmp.coef = coef;
        tmp.link = tail.link;
        tail.link = tmp;
        tail = tmp;
    }
    Polynomial mul(Polynomial y) {
        PolyNode a = this.tail.link.link;
        PolyNode b = y.tail.link.link;
        Polynomial sum = new Polynomial();
        while (b != y.tail.link) {
            Polynomial tmp = new Polynomial();
            for (PolyNode scan = a; scan != this.tail.link; scan = scan.link) {
                tmp.attach(b.expo+scan.expo, b.coef*scan.coef);
            }
            Polynomial rel = sum.add(tmp);
            sum.erase();
            tmp.erase();
            sum = rel;
            b = b.link;
        }
        return sum;
    }
    Polynomial add(Polynomial y) {
        PolyNode a = this.tail.link.link;
        PolyNode b = y.tail.link.link;
        Polynomial d = new Polynomial();
        for (;;) {
            if (a.expo == b.expo) {
                if (a == this.tail.link) {
                    break;
                } else {
                    int sum = a.coef + b.coef;
                    if (sum != 0) d.attach(a.expo, sum);
                    a = a.link;
                    b = b.link;
                }
            } else if (a.expo < b.expo) {
                d.attach(b.expo, b.coef);
                b = b.link;
            } else {
                d.attach(a.expo, a.coef);
                a = a.link;
            }
        } 
        return d;
    }
    void erase() {
        PolyNode tmp = tail.link;
        tail.link = avail;
        avail = tmp;
    }
    Polynomial print() {
        PolyNode tmp = tail.link.link;
        while (tmp != tail.link) {
            System.out.print("("+tmp.coef+","+tmp.expo+") ");
            tmp = tmp.link;
        }
        System.out.println();
        return this;
    }
    public static void main(String[] argv) {
        Polynomial x = new Polynomial();
        Polynomial y = new Polynomial();
        Polynomial rel;
        x.attach(14, 3);
        x.attach(13, 2);
        x.attach(10, -4);
        x.attach(8, 2);
        x.attach(6, 2);
        x.attach(5, -2);
        x.attach(4, 5);
        x.attach(3, -2);
        x.attach(0, 1);
        y.attach(14, 8);
        y.attach(10, -3);
        y.attach(6, 10);
        y.attach(3, 3);
        y.attach(1, 2);
        y.attach(0, -4);
        x.print();
        y.print();
        x.add(y).print().erase();
        x.mul(y).print().erase();
    }
}