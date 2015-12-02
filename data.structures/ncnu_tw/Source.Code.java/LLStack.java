/**
 * Program Name: LLStack.java
 * Author: Shiuh-Sheng Yu,
 * Department of Information Management
 * National Chi Nan University
 * Since: 2008/01/02
 * Purpose: Linked List Stack
 */
public class LLStack {
    private LLNode top;
    private int size;
    class LLNode {
        Object data;
        LLNode link;
    }
    public void add(Comparable x) {
        LLNode tmp = new LLNode();
        tmp.data = x;
        tmp.link = top;
        top = tmp;
        size++;
    }
    public Object delete(){
        if (size == 0) return null;
        LLNode tmp = top;
        top = top.link;
        size--;
        return tmp.data;
    }
    public boolean isEmpty() {
        return size == 0;
    }
}