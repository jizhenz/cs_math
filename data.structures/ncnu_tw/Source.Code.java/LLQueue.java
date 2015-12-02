/**
 * Program Name: LLQueue.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2008/01/02
 * Purpose: Linked List Queue
 */
public class LLQueue {
    class LLNode {
        Object data;
        LLNode link;
    }
    private LLNode head, tail;
    private int size;
    public void add(Object x) {
        LLNode tmp = new LLNode();
        tmp.data = x;
        if (tail == null) {
            head = tmp;
        } else {
            tail.link = tmp;
        }
        tail = tmp;
        size++;
    }
    public Object delete() {
        if (size == 0) return null;
        LLNode tmp = head;
        head = head.link;
        if (head == null) tail = null;
        size--;
        return tmp.data;
    }
    public boolean isEmpty() {
        return size == 0;
    }
}