package cz.cvut.fel.omo.model.simulation.event;

import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.ArrayList;
import java.util.List;

/**
 * This method represents all events current waiting to be handled.
 * Design pattern: Singleton
 */
public final class Events {
    private static List<Event> events = null; // the events

    private Events() {

    }

    /**
     * This method resets the list of events to an empty list.
     */
    public static void resetEvents() {
        events = new ArrayList<>();
    }

    /**
     * This method adds a new event to the list.
     * @param event the event to add
     */
    public static void addEvent(Event event) {
        if (events != null) {
            events.add(event);
        } else {
            Simulation.log(new Log("The simulation was not setup properly.", LogType.ERROR));
        }
    }

    /**
     * This method removes an event from the list.
     * @param event the event to remove
     */
    public static void removeEvent(Event event) {
        if (events != null) {
            events.remove(event);
        } else {
            Simulation.log(new Log("The simulation was not setup properly.", LogType.ERROR));
        }
    }

    /**
     * This method returns a list of all events waiting to be handled.
     * @return the events waiting to be handled
     */
    public static List<Event> getEvents() {
        return events;
    }
}