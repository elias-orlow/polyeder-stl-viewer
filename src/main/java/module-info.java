module org.alegroup.polyederstlviewer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens org.alegroup.polyederstlviewer to javafx.fxml;
    exports org.alegroup.polyederstlviewer;
}