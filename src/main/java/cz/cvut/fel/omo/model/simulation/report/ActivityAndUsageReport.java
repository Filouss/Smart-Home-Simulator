package cz.cvut.fel.omo.model.simulation.report;

import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.TimeTracker;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;
import cz.cvut.fel.omo.model.simulation.report.util.FileWriterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Design pattern: Singleton
 */
public class ActivityAndUsageReport {
    private static List<Log> activityLogs;

    private ActivityAndUsageReport() {}

    /**
     * This method generates an ActivityAndUsageReport for the past 3 months.
     */
    public static void generate(String subfolder) {
        // declare a new string builder for the report content
        StringBuilder content = new StringBuilder();

        // if the activity logs aren't empty
        if (activityLogs != null && !activityLogs.isEmpty()) {
            // create a list for  unique activity logs
            List<Log> uniqueActivityLogs = new ArrayList<>();
            List<Long> uniqueActivityLogsOccurrences = new ArrayList<Long>();
            // loop through all activity logs
            for (Log activityLog : activityLogs) {
                // if the log isn't more than 3 months old
                if (TimeTracker.getMinutesPassed() - activityLog.getTime() < 60*24*30*3){
                    int indexOfSameLog = -1;
                    // try to get the index of the same activity log in unique activity logs
                    for (int i = 0; i < uniqueActivityLogs.size(); i++) {
                        if (Objects.equals(uniqueActivityLogs.get(i).getText(), activityLog.getText())) {
                            indexOfSameLog = i;
                            break;
                        }
                    }
                    // if the same activity log isn't in unique activity logs already
                    if (indexOfSameLog == -1) {
                        // add it
                        uniqueActivityLogs.add(activityLog);
                        uniqueActivityLogsOccurrences.add(1L);
                        // else
                    } else {
                        // increase its amount of occurrences
                        uniqueActivityLogsOccurrences.set(indexOfSameLog, uniqueActivityLogsOccurrences.get(indexOfSameLog) + 1);
                    }
                }
            }

            // dump the data to the content string builder
            for (int i = 0; i < uniqueActivityLogs.size(); i++) {
                content.append(uniqueActivityLogs.get(i).getText()).append(" (").append(uniqueActivityLogsOccurrences.get(i)).append(") \n");
            }
        }

        // save the content to a file
        FileWriterUtil.writeFile(subfolder, "ActivityAndUsageReport", content.toString());
    }

    /**
     * This method adds an activity log to the activityLogs for report generation.
     * @param log the log to add
     */
    public static void addActivityLog(Log log) {
        if (activityLogs == null) {
            activityLogs = new ArrayList<Log>();
        }
        activityLogs.add(log);
    }
}
