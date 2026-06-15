package org.alegroup.polyederstlviewer.view.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.util.STLParser;

import java.io.File;
import java.io.IOException;

public class ToolbarController {

    @FXML
    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller) {
        this.mainWindowController = controller;
    }


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

    @FXML
    private void onOpenClicked() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("STL Files", "*.stl")
        );

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            Polyhedron poly = STLParser.parse(file);
            mainWindowController.setPolyhedron(poly);
        }
    }
}
