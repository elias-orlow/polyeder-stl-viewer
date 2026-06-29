package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.alegroup.polyederstlviewer.constants.PolyInfoConstants;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;

/**
 * Controller responsible for displaying information about the currently loaded polyhedron,
 * including triangle count, surface area, and volume. It also provides functionality for
 * changing the polyhedron's color through a custom dialog window.
 */
public class PolyInfoController
{

    /**
     * Root container of the polyhedron information panel.
     */
    @FXML
    private VBox root;

    /**
     * Label displaying the number of triangles of the polyhedron.
     */
    @FXML
    private Label triangleCountLabel;

    /**
     * Label displaying the surface area of the polyhedron.
     */
    @FXML
    private Label surfaceAreaLabel;

    /**
     * Label displaying the volume of the polyhedron.
     */
    @FXML
    private Label volumeLabel;

    /**
     * Initializes the controller by registering a listener on the polyhedron property
     * of the {@link SceneModel}. Whenever a new polyhedron is loaded, the displayed
     * information is updated accordingly.
     *
     * @precondition SceneModel.getInstance().poly != null
     * @postcondition The UI updates automatically when a new polyhedron is loaded
     */
    public void initialize ()
    {
        SceneModel.getInstance().poly.addListener((observable, oldValue, newValue) -> {
            update(newValue);
        });
    }

    /**
     * Updates the displayed polyhedron information based on the provided parse result.
     *
     * @param poly the parsed STL result containing the polyhedron and its analysis data
     * @precondition poly != null
     * @postcondition All labels display updated triangle count, surface area, and volume
     */
    private void update (STLParseResult poly)
    {
        triangleCountLabel.setText(
                PolyInfoConstants.TRIANGLE_COUNT_PREFIX + poly.getPolyhedron().triangleCount()
        );

        surfaceAreaLabel.setText(String.format(
                PolyInfoConstants.SURFACE_AREA_FORMAT,
                poly.getAreaResult().getSurfaceArea()
        ));

        volumeLabel.setText(String.format(
                PolyInfoConstants.VOLUME_FORMAT,
                poly.getPolyhedron().volume()
        ));
    }

    /**
     * Clears all displayed polyhedron information and resets the labels to their default values.
     *
     * @precondition none
     * @postcondition All labels display placeholder values indicating no polyhedron is loaded
     */
    public void clear ()
    {
        triangleCountLabel.setText(PolyInfoConstants.TRIANGLE_COUNT_DEFAULT);
        surfaceAreaLabel.setText(PolyInfoConstants.SURFACE_AREA_DEFAULT);
        volumeLabel.setText(PolyInfoConstants.VOLUME_DEFAULT);
    }

    /**
     * Opens a custom dialog window containing a {@link ColorPicker} that allows the user
     * to select a new color for the currently displayed polyhedron. The selected color is
     * applied immediately after confirmation.
     *
     * @precondition A polyhedron must be loaded in the SceneModel
     * @postcondition The polyhedron's material color is updated if the user confirms the dialog
     */
    @FXML
    private void onChangeColor ()
    {
        ColorPicker picker = new ColorPicker();

        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle(PolyInfoConstants.COLOR_DIALOG_TITLE);
        dialog.getDialogPane().setContent(picker);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK)
            {
                return picker.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(color -> {
            SceneModel.getInstance().setPolyColor(color);
        });
    }
}