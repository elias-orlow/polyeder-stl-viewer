package org.alegroup.polyederstlviewer;

import javafx.application.Application;
import org.alegroup.polyederstlviewer.control.STLViewerApplication;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.util.HashMap;

public class Main {

    public static void main (String[] args)
    {
        Application.launch(STLViewerApplication.class, args);
    }


}
