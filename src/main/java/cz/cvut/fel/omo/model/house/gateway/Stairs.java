package cz.cvut.fel.omo.model.house.gateway;

import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents stairs inside the house.
 */
public class Stairs extends Gateway {

    /**
     * This constructor constructs a new pair of stairs between 2 rooms.
     * @param firstRoom the room on the first floor
     * @param secondRoom the room on the second floor
     */
    public Stairs(Room firstRoom, Room secondRoom) {
        super(firstRoom, secondRoom);
    }
}
