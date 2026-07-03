package org.alegroup.polyederstlviewer.constants;

/**
 * Centralized general-purpose constants used across multiple subsystems.
 * <p>
 * This interface provides basic numeric and index-related constants
 * that are frequently reused throughout the application to avoid
 * magic numbers and ensure consistent behavior in low-level operations.
 */
public interface GeneralConstants
{
    int INT_ZERO = 0;
    float ZERO_FLOAT = 0.0f;
    double ZERO_DOUBLE = 0.0;

    int INT_ONE = 1;
    int INT_TWO = 2;
    int INT_THREE = 3;
    int INT_FOUR = 4;

    int FIRST_INDEX = 0;
    int NEXT_INDEX_OFFSET = 1;
    int SECOND_INDEX = 1;
    int THIRD_INDEX = 2;
    int FOURTH_INDEX = 3;

    int HASH_MULTIPLIER = 31;
    long NANOS_PER_MILLISECOND = 1_000_000L;

    String QUOTATION = "\"";
    String EMPTY_STRING = "";
    String COLON = ": ";
    String LINE_SEPARATOR  = "\n";
    String CLOSED_BRACKET = ")";
    String APOSTROPH = "'";
}