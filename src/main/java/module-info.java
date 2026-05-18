module org.alegroup.polyederstlviewer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;

    opens org.alegroup.polyederstlviewer to javafx.fxml;
    opens org.alegroup.polyederstlviewer.View to javafx.fxml;
    exports org.alegroup.polyederstlviewer;
    exports org.alegroup.polyederstlviewer.Control;
    opens org.alegroup.polyederstlviewer.View.MainWindow to javafx.fxml;
    opens org.alegroup.polyederstlviewer.View.ConsoleWindow to javafx.fxml;
}