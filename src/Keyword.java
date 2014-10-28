/**
 * Created by tvories on 10/24/14.
 */
public class Keyword implements Comparable<Keyword> {
    private String key;
    private URLList urls;

    public Keyword(String key) {
        this.key = key;
        urls = new URLList();
    }

    public String getKey() {
        return key;
    }

    public URLList getUrls() {
        return urls;
    }

    public String[] getArrayUrls() {
        return urls.getAll();
    }

    public void printUrls() {
        String[] toPrint = urls.getAll();
        for (int i = 0; i < toPrint.length; i++) {
            System.out.println(toPrint[i]);
        }
    }

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

    public void addUrl(String url) {
        urls.add(url);
    }

    public int compareTo(Keyword other) {
        return key.compareToIgnoreCase(other.getKey());
    }
}
