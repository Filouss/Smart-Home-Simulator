package cz.cvut.fel.omo.model.simulation.report;

import cz.cvut.fel.omo.model.simulation.TimeTracker;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.report.util.FileWriterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Design pattern: Singleton
 */
public class EventReport {
    private static List<Log> eventLogs;

    private EventReport() {}

    /**
     * This method generates an EventReport for the past 3 months.
     */
    public static void generate(String subfolder) {
        StringBuilder contentBuilder = new StringBuilder();

        // if the activity logs aren't empty
        if (eventLogs != null && !eventLogs.isEmpty()) {
            // create a list for  unique event logs
            List<Log> uniqueEventLogs = new ArrayList<>();
            List<Long> uniqueEventLogsOccurrences = new ArrayList<Long>();
            for (Log log : eventLogs) {
                if (TimeTracker.getMinutesPassed() - log.getTime() < 60*24*30*3){
                    int indexOfSameLog = -1;

                    for (int i = 0; i < uniqueEventLogs.size(); i++) {
                        if (Objects.equals(uniqueEventLogs.get(i).getText(), log.getText())) {
                            indexOfSameLog = i;
                            break;
                        }
                    }
                    if (indexOfSameLog == -1) {
                        // add it
                        uniqueEventLogs.add(log);
                        uniqueEventLogsOccurrences.add(1L);
                        // else
                    } else {
                        // increase its amount of occurrences
                        uniqueEventLogsOccurrences.set(indexOfSameLog, uniqueEventLogsOccurrences.get(indexOfSameLog) + 1);
                    }
                }
            }
            // dump the data to the content string builder
            for (int i = 0; i < uniqueEventLogs.size(); i++) {
                contentBuilder.append(uniqueEventLogs.get(i).getText()).append(" (").append(uniqueEventLogsOccurrences.get(i)).append(") \n");
            }
        }

        // save the content to a file
        FileWriterUtil.writeFile(subfolder, "EventReport", contentBuilder.toString());
    }

    /**
     * This method adds a log to the eventLogs for report generation.
     * @param log the log to add
     */
    public static void addEventLog(Log log) {
        if (eventLogs == null) {
            eventLogs = new ArrayList<Log>();
        }
        eventLogs.add(log);
    }
}
