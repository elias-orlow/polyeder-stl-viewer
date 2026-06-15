package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;

public class MainWindowController {

    @FXML
    public BorderPane mainWindow;
    @FXML
    private PolyInfoController polyInfoViewController;
    @FXML
    private ToolbarController toolbarController;


    public Slider zoomSlider;
    public AnchorPane renderingView;

    public void initialize(){

        //mainWindow.setScaleX(100);
        toolbarController.setMainWindowController(this);
    }

    public void setPolyhedron(Polyhedron poly) {

        // Sidebar aktualisieren
        polyInfoViewController.update(poly);

        // Viewer aktualisieren
        SceneModel.getInstance().renderPolyhedron(poly);
    }


}
