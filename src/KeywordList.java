//-----------------------------------------------------------------------
// Class:			KeywordList.java
//
// Author:			Taylor Vories
//
// Class:			CS 2050
//
// Description:		Linked List specifically designed for Keyword Objects.
//                  It uses a sorted insert method.
//
// Files:		    KeywordList.java, Keyword.java
//
//-----------------------------------------------------------------------

import java.util.ArrayList;

public class KeywordList {

    Node head;
    int  count = 0;                         // Total number of items in list
    public final boolean DEBUG = false;     // Flag to output diagnostics to console if true

    /**
     * Adds data to the front of the Linked List.
     * @param toAdd Node item to add.
     */
    public void addAtFront(Node toAdd) {
        toAdd.setNext(head);
        head = toAdd;
        count++;
    }

    /**
     * Sorted insert for Keyword Linked List. Includes url the keyword was found in.
     * @param data String of the data to add
     * @param origUrl URL that came with the keyword
     */
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
        } else {                                                            // List not empty, check for first item
            if (DEBUG) {
                System.out.printf("Keyword list is not empty, testing cases for %s.\n", n.getData().getKey());
            }
            Node curr = head;
            if (n.getData().compareTo(curr.getData()) <= 0) {               // If less than or equal to first item
                if (DEBUG) {
                    System.out.printf("Still searching for proper placement of %s.  It is less than %s.\n", n.getData().getKey(), curr.getData().getKey());
                }
                if (n.getData().compareTo(curr.getData()) != 0) {
                    if (DEBUG) {
                        System.out.printf("%s is less than first item in list, which is %s. Placing in front.\n", n.getData().getKey(), curr.getData().getKey());
                    }
                    n.getData().addUrl(origUrl);
                    addAtFront(n);                                          //Adds to the front
                } else if (n.getData().compareTo(curr.getData()) == 0) {    // Add url to existing keyword
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
                    } else {                                                // We're inbetween two points
                        if (n.getData().compareTo(curr.getNext().getData()) != 0) {
                            // If the keyword DOES NOT exist
                            n.getData().addUrl(origUrl);
                            n.setNext(curr.getNext());
                            curr.setNext(n);
                            count++;
                        } else if (n.getData().compareTo(curr.getNext().getData()) == 0){ // Add url to existing keyword
                            if (DEBUG) {
                                System.out.println("I'm equal!");
                            }
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

    /**
     * Inserts a keyword at front of the list
     * @param data Keyword to insert
     * @param origUrl URL keyword was found in
     */
    public void append(Keyword data, String origUrl) {
        append(data.getKey(), origUrl);
    }

    /**
     * Method to print out the entire list to console.
     */
    public void print() {
        Node curr = head;
        while (curr != null) {
            curr.getData().print();
            curr = curr.getNext();
        }
    }

    /**
     * Method to retrieve the entire list as an ArrayList.
     * @return Returns Linked List as an Array List
     */
    public ArrayList<Keyword> getAll() {
        ArrayList<Keyword> toReturn = new ArrayList<Keyword>();
        Node curr = head;
        while (curr != null) {
            toReturn.add(curr.getData());
        }
        //Code below caused null error.  Not sure how I set the counting wrong.
        /*for (int i = 0; i < count; i++) {
            toReturn[i] = curr.getData();
            curr = curr.getNext();
        }*/
        return toReturn;
    }

    /**
     * Method to search for a specific keyword in the list.
     * @param key String value of a keyword
     * @return Returns a Keyword object if found with the URLs.
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

    /**
     * Getter
     * @return Returns size of the Linked List
     */
    public int size() {
        return count;
    }

    /***************************************************************************************/
    //                  Here be the Node class
    //**************************************************************************************/


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