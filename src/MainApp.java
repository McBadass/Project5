//-----------------------------------------------------------------------
// Class:			MainApp.java
//
// Author:			Taylor Vories
//
// Class:			CS 2050
//
// Description:		Main application.  Handles the core logic.
//
// Files:		    MainApp.java
//
// REQUIREMENTS: Should have Java 8 (or highest version of Java 7) in order to work.
//              Also requires the Apache Commons Validator Library at:
//              http://commons.apache.org/proper/commons-validator/
//              This should be included in the source files.  Also should have
//              a file called stopwords.txt that includes a list of words to ignore
//              when indexing.
//-----------------------------------------------------------------------

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.*; // Import routines package
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MainApp extends Application{

    private String[] stopWords;         //List of words to ignore
    private final int MAXSITES = 30;    // Maximum number of sites to traverse
    private int siteCount = 0;          // Total number of sites so far
    private int currentUrl = 0;         // Total number of URLs found so far
    private ArrayList<String> urls;     // Arraylist of strings containing URLs
    private Stage primaryStage;         // JavaFX Primary stage
    private BorderPane rootLayout;      // Root layout including File Menu
    private boolean loadedStopwords;    // Flag to know if stopwords.txt loaded properly.  Will run regardless
    private KeywordList kList;          // Keyword list for program
    private Page p;                     // Page object to handle indexing

    /**
     * Sets the JavaFX stage
     * @param primaryStage Used internally
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Primitive Search - Web 1.0!");
        setUserAgentStylesheet(STYLESHEET_MODENA);

        initRootLayout();
        showMainMenu();
        initStopWords();
    }

    /**
     * Initializes the root layout
     */
    private void initRootLayout() {
        try {
            //Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            //Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to launch the MainApp.fxml.
     */
    private void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("MainApp.fxml"));
            AnchorPane mainApp = (AnchorPane) loader.load();

            //Set MainApp to the center of RootLayout
            rootLayout.setCenter(mainApp);

            // Give the controller access to mainApp
            MainAppController mainAppController = loader.getController();
            mainAppController.setSearchButtonEnable(false);
            mainAppController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to initialize stopwords, a list of words to ignore when indexing.
     */
    private void initStopWords() {
        //Initiate list of stopwords to ignore
        File stopFile = new File("stopwords.txt");
        try {
            BufferedReader getline = new BufferedReader(new FileReader(stopFile));
            Scanner fileScanner = new Scanner(getline);
            stopWords = fileScanner.nextLine().split(",");
            loadedStopwords = true;
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find stopwords.txt, make sure it's in same directory as program.  Running without stopwords for now.");
            loadedStopwords = false;    // Sets a flag to display results of stopwords loading
        } // Stopwords list is initiated at this point
    }

    /**
     * Launches the program
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method to load the website in the Page class.
     * @param userInputUrl String value of URL to load.
     */
    public void loadWebsite(String userInputUrl) {
        kList = new KeywordList();
        p = null;                       // Initializes p.
        urls = new ArrayList<String>();
        userInputUrl = formatForUrl(userInputUrl);

        try {
            System.out.println("Url inputting: " + userInputUrl);
            p = new Page(userInputUrl);
        } catch (Exception e) {
            System.out.println("How'd you screw this one up?");
            System.exit(0); //TODO: Handle this better
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
            ArrayList<String> links = p.getLinks();
            for (int i = 0; i < links.size(); i++) {
                urls.add(links.get(i));
            }
            if (++currentUrl >= urls.size()) {
                p = null;
            } else {
                do {
                    try {
                        p = new Page(urls.get(currentUrl));
                    } catch (Exception e) { // Bad url - ignore
                        currentUrl++;
                    }
                } while (currentUrl < urls.size() && p == null);
            }
            if (++siteCount >= MAXSITES) break;
        }
    } // end loadWebsite()

    /**
     * Method to reset MainApp to a pre-connection stage.  Allows for a new URL to be indexed.
     */
    public void restartConnection() {
        p = null;               // Clears p to make room for new p
        siteCount = 0;          // Clears sites
        currentUrl = 0;         // Clears urls
        urls.clear();           // Clears Array List of urls
        kList = null;           // Clears current list of keywords
    }

    /**
     * Method to search for a keyword in the KeywordList
     * @param key String value of Keyword to search for
     * @return Returns a Keyword if found.
     */
    public Keyword search(String key) {
        if (isValidKey(key)) {
            return kList.search(key.trim());    // Clears whitespace
        } else {
            // A way to handle if the keyword in the list of ignored words
            Keyword k = new Keyword("Search term is in list of ignored words.");
            return k;
        }
    }

    /**
     * Searches for a keyword.
     * @param keys String[] array of keywords to search for.
     * @return Returns Array of Keywords found.
     */
    public Keyword[] search(String[] keys) {
        Keyword[] toReturn = new Keyword[keys.length];
        for (int i = 0; i < keys.length; i++) {
            toReturn[i] = search(keys[i]);
        }
        return toReturn;
    }

    /**
     * Checks a string to verify if it is a valid URL.  MUST HAVE APACHE COMMONS
     * LIBRARY FOR THIS TO WORK
     * @param url String value of the URL to test
     * @return Returns true if valid, false if not.
     */
    public boolean isValidUrl(String url) {
        UrlValidator defaultValidator = new UrlValidator(); // default schemes
        if (defaultValidator.isValid(url)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Formats a string to make sure the Page class can handle it
     * @param input String input of the url before formatted
     * @return Returns a String that has http:// at the front.
     */
    public String formatForUrl(String input) {
        if (input.contains("http://")) {
            return input;
        } else {
            return "http://" + input;
        }
    }

    /**
     * Cleans out any HTML characters.  Not hugely robust.  It could end
     * up causing issues with more complicated searches or websites
     * @param toClean String that may or may not contain HTML characters
     * @return Returns a String without HTML characters
     */
    public String cleanHTML(String toClean) {
        toClean = toClean.replaceAll("\\<.*?\\>", ""); // To replace html tags
        return toClean;
    }

    /**
     * Cleans up a string to be inserted in a keyword list.  Gets rid of
     * whitespace, periods, commas, tab spaces, newlines, and parenthesis.
     * @param toClean String value of a "dirty" keyword.
     * @return Returns a String of a "clean" keyword ready to be inserted.
     */
    public String cleanString(String toClean) {
        toClean = toClean.replaceAll(" ", "");      // Get rid of whitespace
        toClean = toClean.replaceAll("\\.", "");    // Get rid of period
        toClean = toClean.replaceAll(",", "");      // Gets rid of comma
        toClean = toClean.replaceAll("\t", "");     // Gets rid of tab space
        toClean = toClean.replaceAll("\n", "");     // Gets rid of newlines
        toClean = toClean.replaceAll("[()]", "");   // Gets rid of parenthesis
        return toClean;
    }

    /**
     * Checks to see if a keyword is a valid key or if it should be ignored based
     * on the list of stopwords.
     * @param key String of key to check against stopwords.
     * @return Returns true if keyword is valid (should not be ignored), returns false if exists in stopwords list.
     */
    public boolean isValidKey(String key) {
        for (int i = 0; i < stopWords.length; i++) {
            if (key.equalsIgnoreCase(stopWords[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to return the KeywordList.
     * @return KeywordList to return.
     */
    public KeywordList getkList() {
        return kList;
    }

    /**
     * Getter for page.
     * @return Returns page.
     */
    public Page getPage() {
        return p;
    }

    /**
     * Getter for urls.
     * @return urls.
     */
    public ArrayList<String> getUrls() {
        return urls;
    }

    /**
     * Boolean method to verify that stopwords.txt was loaded correctly or not.
     * @return Returns true if stopwords.txt loaded, false if not.
     */
    public boolean isLoadedStopwords() {
        return loadedStopwords;
    }
}