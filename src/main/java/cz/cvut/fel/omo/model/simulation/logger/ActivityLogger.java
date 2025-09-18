package cz.cvut.fel.omo.model.simulation.logger;


import cz.cvut.fel.omo.model.simulation.report.ActivityAndUsageReport;

import java.util.ArrayList;
import java.util.List;

/**
 * This logger adds ACTIVITY logs to the ActivityAndUsageReport.
 * Design pattern: Chain of responsibility
 */
public class ActivityLogger implements Logger {
    private Logger nextLogger;

    @Override
    public void log(Log log) {
        if (log.getType() == LogType.ACTIVITY) {
            ActivityAndUsageReport.addActivityLog(log);
        } else {
            this.nextLogger.log(log);
        }
    }

    @Override
    public void setNextLogger(Logger logger) {
        this.nextLogger = logger;
    }
}
