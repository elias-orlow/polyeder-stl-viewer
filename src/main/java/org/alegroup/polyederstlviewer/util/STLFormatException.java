package org.alegroup.polyederstlviewer.util;

/**
 * Exception thrown when an STL file is malformed or cannot be parsed.
 * This exception indicates structural or syntactic issues encountered
 * during the parsing of ASCII or binary STL files.
 */
public class STLFormatException extends Exception
{

    /**
     * Constructs a new STLFormatException with a specific error message.
     *
     * @param message the detail message describing the parsing error
     * @precondition message != null
     * @postcondition A new STLFormatException instance is created with the given message
     */
    public STLFormatException (String message)
    {
        super(message);
    }

    /**
     * Constructs a new STLFormatException with a specific error message
     * and an underlying cause.
     *
     * @param message the detail message describing the parsing error
     * @param cause   the underlying throwable that caused this exception
     * @precondition message != null AND cause != null
     * @postcondition A new STLFormatException instance is created with the given message and cause
     */
    public STLFormatException (String message, Throwable cause)
    {
        super(message, cause);
    }
}