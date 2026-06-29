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

    int FIRST_INDEX = 0;
    int NEXT_INDEX_OFFSET = 1;
    int SECOND_INDEX = 1;

    int HASH_MULTIPLIER = 31;
}