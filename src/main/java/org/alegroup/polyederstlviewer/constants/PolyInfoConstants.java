package org.alegroup.polyederstlviewer.constants;

/**
 * Contains constant values used by the PolyInfoController for label texts,
 * formatting strings, and dialog titles. Centralizing these literals ensures
 * compliance with the project's design-by-contract and literal usage guidelines.
 */
public interface PolyInfoConstants
{

    String TRIANGLE_COUNT_PREFIX = "Triangles: ";
    String SURFACE_AREA_FORMAT = "Surface Area: %.2f area units";
    String VOLUME_FORMAT = "Volume: %.2f volume units";

    String TRIANGLE_COUNT_DEFAULT = "Triangles: -";
    String SURFACE_AREA_DEFAULT = "Surface Area: -";
    String VOLUME_DEFAULT = "Volume: -";

    String COLOR_DIALOG_TITLE = "Choose Polyhedron Color";
}
