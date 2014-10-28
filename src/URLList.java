/**
 * Created by tvories on 10/15/14.
 */
public class URLList {

    Node head;
    int  count = 0;

    public void add(String data) {
        //TODO: Verify url doesn't already exist
        Node n = new Node();
        n.setData(data);
        //insertion at head of list
        n.setNext(head);
        head = n;
        count++;
    }

    public void print() {
        Node curr = head;
        while (curr != null) {
            System.out.println("" + curr);
            curr = curr.getNext();
        }
    }

    public String[] getAll(){
        String[] stArr = new String[count];
        Node curr = head;

        for(int i = 0; i < count; i++) {
            stArr[i] = curr.getData();
            curr = curr.getNext();
        }
        return stArr;
    }

    //TODO: Probably not needed for this project
    public String delete(String who) {
        String result = null;
        if (head == null) return result;
        if (who.equals(head.toString())) {
            //found in first position
            result = head.getData();
            head = head.getNext();
            count--;
        } else {
            for (Node curr = head; curr.getNext() != null; curr = curr.getNext()) {
                //compare other to data at curr
                Node guy = curr.getNext();
                if (who.equals(guy.toString())) {
                    result = guy.getData();
                    curr.setNext(guy.getNext());
                    count--;
                    break;
                }
            }
        }
        return result;
    }

    public int size() {
        return count;
    }

    //TODO: May want to turn this into boolean
    public Object find(Object other) {
        for (Node curr = head; curr != null; curr = curr.getNext()) {
            //compare other to data at curr
            if (other.equals(curr.toString())) return curr.getData();
        }
        return null;
    }

    /* ---------------------------------------------------------------
    *   Node Class Follows
    *  -------------------------------------------------------------*/
    /**
     *	Node is a carrier class for
     * a linked list
     */
    private class Node {

        private String data;
        private Node next;

        public Node() {
            data = null;
            next = null;
        }

        public Node(String obj, Node other) {
            data = obj;
            next = other;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node other) {
            next = other;
        }

        public String getData() {
            return data;
        }

        public void setData(String obj) {
            data = obj;
        }

        /*public String toString() {
            return data.toString();
        }*/
    } // End Node class
}
