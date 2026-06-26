package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;
import org.alegroup.polyederstlviewer.util.STLParser;

import javax.print.attribute.standard.RequestingUserName;

public class PolyInfoController {

    @FXML private VBox root;
    @FXML private Label triangleCountLabel;
    @FXML private Label surfaceAreaLabel;
    @FXML private Label volumeLabel;

    public void initialize(){

        SceneModel.getInstance().poly.addListener((observable, oldValue, newValue) -> {
            update(newValue);
        });
    }

    private void update(STLParseResult poly) {
        triangleCountLabel.setText("Triangles: " + poly.getPolyhedron().triangleCount());
        surfaceAreaLabel.setText(String.format("Surface Area: %.2f area units", poly.getAreaResult().getSurfaceArea()));
        volumeLabel.setText(String.format("Volume: %.2f volume units", poly.getPolyhedron().volume()));
    }

    public void clear() {
        triangleCountLabel.setText("Triangles: -");
        surfaceAreaLabel.setText("Surface Area: -");
        volumeLabel.setText("Volume: -");
    }

    @FXML
    private void onChangeColor() {
        ColorPicker picker = new ColorPicker();

        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Choose Polyhedron Color");
        dialog.getDialogPane().setContent(picker);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return picker.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(color -> {
            SceneModel.getInstance().setPolyColor(color);
        });
    }


//    public void toggleVisibility() {
//        root.setVisible(!root.isVisible());
//        root.setManaged(root.isVisible());
//    }
}

