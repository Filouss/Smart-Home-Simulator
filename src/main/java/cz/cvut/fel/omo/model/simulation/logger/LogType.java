package cz.cvut.fel.omo.model.simulation.logger;

/**
 * This enum represents LogType.
 * Loggers use this to determine whether they are responsible for a Log.
 */
public enum LogType {
    EVENT,
    ACTIVITY,
    ERROR,
    INFO,
    WARNING,
}
