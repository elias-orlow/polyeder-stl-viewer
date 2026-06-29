package org.alegroup.polyederstlviewer;

import javafx.application.Application;
import org.alegroup.polyederstlviewer.control.STLViewerApplication;

/**
 * Entry point of the STL Viewer application. This class delegates the startup
 * process to the JavaFX framework by launching the {@link STLViewerApplication}.
 * It contains the main method required for starting JavaFX applications from
 * environments that do not support direct JavaFX execution.
 */
public class Main
{

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the application
     * @precondition none
     * @postcondition The JavaFX runtime is initialized and the primary application
     * window is displayed.
     */
    public static void main (String[] args)
    {
        Application.launch(STLViewerApplication.class, args);
    }
}