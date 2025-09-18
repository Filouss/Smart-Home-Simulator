package cz.cvut.fel.omo.model.simulation.event;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface is used for objects that need to do something every 10 minutes during a simulation.
 * Design pattern: Observer
 */
public interface DoesStuffEveryTenMinutes {
    /**
     * This method is run every ten minutes during simulations.
     */
    default void tenMinutesPassed() {}
}