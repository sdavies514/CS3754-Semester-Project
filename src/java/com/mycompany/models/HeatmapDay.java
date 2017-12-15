/*
 * Created by Casey Butenhoff on 2017.11.18  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.models;

/**
 * Represents a single day in a heatmap datatable.
 *
 * @author CJ
 */
public class HeatmapDay {

    /**
     * The background color (in hex format, such as #FFFFFFFF) that should be
     * used for this day.
     */
    private final String color;

    /**
     * The string to actually display in the cell for this day.
     */
    private final String value;

    public HeatmapDay(final String color, final String value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }
}
