package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;

/**
 * Controller responsible for initializing and embedding the 3D rendering
 * SubScene into the main application window. This class acts as the bridge
 * between the FXML-defined layout and the rendering logic managed by
 * {@link SceneModel}.
 */
public class RenderingViewController
{

    /**
     * The root pane into which the 3D SubScene will be injected.
     */
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the rendering view by attaching the SubScene created by
     * {@link SceneModel} to the root pane defined in the FXML layout.
     *
     * @precondition rootPane != null
     * @postcondition The SubScene is added to the rootPane and bound to its size
     */
    public void initialize ()
    {
        SceneModel.getInstance().addSubSceneToPane(rootPane);
    }
}