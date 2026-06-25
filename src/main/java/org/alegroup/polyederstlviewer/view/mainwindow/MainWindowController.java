package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
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

    public void setPolyhedron(STLParseResult poly) {

        //polyInfoViewController.update(poly);
        SceneModel.getInstance().renderPolyhedron(poly);
    }

    public void clearPolyInfo() {
        polyInfoViewController.clear();
    }

    public void togglePolyInfoVisibility() {
        polyInfoViewController.toggleVisibility();
    }
}
