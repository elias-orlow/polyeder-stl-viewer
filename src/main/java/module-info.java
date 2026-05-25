module org.alegroup.polyederstlviewer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;
    requires java.desktop;

    exports org.alegroup.polyederstlviewer;
    exports org.alegroup.polyederstlviewer.control;
    exports org.alegroup.polyederstlviewer.control.commands;

    opens org.alegroup.polyederstlviewer to javafx.fxml;
    opens org.alegroup.polyederstlviewer.view.mainwindow to javafx.fxml;
    opens org.alegroup.polyederstlviewer.view.consolewindow to javafx.fxml;
    exports org.alegroup.polyederstlviewer.util;
    opens org.alegroup.polyederstlviewer.util to javafx.fxml;
}