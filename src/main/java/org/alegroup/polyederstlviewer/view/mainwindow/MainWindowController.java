package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;

/**
 * Controller responsible for coordinating the main window layout, connecting
 * the toolbar, polyhedron information panel, rendering view, and zoom slider.
 * This class acts as the central hub for UI interactions that affect the 3D scene.
 */
public class MainWindowController
{

    /**
     * The root BorderPane of the main application window.
     */
    @FXML
    public BorderPane mainWindow;

    /**
     * Controller responsible for displaying polyhedron information.
     */
    @FXML
    private PolyInfoController polyInfoViewController;

    /**
     * Controller responsible for handling toolbar interactions.
     */
    @FXML
    private ToolbarController toolbarController;

    /**
     * Slider used to control the camera zoom level.
     */
    @FXML
    public Slider zoomSlider;

    /**
     * AnchorPane containing the 3D rendering SubScene.
     */
    public AnchorPane renderingView;

    /**
     * Initializes the main window controller by linking the toolbar controller
     * and registering a listener on the zoom slider to adjust the camera zoom.
     *
     * @precondition toolbarController != null AND zoomSlider != null
     * @postcondition Zoom slider changes affect the camera zoom in the 3D scene
     */
    public void initialize ()
    {
        toolbarController.setMainWindowController(this);

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double dy = newVal.doubleValue() - oldVal.doubleValue();
            SceneModel.getInstance().zoomBy(dy);
        });
    }

    /**
     * Loads the given polyhedron into the 3D scene for rendering.
     *
     * @param poly the parsed STL result containing the polyhedron
     * @precondition poly != null
     * @postcondition The polyhedron is rendered in the 3D viewer
     */
    public void setPolyhedron (STLParseResult poly)
    {
        SceneModel.getInstance().renderPolyhedron(poly);
    }

    /**
     * Clears all displayed polyhedron information from the information panel.
     *
     * @precondition none
     * @postcondition PolyInfoView displays placeholder values
     */
    public void clearPolyInfo ()
    {
        polyInfoViewController.clear();
    }
}