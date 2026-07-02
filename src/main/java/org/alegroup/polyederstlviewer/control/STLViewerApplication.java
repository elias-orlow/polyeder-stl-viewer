package org.alegroup.polyederstlviewer.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.alegroup.polyederstlviewer.constants.ApplicationConstants;

import java.io.IOException;

/**
 * Main JavaFX application entry point for the STL Viewer.
 * Loads the primary window from FXML and initializes the stage.
 *
 * @precondition JavaFX runtime must be initialized before calling start()
 * @postcondition Main window is displayed and ready for user interaction
 */
public class STLViewerApplication extends Application
{

    /**
     * Initializes and displays the main application window.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws IOException if the FXML file cannot be loaded
     * @precondition stage != null AND FXML resource exists
     * @postcondition Main window is loaded, sized, configured, and shown
     */
    @Override
    public void start (Stage stage) throws IOException
    {

        FXMLLoader loader = new FXMLLoader(
                STLViewerApplication.class.getResource(
                        ApplicationConstants.MAIN_WINDOW_FXML_PATH
                )
        );

        double screenWidth = Screen.getPrimary().getBounds().getMaxX() - ApplicationConstants.SCREEN_SIZE_OFFSET;
        double screenHeight = Screen.getPrimary().getBounds().getMaxY() - ApplicationConstants.SCREEN_SIZE_OFFSET;

        Scene scene = new Scene(loader.load(), screenWidth, screenHeight);

        stage.setTitle(ApplicationConstants.APPLICATION_TITLE);
        stage.getIcons().add(
                new Image(
                        STLViewerApplication.class
                                .getResource(ApplicationConstants.APPLICATION_ICON_PATH)
                                .toString()
                )
        );

        stage.setScene(scene);
        stage.show();
    }
}