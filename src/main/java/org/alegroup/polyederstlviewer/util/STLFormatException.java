package org.alegroup.polyederstlviewer.util;

/**
 * Exception thrown when an STL file is malformed or cannot be parsed.
 *
 * @precondition None.
 * @postcondition Exception indicates parsing error.
 */
public class STLFormatException extends Exception
{
    public STLFormatException(String message)
    {
        super(message);
    }

    public STLFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
