package org.alegroup.polyederstlviewer.constants;

/**
 * Contains literal constants used exclusively by the STLParser.
 * Centralizing these values ensures compliance with the project's
 * literal usage guidelines and improves maintainability.
 */
public interface STLParserConstants
{

    // General parsing constants
    int MARK_LIMIT = 1024;
    int HEADER_READ_SIZE = 256;
    int MIN_BINARY_SIZE = 84;
    int BINARY_HEADER_SIZE = 80;
    int BINARY_TRIANGLE_SIZE = 50;
    int VERTEX_COUNT = 3;

    String ASCII_CHARSET = "US-ASCII";

    // Tokens
    String TOKEN_FACET = "facet";
    String TOKEN_VERTEX = "vertex";
    String TOKEN_OUTER_LOOP = "outer loop";
    String TOKEN_SOLID = "solid";
    String TOKEN_FACET_NORMAL = "facet normal";
    String TOKEN_ENDFACET = "endfacet";

    String NEWLINE = "\n";

    // Error messages
    String ERROR_INCOMPLETE_FACET = "Facet ended without 3 vertices";
    String ERROR_BINARY_TOO_SHORT = "Binary STL too short";

    // Validation
    String FILE_NULL = "file must not be null";
}