package org.alegroup.polyederstlviewer.constants;

/**
 * Provides shared constants for console output, command-line argument handling,
 * error exit codes, STL file parsing messages, and formatted result output.
 * <p>
 * This interface is used as a central place for literal values that are needed
 * in console-related classes.
 */
public interface ConsoleConstants
{
    public static final int ARG_COUNT_ZERO = 0;
    public static final int FIRST_INDEX = 0;
    public static final int INDEX_OFFSET = 1;

    public static final int EXIT_CODE_USAGE_ERROR = 1;
    public static final int EXIT_CODE_FILE_NOT_FOUND = 2;
    public static final int EXIT_CODE_UNEXPECTED_ERROR = 4;

    public static final int TOP_TRIANGLES_LIMIT = 10;

    String SYSTEM_OUTPUT_PREFIX = ">> ";
    String SYSTEM_INPUT_PREFIX = "<< ";

    public static final String USAGE_MESSAGE =
            "Usage: java -jar polyviewer.jar <path-to-stl-file>";

    public static final String FILE_NOT_FOUND_PREFIX =
            "File not found: ";

    public static final String PARSING_PREFIX =
            "Parsing STL file: ";

    public static final String TRIANGLE_COUNT_PREFIX =
            "Triangles: ";

    public static final String SURFACE_AREA_FORMAT =
            "Surface area: %.6f%n";

    public static final String VOLUME_FORMAT =
            "Volume: %.6f%n";

    public static final String TOP_TRIANGLES_HEADER =
            "Top 10 largest triangles by area:";

    public static final String TOP_TRIANGLE_ENTRY_FORMAT =
            "%2d: area=%.6f%n";

    public static final String UNEXPECTED_ERROR_PREFIX =
            "Unexpected error: ";
}