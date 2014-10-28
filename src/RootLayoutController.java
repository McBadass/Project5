//-----------------------------------------------------------------------
// Class:			RootLayoutController
//
// Author:			Taylor Vories
//
// Class:			CS 2050
//
// Description:		Controls the root layout (main menu container)
//
// Files:		    RootLayoutController.java, RootLayout.fxml, MainApp.java
//-----------------------------------------------------------------------

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;

public class RootLayoutController {
	//Reference to main application
	private MainApp mainApp;

	@FXML
	private MenuItem fileCloseMenuItem;

	//Handle fileCloseMenuClick
	public void handleFileCloseMenuClick(ActionEvent event) {
		System.exit(0);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}