package com.mycompany.controllers;

import com.mycompany.EntityBeans.Activity;
import com.mycompany.EntityBeans.Project;
import com.mycompany.FacadeBeans.ActivityFacade;
import com.mycompany.models.HeatmapDay;
import com.mycompany.models.HeatmapTable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;

@Named("heatmapController")
@SessionScoped
public class HeatmapController implements Serializable {

    @EJB
    private ActivityFacade activityFacade;

    private HeatmapTable heatmapTable;

    public HeatmapController() {
    }

    public HeatmapTable getHeatmapTable() {
        return heatmapTable;
    }

    public void setHeatmapTable(HeatmapTable heatmapTable) {
        this.heatmapTable = heatmapTable;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public String generate(Project project) {
        if (project != null) {
            LocalDate today = new LocalDate();
            LocalDate earliest = today;
            int maxCount = 1;
            HashMap<LocalDate, Integer> activityCounter = new HashMap<>();
            for (Activity activity : activityFacade.findByProject(project)) {
                System.out.println("iterating over activity");
                Date date = activity.getTimestamp();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                LocalDate localDate = new LocalDate(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DATE));
                if (localDate.isBefore(earliest)) {
                    earliest = localDate;
                }
                Integer count = activityCounter.containsKey(localDate) ? activityCounter.get(localDate) + 1 : 1;
                if (count > maxCount){
                    maxCount = count;
                }
                activityCounter.put(localDate, count);
            }

            // Start at the beginning of the earliest week. Joda is 1-indexed.
            while (earliest.dayOfWeek().get() > 1) {
                System.out.println("moving earlier");
                earliest = earliest.minusDays(1);
            }

            heatmapTable = new HeatmapTable();
            List<HashMap<String, HeatmapDay>> heatmapMaps = heatmapTable.getHeatmapMaps();
            HashMap<String, HeatmapDay> heatmapMap = null;
            LocalDate tomorrow = today.plusDays(1);
            for (LocalDate current = earliest; current.isBefore(tomorrow); current = current.plusDays(1)) {
                if (current.dayOfWeek().get() == 1) {
                    heatmapMap = new HashMap<>();
                    heatmapMaps.add(heatmapMap);
                }

                System.out.println("iterating day " + current.toString());

                String column = String.format("%d", current.dayOfWeek().get());
                if (!heatmapTable.getColumns().contains(column)) {
                    heatmapTable.getColumns().add(column);
                }

                Integer count = activityCounter.containsKey(current) ? activityCounter.get(current) : 0;
//                String color = String.format("#FF0000%x2", Math.round((1 - ((double)count / maxCount)) * 256));

                long colorLong = Math.round((((double)count / maxCount)) * 255);
                System.out.println("color " + colorLong);
                String color = String.format("#FF0000%2x", colorLong);
                String value = String.format("%d", count);
                heatmapMap.put(column, new HeatmapDay(color, value));
            }
        }
        return "ProjectHeatmap.xhtml?faces-redirect=true";
    }
}
