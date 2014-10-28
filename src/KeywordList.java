/**
 * Created by tvories on 10/15/14.
 */
public class KeywordList {

    Node head;
    int  count = 0;
    public final boolean DEBUG = true;

    public void add(String data) {
        Node n = new Node();
        n.setData(data);
        //insertion at head of list
        n.setNext(head);
        head = n;
        count++;
    }

    public void add(Keyword data) {
        add(data.getKey());
    }

    public void addAtFront(Node toAdd) {
        toAdd.setNext(head);
        head = toAdd;
        count++;
    }

    public void append(String data, String origUrl) {
        Node n = new Node();
        Keyword toAdd = new Keyword(data);
        n.setData(toAdd);

        // If list is empty
        if (head == null) {
            if (DEBUG) {
                System.out.printf("List is empty, adding keyword %s to list.\n", n.getData().getKey());
            }
            n.getData().addUrl(origUrl);
            head = n;
        } else { // List not empty, check for first item
            if (DEBUG) {
                System.out.printf("Keyword list is not empty, testing cases for %s.\n", n.getData().getKey());
            }
            Node curr = head;
            if (n.getData().compareTo(curr.getData()) <= 0) { // If less than or equal to first item
                if (DEBUG) {
                    System.out.printf("Still searching for proper placement of %s.  It is less than %s.\n", n.getData().getKey(), curr.getData().getKey());
                }
                if (n.getData().compareTo(curr.getData()) != 0) {
                    if (DEBUG) {
                        System.out.printf("%s is less than first item in list, which is %s. Placing in front.\n", n.getData().getKey(), curr.getData().getKey());
                    }
                    n.getData().addUrl(origUrl);
                    addAtFront(n); //Adds to the front
                } else if (n.getData().compareTo(curr.getData()) == 0) { // Add url to existing keyword
                    if (DEBUG) {
                        System.out.printf("First keyword is the same! %s equals %s\n", n.getData().getKey(), curr.getData().getKey());
                    }
                    curr.getData().addUrl(origUrl);
                }
            } else {
                // Stops if it runs to end of list or finds its spot
                while (curr.getNext() != null) {
                    if (n.getData().compareTo(curr.getNext().getData()) > 0) { // If keyword is greater than current, move forward
                        if (DEBUG) {
                            System.out.printf("Keyword %s is bigger than %s.  Moving forward.\n", n.getData().getKey(), curr.getData().getKey());
                        }
                        // If n is greater than current.next, keep moving
                        curr = curr.getNext();
                    } else { // We're inbetween two points
                        if (n.getData().compareTo(curr.getNext().getData()) != 0) {
                            // If the keyword DOES NOT exist
                            n.getData().addUrl(origUrl);
                            n.setNext(curr.getNext());
                            curr.setNext(n);
                            count++;
                        } else if (n.getData().compareTo(curr.getNext().getData()) == 0){ // Add url to existing keyword
                            System.out.println("I'm equal!");
                            curr.getNext().getData().addUrl(origUrl);
                            return;
                        }
                        break;
                    }
                }
                if (DEBUG) {
                    System.out.printf("Reached the end of the list.  Testing %s against last item %s.\n", n.getData().getKey(), curr.getData().getKey());
                }
                if (n.getData().compareTo(curr.getData()) >= 0) {
                    // If the keyword goes to end of list, compare to last item
                    if (n.getData().compareTo(curr.getData()) != 0) {
                        if (DEBUG) {
                            System.out.printf("Keyword %s is not equal to last item, %s.  Adding at end.\n", n.getData().getKey(), curr.getData().getKey());
                        }
                        // If they aren't equal
                        n.getData().addUrl(origUrl);
                        curr.setNext(n);
                        count++;
                    } else {
                        if (DEBUG) {
                            System.out.printf("Keyword %s is equal to %s, adding URL ONLY.", n.getData().getKey(), curr.getData().getKey());
                        }
                        curr.getData().addUrl(origUrl);
                    }
                }
            }
        }
    }

    public void append(Keyword data, String origUrl) {
        append(data.getKey(), origUrl);
    }

    public void print() {
        Node curr = head;
        while (curr != null) {
            curr.getData().print();
            curr = curr.getNext();
        }
    }

    public Keyword[] getAll() {
        Keyword[] toReturn = new Keyword[count];
        Node curr = head;
        for (int i = 0; i < count; i++) {
            toReturn[i] = curr.getData();
            curr = curr.getNext();
        }
        return toReturn;
    }

    /*
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
            }
        }
        return result;
    }
    */

    public Keyword search(String key) {
        Keyword toReturn = null;
        // If list is empty
        if (head == null) {
            return toReturn;
        }
        // If first item
        if (key.equalsIgnoreCase(head.getData().getKey())) {
            toReturn = head.getData();
        } else { // Else traverse list
            Node curr = head;
            while (curr.getNext() != null) {
                if (key.compareToIgnoreCase(curr.getData().getKey()) == 0)
                {
                    // If found
                    toReturn = curr.getData();
                    return toReturn;
                } else
                { // Else keep moving
                    curr = curr.getNext();
                }
            }
        }
        return toReturn;
    }

    public int size() {
        return count;
    }

    /*
    public String find(String other) {
        for (Node curr = head; curr != null; curr = curr.getNext()) {
            //compare other to data at curr
                    break;
                }
            if (other.equals(curr.toString())) return curr.getData();
        }
        return null;
    }
    */

    /**
     *	Node is a carrier class for
     * a linked list
     */
    public class Node {
        private Keyword keyword;
        private Node next;

        public Node() {
            keyword = null;
            next = null;
        }

        //TODO: May not need this
        public Node(Keyword obj,Node other) {
            keyword = obj;
            next = other;
        }

        private Node getNext() {
            return next;
        }

        private void setNext(Node other) {
            next = other;
        }

        private Keyword getData() {
            return keyword;
        }

        private void setData(Keyword obj) {
            keyword = obj;
        }

        private void setData(String obj) {
            Keyword newKey = new Keyword(obj);
            keyword = newKey;
        }

        private void setUrl(String url) {
            keyword.addUrl(url);
        }
    }
}