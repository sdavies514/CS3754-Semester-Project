/*
 * Created by Casey Butenhoff on 2017.11.18  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HeatmapTable {

    private final List<HashMap<String, HeatmapDay>> heatmapMaps = new ArrayList<>();

    private final List<String> columns = new ArrayList<>();

    public List<HashMap<String, HeatmapDay>> getHeatmapMaps() {
        return heatmapMaps;
    }

    public List<String> getColumns() {
        return columns;
    }

}
