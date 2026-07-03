package org.alegroup.polyederstlviewer.constants;

/**
 * Contains all constants used by SceneModel.
 *
 * In Java, fields in an interface are automatically public static final.
 */
public interface SceneModelConstants
{
    boolean CAMERA_FIXED_EYE_AT_ZERO = true;
    boolean SUBSCENE_DEPTH_BUFFER_ENABLED = true;

    double NO_ROTATION_ANGLE = 0.0;
    double RESET_TRANSLATE_VALUE = 0.0;
    double ZERO_COORDINATE = 0.0;
    double UNIT_COORDINATE = 1.0;
    double BOX_THIN_SIDE = 1.0;

    int ORIGIN_INDEX = 0;
    int LAST_STACK_INDEX_OFFSET = 1;

    int AMBIENT_LIGHT_RGB_VALUE = 80;
    double POINT_LIGHT_TRANSLATE_X = 300.0;
    double POINT_LIGHT_TRANSLATE_Y = 300.0;
    double POINT_LIGHT_TRANSLATE_Z = 300.0;

    double CAMERA_NEAR_CLIP = 0.1;
    double CAMERA_FAR_CLIP = 10000.0;
    double CAMERA_INITIAL_TRANSLATE_Z = -500.0;
    double CAMERA_MIN_TRANSLATE_Z = -10.0;

    double ORBIT_X_INITIAL_ANGLE = -20.0;
    double ORBIT_Y_INITIAL_ANGLE = 0.0;
    double ORBIT_ROTATION_SPEED = 0.3;
    double ORBIT_X_MIN_ANGLE = -89.0;
    double ORBIT_X_MAX_ANGLE = 89.0;

    double PAN_SPEED_FACTOR = 0.0005;
    double SCROLL_ZOOM_FACTOR = 0.5;
    double PROGRAMMATIC_ZOOM_FACTOR = 100.0;

    double SUBSCENE_INITIAL_WIDTH = 100.0;
    double SUBSCENE_INITIAL_HEIGHT = 100.0;
    int SUBSCENE_BACKGROUND_RGB_VALUE = 40;

    int GRID_SIZE = 250;
    int GRID_MINOR_STEP = 5;
    int GRID_MAJOR_STEP = 50;
    double GRID_THICKNESS_FACTOR = 0.0006;
    double INITIAL_LINE_THICKNESS = 0.1;
    double FULL_LENGTH_MULTIPLIER = 2.0;
    int GRID_MAJOR_COLOR_RGB_VALUE = 110;
    int GRID_MINOR_COLOR_RGB_VALUE = 60;

    int FLAT_COLOR_IMAGE_WIDTH = 1;
    int FLAT_COLOR_IMAGE_HEIGHT = 1;
    int FLAT_COLOR_PIXEL_X = 0;
    int FLAT_COLOR_PIXEL_Y = 0;

    double DEFAULT_MESH_SCALE = 20.0;
    double SHADED_MATERIAL_SPECULAR_POWER = 128.0;

    String NO_SUBSCENE_SCREENSHOT_MESSAGE = "No SubScene available for screenshot.";
    String SCREENSHOT_FILE_CHOOSER_TITLE = "Export Screenshot";
    String PNG_IMAGE_DESCRIPTION = "PNG Image";
    String PNG_EXTENSION_PATTERN = "*.png";
    String PNG_FORMAT_NAME = "png";
    String SCREENSHOT_SAVED_MESSAGE_PREFIX = "Screenshot saved: ";
}
