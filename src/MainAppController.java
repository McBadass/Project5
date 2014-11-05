//-----------------------------------------------------------------------
// Class:			MainMenuController.java
//
// Author:			Taylor Vories
//
// Class:			CS 2050
//
// Description:		Handles the MainMenu GUI
//
// Files:		    MainApp.java, MainAppController.java, MainApp.fxml
//-----------------------------------------------------------------------

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class MainAppController {
	//Reference to Main Application
	private MainApp mainApp;

	@FXML
	private TextField urlField;				// Text Field for URL
	@FXML
	private TextField keyField;				// Text Field for keywords

	@FXML
	private Label connectionStatusLabel;	// Displays the connection status of the website
	@FXML
	private Label stopwordStatusLabel;		// Lets the user know if the list of ignored words exists in the directory

	@FXML
	private Button connectUrlButton;		// Attempts to connect to a website
	@FXML
	private Button keywordSearchButton;		// Search button
	@FXML
	private Button startOverButton;			// Restarts the application to allow a new connection

	@FXML
	private TextArea results;				// Displays the results of a search

	/**
	 * Handles the Index button.  Attempts to connect to the given website in the urlField.
	 * The button handles some logic, but mostly runs in MainApp.
	 * @param event Button click
	 */
	public void handleIndexButton(ActionEvent event) {
		String userInput = urlField.getText();
		userInput = mainApp.formatForUrl(userInput);	// Formats string to make Page class happy
		if (mainApp.isValidUrl(userInput)) {
			setConnectionStatusLabel("Website is connecting.  This may take a moment", 0);
			mainApp.loadWebsite(userInput);
			if (mainApp.getPage().isConnected()) { // If the connection to the page worked
				setConnectionStatusLabel("Site " + userInput + " is connected!", 1);
				connectUrlButton.setDisable(true);
			}
			setSearchButtonEnable(true);
			if (mainApp.isLoadedStopwords()) {
				stopwordStatusLabel.setTextFill(Color.GREEN);
				stopwordStatusLabel.setText("Stopwords filter loaded successfully.");
			} else {
				stopwordStatusLabel.setTextFill(Color.RED);
				stopwordStatusLabel.setText("Note: stopwords.txt did not load properly.  Check directory.");
			}
		} else {
			setSearchButtonEnable(false);
			setConnectionStatusLabel("Not a valid url.", 0);
		}
	}

	/**
	 * Method to set connection status label.
	 * @param toSet String to display
	 * @param color Color to display.  0 for Red, 1 for Green
	 */
	public void setConnectionStatusLabel(String toSet, int color) {
		if (color == 1) { // If color should be green aka yes
			connectionStatusLabel.setTextFill(Color.GREEN);
			connectionStatusLabel.setText(toSet);
		} else if (color == 0) { // If color should be red aka no
			connectionStatusLabel.setTextFill(Color.RED);
			connectionStatusLabel.setText(toSet);
		} else {
			connectionStatusLabel.setTextFill(Color.BLACK);
			connectionStatusLabel.setText(toSet);
		}
	}

	/**
	 * Starts the connection over when pressed.  A way to put in a new URL
	 * @param event Button clicked
	 */
	public void handleStartOverButton(ActionEvent event) {
		mainApp.restartConnection();
		startOver();
	}

	/**
	 * Searches for a keyword in the connected page.  Sets results in text area.
	 * @param event Button click
	 */
	public void handleKeywordSearchButton(ActionEvent event) {
		results.clear();
		if (keyField.getText().length() < 3) {
			results.appendText("Search term is too short.  Must be 3 characters or longer.");
		} else {
			// This breaks up the user input by whitespace, OR, and commas
			Keyword[] kMultiple = mainApp.search(parseUserInput(keyField.getText()));
			for (int i = 0; i < kMultiple.length; i++) {
				if (kMultiple[i] != null) { // Keyword was found
					// If key is in list of ignored words
					if (kMultiple[i].getKey().equals("Search term is in list of ignored words.")) {
						results.appendText(kMultiple[i].getKey() + "\n\n");
					} else { // Key is not in list of ignored words
						results.appendText("Search term **" + kMultiple[i].getKey() + "** was found in the following websites:\n\n");
						String[] kUrls = kMultiple[i].getUrls().getAll();
						for (int j = 0; j < kUrls.length; j++) {
							results.appendText(kUrls[j] + "\n");
						}
						results.appendText("\n\n*******************************************************\n\n");
					}
				} else {
					results.setText("Search term not found for " + kMultiple[i] + "\n\n");
				}
				System.out.println();
				System.out.println();
			}
		} // end else
	} // end handleKeywordSearchButton()

	public String[] parseUserInput(String userInput) {
		String[] toReturn;
		if (userInput.contains(",")) {
			toReturn = userInput.split(",");
		} else if (userInput.contains(" ")){
			toReturn = userInput.split(" ");
			for (int i = 0; i < toReturn.length; i++) {
				toReturn[i] = toReturn[i].trim();		// Trims whitespace
			}
		} else {
			toReturn = new String[1];
			toReturn[0] = userInput;
		}
		return toReturn;
	}

	/**
	 * Sets the results text area.
	 * @param res Array of Strings to set
	 */
	public void setResults(String[] res) {
		for (int i = 0; i < res.length; i++) {
			results.appendText(res[i] + "\n");
		}
	}

	/**
	 * Sets the results text area.
	 * @param res ArrayList of Strings to set
	 */
	public void setResults(ArrayList<String> res) {
		for (int i = 0; i < res.size(); i++) {
			results.appendText(res.get(i) + "\n");
		}
	}

	/**
	 * Sets the results text area.
	 * @param res Single String to set
	 */
	public void setResults(String res) {
		results.appendText(res + "\n");
	}

	/**
	 * Method to start the program over to allow for a new URL
	 */
	public void startOver() {
		connectUrlButton.setDisable(false);
		keywordSearchButton.setDisable(true);
		connectionStatusLabel.setText("");
		stopwordStatusLabel.setText("");
		urlField.clear();
		keyField.clear();
		results.clear();
	}

	/**
	 * Sets the status of the search button.
	 * @param enabled Boolean of enabled (true) or disabled (false)
	 */
	public void setSearchButtonEnable(boolean enabled) {
		keywordSearchButton.setDisable(!enabled);
	}

	/**
	 * Reference to mainApp
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
