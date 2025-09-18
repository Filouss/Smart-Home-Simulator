package cz.cvut.fel.omo.model.simulation.logger;

import cz.cvut.fel.omo.model.simulation.TimeTracker;

/**
 * This class represents a Log. Objects of this class can be passed to a Logger.
 */
public class Log {
    private long time;
    private String text;
    private LogType type;

    public Log(String text, LogType type) {
        this.time = TimeTracker.getMinutesPassed();
        this.text = text;
        this.type = type;
    }

    public LogType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }
}
