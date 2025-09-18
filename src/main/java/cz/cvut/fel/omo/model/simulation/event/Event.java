package cz.cvut.fel.omo.model.simulation.event;

import java.util.List;

/**
 * This class represents an event.
 * @param type the type of event
 * @param objects the object the event is related to
 */
public record Event (EventType type, List<Object> objects){}