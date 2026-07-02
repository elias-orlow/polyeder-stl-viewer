package org.alegroup.polyederstlviewer.constants;

/**
 * Contains general application-wide constants used across the project.
 * All literal values are centralized here to comply with the project's
 * design-by-contract and literal usage guidelines.
 */
public interface ApplicationConstants
{

    /**
     * Exit status code used when terminating the application successfully.
     */
    int EXIT_STATUS_SUCCESS = 0;

    String MAIN_WINDOW_FXML_PATH =
            "/org/alegroup/polyederstlviewer/view/MainWindow.fxml";

    String APPLICATION_TITLE =
            "STL-File Viewer!";

    String APPLICATION_ICON_PATH =
            "/org/alegroup/polyederstlviewer/icons/icon.png";

    double SCREEN_SIZE_OFFSET = 150.0;

}
