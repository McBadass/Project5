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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
public class MainAppController {
	//Reference to Main Application
	private MainApp mainApp;

	@FXML
	private TextField urlField;
	@FXML
	private TextField keyField;

	@FXML
	private Button connectUrlButton;
	@FXML
	private Button keywordSearchButton;

	@FXML
	private TextArea results;

}
