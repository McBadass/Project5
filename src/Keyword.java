//-----------------------------------------------------------------------
// Class:			Keyword.java
//
// Author:			Taylor Vories
//
// Class:			CS 2050
//
// Description:		Class for Keyword Objects.  Simple object to store
//                  Keyword information and which URLs it showed up in
//
// Files:		    MainApp.java
//
//-----------------------------------------------------------------------
public class Keyword implements Comparable<Keyword> {
    private String key;
    private URLList urls;

    /**
     * Constructor
     * @param key String value for keyword
     */
    public Keyword(String key) {
        this.key = key;
        urls = new URLList();
    }

    /**
     * Getter
     * @return Returns string
     */
    public String getKey() {
        return key;
    }

    /**
     * Getter
     * @return Returns list of URLs
     */
    public URLList getUrls() {
        return urls;
    }

    /**
     * Getter
     * @return Return array of URLs
     */
    public String[] getArrayUrls() {
        return urls.getAll();
    }

    /**
     * Prints urls to console.
     */
    public void printUrls() {
        String[] toPrint = urls.getAll();
        for (int i = 0; i < toPrint.length; i++) {
            System.out.println(toPrint[i]);
        }
    }

    /**
     * Prints out keyword and urls to console.
     */
    public void print() {
        System.out.println("**********************************");
        System.out.println();
        System.out.printf("Keyword: %s\n", key);
        System.out.printf("URLs where %s appears:\n", key);
        System.out.println();
        String[] urprint = urls.getAll();
        for (int i = 0; i < urls.size(); i++) {
            System.out.println(urprint[i]);
        }
        System.out.println("----------------------------------\n");
    }

    /**
     * Method to add a url to the url list.
     * @param url
     */
    public void addUrl(String url) {
        urls.add(url);
    }

    /**
     * Compares one keyword to another
     * @param other The keyword to compare to.
     * @return Returns -10 if less than, 10 if more than, and 0 if equal to.
     */
    public int compareTo(Keyword other) {
        return key.compareToIgnoreCase(other.getKey());
    }
}
