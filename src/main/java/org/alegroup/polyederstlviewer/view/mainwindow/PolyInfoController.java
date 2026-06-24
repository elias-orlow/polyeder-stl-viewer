package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;

public class PolyInfoController {

    @FXML private VBox root;
    @FXML private Label triangleCountLabel;
    @FXML private Label surfaceAreaLabel;
    @FXML private Label volumeLabel;

    public void initialize()
    {
        SceneModel.getInstance().parseResult.addListener((observable, oldValue, newValue) ->
        {
            update(newValue);
        });
    }

    private void update(STLParseResult result)
    {
        if (result == null)
        {
            clear();
            return;
        }

        Polyhedron poly = result.getPolyhedron();

        triangleCountLabel.setText("Triangles: " + poly.triangleCount());
        surfaceAreaLabel.setText("Surface Area: " + result.getSurfaceArea());
        volumeLabel.setText("Volume: " + poly.volume());
    }

    public void clear() {
        triangleCountLabel.setText("Triangles: -");
        surfaceAreaLabel.setText("Surface Area: -");
        volumeLabel.setText("Volume: -");
    }

    public void toggleVisibility() {
        root.setVisible(!root.isVisible());
        root.setManaged(root.isVisible());
    }
}

