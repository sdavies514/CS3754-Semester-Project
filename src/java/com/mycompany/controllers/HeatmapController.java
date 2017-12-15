package com.mycompany.controllers;

import com.mycompany.EntityBeans.Activity;
import com.mycompany.EntityBeans.Project;
import com.mycompany.FacadeBeans.ActivityFacade;
import com.mycompany.models.HeatmapColumn;
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

    private Project project;

    public HeatmapController() {
    }

    public HeatmapTable getHeatmapTable() {
        return heatmapTable;
    }

    public void setHeatmapTable(HeatmapTable heatmapTable) {
        this.heatmapTable = heatmapTable;
    }

    public Project getProject() {
        return project;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public String generate(Project project) {
        this.project = project;
        if (project != null) {
            LocalDate today = new LocalDate();

            // This keeps track of the earliest date of any of this project's
            // activity. Since our heatmap displays the entire history of the
            // project, it helps us later to determine which date out heatmap
            // should begin on.
            LocalDate earliest = today;

            // This keeps track of the largest single day of activity for this
            // project. It's used later to scale the coloring so that regardless
            // of the actual magnitude of the activities, the day with the most
            // activity will always be colored with the brightest color we have.
            int maxCount = 1;

            // This will contain the frequency of activity on each date that is
            // represented in the activity table for this project. In other
            // words, it's a sparse frequency table.
            HashMap<LocalDate, Integer> activityCounter = new HashMap<>();

            // Iterate over every piece of activity that was related to this
            // project.
            for (Activity activity : activityFacade.findByProject(project)) {

                // Java Date returns odd values such as years relative to 1900
                // which we don't want to have to deal with, so we initialize a
                // calendar instance with it instead, which lets us access the
                // date in a way that makes a bit more sense.
                Date date = activity.getTimestamp();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                // Java Calendar uses 0-indexing for month (but not for year
                // or date), but LocalDate expects everything to be 1-indexed so
                // we must add one to the month in order to convert it to
                // 1-indexed form.
                LocalDate localDate = new LocalDate(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DATE));

                // We need to keep track of the date of the earliest activity in
                // this project so that we know which date to start the heatmap
                // on.
                if (localDate.isBefore(earliest)) {
                    earliest = localDate;
                }

                // We count each entry in the activity table as one unit of
                // activity and simply sum them together. A more sophisticated
                // version of this algorithm might assign each entry a weight
                // depending on its significance or some other property.
                Integer count = activityCounter.containsKey(localDate)
                        ? activityCounter.get(localDate) + 1
                        : 1;
                if (count > maxCount) {
                    maxCount = count;
                }
                activityCounter.put(localDate, count);
            }

            // Start on Sunday of the earliest week. Joda is 1-indexed and
            // starts with Monday = 1 so we must take modulo 7 in order to make
            // Sunday = 0.
            while (earliest.dayOfWeek().get() % 7 > 0) {
                earliest = earliest.minusDays(1);
            }

            heatmapTable = new HeatmapTable();

            // Create a special column for a row label that will indicate which
            // week that particular row represents.
            HeatmapColumn rowLabelColumn = new HeatmapColumn();
            heatmapTable.getColumns().add(rowLabelColumn);

            // This special column is simply to label each row so no need to
            // assign it a header value.
            rowLabelColumn.setHeader("");

            // The property field functions as the unique key that identifies
            // this column in a datatable with dynamic columns. Actual row
            // entries will need to reference this property field in order to
            // become associated with this column.
            rowLabelColumn.setProperty("RowLabel");

            // Since this is for a datatable control with dynamic columns, we
            // must supply the data for each row in map form, with the key of
            // each entry being the column that entry corresponds to and the
            // value being the actual data that should be displayed in that
            // cell.
            HashMap<String, HeatmapDay> heatmapMap = null;

            // We want to include activity that occurred today, so we must loop
            // until just before we reach tomorrow.
            LocalDate tomorrow = today.plusDays(1);
            for (LocalDate current = earliest; current.isBefore(tomorrow);
                    current = current.plusDays(1)) {

                // we want to start a each week on Sunday but Joda is
                // 1-indexed and starts with Monday = 1 so we must take modulo 7
                // in order to make Sunday = 0.
                int currentDayOfWeek = current.dayOfWeek().get() % 7;
                if (currentDayOfWeek == 0) {

                    // We want each row in the data table to represent a single
                    // week, so we start a new HashMap for each new week we
                    // encounter.
                    heatmapMap = new HashMap<>();
                    heatmapTable.getHeatmapMaps().add(heatmapMap);

                    // In the first column of each week row, we want to insert
                    // a label indicating which week that row represents. We
                    // don't want this cell to contain a background color, so
                    // we give it a completely transparent one (with the last
                    // two bytes, which represent the alpha value, as 00 to make
                    // the color completely transparent).
                    heatmapMap.put(rowLabelColumn.getProperty(),
                            new HeatmapDay("#00000000",
                                    "Week of " + current.toString()));
                }

                // Each day of the week must have a unique property string. The
                // property field functions as the unique key that identifies
                // the column in a datatable with dynamic columns. Actual row
                // entries will need to reference a property field in order to
                // become associated with the corresponding column. We aren't
                // pre-computing these column properties before the LocalDate
                // for loop because we don't want to show columns for days of
                // the week beyond the current day if the project hasn't existed
                // long enough to have a second row of activity data.
                String columnProperty = String.format("%d", currentDayOfWeek);
                boolean found = false;
                for (HeatmapColumn heatmapColumn : heatmapTable.getColumns()) {
                    if (heatmapColumn.getProperty().equals(columnProperty)) {
                        found = true;
                    }
                }
                if (!found) {
                    HeatmapColumn heatmapColumn = new HeatmapColumn();
                    switch (currentDayOfWeek) {
                        case 0:
                            heatmapColumn.setHeader("Sunday");
                            break;
                        case 1:
                            heatmapColumn.setHeader("Monday");
                            break;
                        case 2:
                            heatmapColumn.setHeader("Tuesday");
                            break;
                        case 3:
                            heatmapColumn.setHeader("Wednesday");
                            break;
                        case 4:
                            heatmapColumn.setHeader("Thursday");
                            break;
                        case 5:
                            heatmapColumn.setHeader("Friday");
                            break;
                        case 6:
                            heatmapColumn.setHeader("Saturday");
                            break;
                    }
                    heatmapColumn.setProperty(columnProperty);
                    heatmapTable.getColumns().add(heatmapColumn);
                }

                // Get the actual frequency of activity that occurred on the
                // current day of our LocalDate loop. If there was no activity
                // on a certain day, we still want to put a zero there to make
                // it obvious that there was zero activity.
                Integer count = activityCounter.containsKey(current)
                        ? activityCounter.get(current)
                        : 0;

                // As mentioned before, we want to scale the magnitudes such
                // that regardless of what they actually are, the day with the
                // most activity will always be colored with the same maximum
                // level of brightness, and lower magnitudes will be
                // correspondingly scaled from there. We accomplish that by
                // taking the ratio of activity count on the current day to the
                // maximum count seen on any one day in this project. That
                // fractional number is then multiplied by 255 in order to scale
                // it two bytes.
                long colorLong = Math.round(((double) count / maxCount) * 255);

                // We use the two byte activity frequency number we scaled above
                // to generate the color string. In this case, we are using a
                // red background color for all cells in the heatmap and simply
                // adjusting the alpha value in order to raise and lower the
                // intensity according to the actual magnitude of the scaled
                // value. %2x in the format string converts the two byte count
                // value into a two character hexadecimal representation, which
                // is placed in the alpha position of the eight-byte color. For
                // the alpha value, FF corresponds to completely opaque while
                // 00 corresponds to completely transparent.
                String color = String.format("#FF0000%2x", colorLong);

                // We want to show the actual magnitude of activity on each day
                // in the cell of the heatmap that it corresponds to, so we must
                // convert the integer
                String value = String.format("%d", count);

                heatmapMap.put(columnProperty, new HeatmapDay(color, value));
            }
        }

        // Redirect to the xhtml page which will actually pull the data we just
        // generated from this controller and display it in a dynamic datatable.
        return "ProjectHeatmap.xhtml?faces-redirect=true";
    }
}
