package cz.cvut.fel.omo.model.simulation.logger;

import cz.cvut.fel.omo.model.simulation.report.EventReport;

import java.util.ArrayList;
import java.util.List;

/**
 * This logger adds EVENT logs to the EventReport.
 * Design pattern: Chain of responsibility
 */
public class EventLogger implements Logger {
    private Logger nextLogger;

    @Override
    public void log(Log log) {
        if (log.getType() == LogType.EVENT) {
            EventReport.addEventLog(log);
        } else {
            this.nextLogger.log(log);
        }
    }

    @Override
    public void setNextLogger(Logger logger) {
        this.nextLogger = logger;
    }
}