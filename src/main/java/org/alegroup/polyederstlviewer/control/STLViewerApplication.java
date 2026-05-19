package org.alegroup.polyederstlviewer.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class STLViewerApplication extends Application
{
    @Override
    public void start (Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(STLViewerApplication.class.getResource("/org/alegroup/polyederstlviewer/view/MainWindow.fxml"));
        // Scale Scene to fit screen (kind of)
        double screenX = Screen.getPrimary().getBounds().getMaxX() - 150;
        double screenY = Screen.getPrimary().getBounds().getMaxY() - 150;
        Scene scene = new Scene(fxmlLoader.load(), screenX, screenY);
        stage.setTitle("STL-File Viewer!");
        stage.setScene(scene);
        stage.show();
    }
}
