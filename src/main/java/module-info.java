module org.alegroup.polyederstlviewer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;
    requires java.desktop;
    requires com.google.gson;


    exports org.alegroup.polyederstlviewer;
    exports org.alegroup.polyederstlviewer.control;

    opens org.alegroup.polyederstlviewer to javafx.fxml;
    opens org.alegroup.polyederstlviewer.view.mainwindow to javafx.fxml;
    opens org.alegroup.polyederstlviewer.view.consolewindow to javafx.fxml;
    opens org.alegroup.polyederstlviewer.model.geometry to com.google.gson;
}