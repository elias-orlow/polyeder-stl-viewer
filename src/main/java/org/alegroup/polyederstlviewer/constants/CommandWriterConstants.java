package org.alegroup.polyederstlviewer.constants;

/**
 * Constants used by CommandWriter to avoid literals in code.
 */
public interface CommandWriterConstants
{
    /**
     * Path to the JSON command file.
     */
    public static final String COMMAND_FILE_PATH =
            "src/main/java/org/alegroup/polyederstlviewer/constants/commands.json";

    /**
     * Error message prefix for file writer issues.
     */
    public static final String ERROR_PREFIX =
            "Something went wrong trying to create the file writer! ";
}
