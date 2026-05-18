package org.alegroup.polyederstlviewer.View.MainWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ToolbarController {

    public void openConsoleWindow(ActionEvent actionEvent) {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/alegroup/polyederstlviewer/View/ConsoleWindow.fxml")
        );

        try {
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Console");
            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            System.out.println("Something went wrong trying to load ConsoleWindow.fxml!");
        }
    }
}
