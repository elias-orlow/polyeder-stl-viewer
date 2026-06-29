package org.alegroup.polyederstlviewer.constants;

/**
 * Contains constant values used by the view and toolbar components.
 * All literals are centralized here to comply with the project’s
 * design-by-contract and literal usage guidelines.
 */
public interface ViewConstants
{

    String CONSOLE_WINDOW_FXML_PATH =
            "/org/alegroup/polyederstlviewer/view/ConsoleWindow.fxml";

    String CONSOLE_WINDOW_TITLE = "Console";

    String CONSOLE_WINDOW_LOAD_ERROR =
            "Something went wrong trying to load ConsoleWindow.fxml! ";

    String STL_FILE_FILTER_NAME = "STL Files";

    String STL_FILE_EXTENSION = "*.stl";

    int EXIT_STATUS_SUCCESS = 0;

    int CONSOLE_WINDOW_WIDTH = 800;

    int CONSOLE_WINDOW_HEIGHT = 600;
}
