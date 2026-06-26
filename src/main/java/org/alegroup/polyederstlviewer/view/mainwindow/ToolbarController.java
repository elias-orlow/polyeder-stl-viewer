package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;
import org.alegroup.polyederstlviewer.util.STLParser;

import java.io.File;
import java.io.IOException;

public class ToolbarController {

    @FXML
    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller) {
        this.mainWindowController = controller;
    }

    // ---------------------------
    // TERMINAL MENU
    // ---------------------------

    public void openConsoleWindow(ActionEvent actionEvent) {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/alegroup/polyederstlviewer/view/ConsoleWindow.fxml")
        );

        try {
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Console");
            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {
            System.out.println("Something went wrong trying to load ConsoleWindow.fxml! " + e.toString() + e.getCause());
            e.printStackTrace();
        }
    }

    // ---------------------------
    // FILE MENU
    // ---------------------------

    @FXML
    private void onOpenClicked() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("STL Files", "*.stl")
        );

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            STLParseResult poly = STLParser.parse(file);
            mainWindowController.setPolyhedron(poly);
        }
    }

    @FXML
    private void onCloseFile() {
        SceneModel.getInstance().clearScene();
        mainWindowController.clearPolyInfo();
    }

    @FXML
    private void onExportScreenshot() {
        SceneModel.getInstance().exportScreenshot();
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }

    // ---------------------------
    // EDIT MENU
    // ---------------------------

    @FXML
    private void onUndo() {
        SceneModel.getInstance().undo();
    }

    @FXML
    private void onRedo() {
        SceneModel.getInstance().redo();
    }

    @FXML
    private void onResetView() {
        SceneModel.getInstance().resetCamera();
    }

    @FXML
    private void onResetTransformations() {
        SceneModel.getInstance().resetObjectTransform();
    }

    // ---------------------------
    // VIEW MENU
    // ---------------------------

    @FXML
    private void onToggleGrid(javafx.event.ActionEvent e) {
        CheckMenuItem item = (CheckMenuItem) e.getSource();
        SceneModel.getInstance().setGridVisible(item.isSelected());
    }

    @FXML
    private void onToggleAxes(javafx.event.ActionEvent e) {
        CheckMenuItem item = (CheckMenuItem) e.getSource();
        SceneModel.getInstance().setAxesVisible(item.isSelected());
    }

    @FXML
    private void onSolidMode() {
        SceneModel.getInstance().setRenderModeSolid();
    }

    @FXML
    private void onShadedMode() {
        SceneModel.getInstance().setRenderModeShaded();
    }

//    @FXML
//    private void onTogglePolyInfo() {
//        mainWindowController.togglePolyInfoVisibility();
//    }
}
