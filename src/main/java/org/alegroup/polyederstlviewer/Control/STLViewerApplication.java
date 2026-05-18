package org.alegroup.polyederstlviewer.Control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class STLViewerApplication extends Application
{
    @Override
    public void start (Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(STLViewerApplication.class.getResource("/org/alegroup/polyederstlviewer/View/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("STL-File Viewer!");
        stage.setScene(scene);
        stage.show();
    }
}
