import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by tvories on 10/22/14.
 */
public class MainApp {

    private String website = "http://rowdy.msudenver.edu/~gordona/cs2050/hw/pgm05F2014-searchEngineProject.html";
    private String[] stopWords; //List of words to ignore
    private final int MAXSITES = 30; // Maximum number of sites to traverse
    private int siteCount = 0;
    private int currentUrl = 0;

    public static void main(String[] args) {
        MainApp runme = new MainApp();
    }

    public MainApp() {
        KeywordList kList = new KeywordList();
        Page p = null;

        //Initiate list of stopwords to ignore
        File stopFile = new File("stopwords.txt");
        try {
            BufferedReader getline = new BufferedReader(new FileReader(stopFile));
            Scanner fileScanner = new Scanner(getline);
            stopWords = fileScanner.nextLine().split(",");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find stopwords.txt, make sure it's in same directory as program.");
            System.exit(0);
        } // Stopwords list is initiated at this point

        try {
            p = new Page(website);
        } catch (Exception e) {
            System.out.println("How'd you fuck this one up?");
        }

        if (p.pageDone()) {
            System.out.println("All done with the page you gave me.");
        }
        while (p != null && !p.pageDone()) {
            while (p != null && !p.pageDone()) {
                String pLine = p.getLine();

                // Clean up pLine and remove HTML tags
                pLine = cleanHTML(pLine);

                // Break up each word by " " whitespace
                String[] pLineArr = pLine.split(" ");

                // Loop through line and add to keyword list
                for (int i = 0; i < pLineArr.length; i++) {
                    pLineArr[i] = cleanString(pLineArr[i]);
                    pLineArr[i] = pLineArr[i].replaceAll(" ", ""); // Get rid of whitespace
                    if (isValidKey(pLineArr[i])) { // If it's not a stopword
                        kList.append(pLineArr[i], p.getName());
                    }
                }
            }
        }

        //kList.print();
        String searchTerm = "cs2050";
        System.out.printf("Searching for %s in website.\n", searchTerm);
        Keyword found = kList.search(searchTerm);
        if (found != null) {
            found.print();
        } else {
            System.out.printf("Keyword %s not found!\n", searchTerm);
        }

        /*KeywordList testList = new KeywordList();
        testList.append("Test2", "Test2FirstURL");
        testList.append("Test3", "Test3URL");
        testList.append("Test1", "fake");
        testList.append("Test2", "fake2");
        testList.append("Test2", "fake3");
        testList.append("Test2", "fake4");
        testList.append("Test2", "fakeURLFINAL");
        testList.append("Test4", "hopefullynotfake");

        testList.print();*/

    }

    public String cleanHTML(String toClean) {
        toClean = toClean.replaceAll("\\<.*?\\>", ""); // To replace html tags
        return toClean;
    }

    public String cleanString(String toClean) {
        toClean = toClean.replaceAll(" ", ""); // Get rid of whitespace
        toClean = toClean.replaceAll("\\.", ""); // Get rid of comma, period
        toClean = toClean.replaceAll(",", "");
        toClean = toClean.replaceAll("\t", "");
        toClean = toClean.replaceAll("\n", "");
        toClean = toClean.replaceAll("[()]", "");
        return toClean;
    }

    public boolean isValidKey(String key) {
        for (int i = 0; i < stopWords.length; i++) {
            if (key.equalsIgnoreCase(stopWords[i])) {
                return false;
            }
        }
        return true;
    }
}