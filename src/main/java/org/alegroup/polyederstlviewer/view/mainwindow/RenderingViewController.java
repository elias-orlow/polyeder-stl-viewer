package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

// --------!!!!!! Auslagern der Szene als SceneModel, damit Methoden zur Verfügung stellen die global verfügbar sind für zoom, translate, rotate, change of rendered object etc. -Singleton

public class RenderingViewController {

    @FXML
    private AnchorPane rootPane;

    public void initialize() {

        SceneModel.getInstance().addSubSceneToPane(rootPane);
        // SceneModel.getInstance().renderObject();
    }
}