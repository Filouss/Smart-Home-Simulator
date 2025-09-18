package cz.cvut.fel.omo.model.house;

import cz.cvut.fel.omo.model.simulation.event.BaseDoesStuffEveryTenMinutes;

/**
 * This class represents an inanimate object that can only be in one room at the same time.
 */
public class InanimateObject extends BaseDoesStuffEveryTenMinutes {
    private final Room room; // the room the object belongs to

    /**
     * This constructor constructs a new InanimateObject using the specified parameters.
     * @param room the room the inanimate object is in
     */
    public InanimateObject(Room room) {
        this.room = room;
    }

    /**
     * This method returns the room the inanimate object is in.
     * @return the room the inanimate object is in
     */
    public Room getRoom() {
        return room;
    }
}
