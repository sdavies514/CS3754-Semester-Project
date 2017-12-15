/*
 * Created by Casey Butenhoff on 2017.11.18  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents all the data required to construct a single heatmap datatable
 * containing dynamic columns.
 *
 * @author CJ
 */
public class HeatmapTable {

    /**
     * Contains all the rows represented in the table. The string key in
     * this collection must match the property field of the column that
     * day is associated with, because this is how days are actually
     * associated with the dynamic columns.
     */
    private final List<HashMap<String, HeatmapDay>> heatmapMaps = new ArrayList<>();

    /**
     * The dynamic columns that should be displayed by the data table. Only
     * columns in this list and data that references these columns will
     * ultimately be shown in the datatable.
     */
    private final List<HeatmapColumn> columns = new ArrayList<>();

    public List<HashMap<String, HeatmapDay>> getHeatmapMaps() {
        return heatmapMaps;
    }

    public List<HeatmapColumn> getColumns() {
        return columns;
    }
}
