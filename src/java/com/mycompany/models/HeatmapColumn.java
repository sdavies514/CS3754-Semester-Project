/*
 * Created by Casey Butenhoff on 2017.12.14  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.models;

/**
 * Represents a single dynamic column in a heatmap datatable.
 *
 * @author CJ
 */
public class HeatmapColumn {

    /**
     * The text to display in the header of this column.
     */
    private String header;

    /**
     * A key that uniquely identifies this column in the HeatmapTable's columns
     * collection.
     */
    private String property;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
