package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.alegroup.polyederstlviewer.constants.ViewConstants;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;
import org.alegroup.polyederstlviewer.util.STLParser;

import java.io.File;
import java.io.IOException;

/**
 * Controller responsible for handling all toolbar interactions, including file operations,
 * editing actions, view settings, and opening auxiliary windows. This class acts as the
 * connection between GUI menu elements and the underlying application logic.
 */
public class ToolbarController
{

    /**
     * Reference to the main window controller.
     */
    @FXML
    private MainWindowController mainWindowController;

    /**
     * Sets the main window controller instance.
     *
     * @param controller the controller of the main window
     * @precondition controller != null
     * @postcondition this.mainWindowController == controller
     */
    public void setMainWindowController (MainWindowController controller)
    {
        this.mainWindowController = controller;
    }

    /* =========================================================
       TERMINAL MENU
       ========================================================= */

    /**
     * Opens a new console window using the ConsoleWindow.fxml layout.
     *
     * @param actionEvent the triggering event
     * @precondition ConsoleWindow.fxml must exist at the specified resource path
     * @postcondition a new Stage containing the console window is displayed
     */
    public void openConsoleWindow (ActionEvent actionEvent)
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(ViewConstants.CONSOLE_WINDOW_FXML_PATH)
        );

        try
        {
            Scene scene = new Scene(loader.load(),
                    ViewConstants.CONSOLE_WINDOW_WIDTH,
                    ViewConstants.CONSOLE_WINDOW_HEIGHT);

            Stage stage = new Stage();
            stage.setTitle(ViewConstants.CONSOLE_WINDOW_TITLE);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e)
        {
            System.out.println(ViewConstants.CONSOLE_WINDOW_LOAD_ERROR + e);
            e.printStackTrace();
        }
    }

    /* =========================================================
       FILE MENU
       ========================================================= */

    /**
     * Opens a file chooser dialog to select an STL file and loads it into the application.
     *
     * @precondition The user must select a valid STL file
     * @postcondition The selected STL file is parsed and displayed in the viewer
     */
    @FXML
    private void onOpenClicked ()
    {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        ViewConstants.STL_FILE_FILTER_NAME,
                        ViewConstants.STL_FILE_EXTENSION)
        );

        File file = chooser.showOpenDialog(null);

        if (file != null)
        {
            STLParseResult poly = STLParser.parse(file);
            mainWindowController.setPolyhedron(poly);
        }
    }

    /**
     * Closes the currently loaded STL file and clears all polyhedron information.
     *
     * @precondition none
     * @postcondition The scene and poly info panel are cleared
     */
    @FXML
    private void onCloseFile ()
    {
        SceneModel.getInstance().clearScene();
        mainWindowController.clearPolyInfo();
    }

    /**
     * Exports a screenshot of the current 3D scene.
     *
     * @precondition The 3D scene must be initialized
     * @postcondition A PNG screenshot is saved to a user-selected location
     */
    @FXML
    private void onExportScreenshot ()
    {
        SceneModel.getInstance().exportScreenshot();
    }

    /**
     * Exits the application.
     *
     * @precondition none
     * @postcondition Application terminates
     */
    @FXML
    private void onExit ()
    {
        System.exit(ViewConstants.EXIT_STATUS_SUCCESS);
    }

    /* =========================================================
       EDIT MENU
       ========================================================= */

    /**
     * Performs an undo operation on the scene transformations.
     *
     * @precondition A previous transformation state must exist
     * @postcondition The last transformation is reverted
     */
    @FXML
    private void onUndo ()
    {
        SceneModel.getInstance().undo();
    }

    /**
     * Performs a redo operation on the scene transformations.
     *
     * @precondition A redo state must exist
     * @postcondition The previously undone transformation is restored
     */
    @FXML
    private void onRedo ()
    {
        SceneModel.getInstance().redo();
    }

    /**
     * Resets the camera to its default position and orientation.
     *
     * @precondition none
     * @postcondition Camera is restored to its initial state
     */
    @FXML
    private void onResetView ()
    {
        SceneModel.getInstance().resetCamera();
    }

    /**
     * Resets all object transformations (translation, rotation, zoom).
     *
     * @precondition none
     * @postcondition Object transformations are restored to default values
     */
    @FXML
    private void onResetTransformations ()
    {
        SceneModel.getInstance().resetObjectTransform();
    }

    /* =========================================================
       VIEW MENU
       ========================================================= */

    /**
     * Toggles the visibility of the grid in the 3D scene.
     *
     * @param e the triggering event
     * @precondition none
     * @postcondition Grid visibility matches the menu item state
     */
    @FXML
    private void onToggleGrid (ActionEvent e)
    {
        CheckMenuItem item = (CheckMenuItem) e.getSource();
        SceneModel.getInstance().setGridVisible(item.isSelected());
    }

    /**
     * Toggles the visibility of the axes in the 3D scene.
     *
     * @param e the triggering event
     * @precondition none
     * @postcondition Axes visibility matches the menu item state
     */
    @FXML
    private void onToggleAxes (ActionEvent e)
    {
        CheckMenuItem item = (CheckMenuItem) e.getSource();
        SceneModel.getInstance().setAxesVisible(item.isSelected());
    }

    /**
     * Switches the render mode to solid.
     *
     * @precondition A mesh must be loaded
     * @postcondition The mesh is rendered using solid material
     */
    @FXML
    private void onSolidMode ()
    {
        SceneModel.getInstance().setRenderModeSolid();
    }

    /**
     * Switches the render mode to shaded.
     *
     * @precondition A mesh must be loaded
     * @postcondition The mesh is rendered using shaded material
     */
    @FXML
    private void onShadedMode ()
    {
        SceneModel.getInstance().setRenderModeShaded();
    }
}