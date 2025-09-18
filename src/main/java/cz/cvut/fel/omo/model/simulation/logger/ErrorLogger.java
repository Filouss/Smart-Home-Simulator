package cz.cvut.fel.omo.model.simulation.logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This logger prints ERROR logs into the console.
 * Design pattern: Chain of responsibility
 */
public class ErrorLogger implements Logger {
    private Logger nextLogger;

    @Override
    public void log(Log log) {
        if (log.getType() == LogType.ERROR) {
            System.out.println("[ERROR] " + log.getText());
        } else {
            this.nextLogger.log(log);
        }
    }

    @Override
    public void setNextLogger(Logger logger) {
        this.nextLogger = logger;
    }
}
