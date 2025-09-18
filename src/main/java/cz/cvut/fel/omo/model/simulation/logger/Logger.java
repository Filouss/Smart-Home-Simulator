package cz.cvut.fel.omo.model.simulation.logger;

/**
 * Design pattern: Chain of responsibility
 */
public interface Logger {
    /**
     * This method either logs the log or passes it to the next logger.
     * @param log the log to log
     */
    void log(Log log);

    /**
     * This method sets the next logger.
     * @param logger the next logger
     */
    void setNextLogger(Logger logger);
}
