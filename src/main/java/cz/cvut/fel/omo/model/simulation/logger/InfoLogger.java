package cz.cvut.fel.omo.model.simulation.logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This logger prints INFO logs into the console.
 * Design pattern: Chain of responsibility
 */
public class InfoLogger implements Logger {
    private Logger nextLogger;

    @Override
    public void log(Log log) {
        if (log.getType() == LogType.INFO) {
            System.out.println("[INFO] " + log.getText());
        } else {
            this.nextLogger.log(log);
        }
    }

    @Override
    public void setNextLogger(Logger logger) {
        this.nextLogger = logger;
    }
}
