package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainWindowController {

    @FXML
    public BorderPane mainWindow;

    public Slider zoomSlider;
    public AnchorPane renderingView;

    public void initialize(){

        //mainWindow.setScaleX(100);
    }
}
