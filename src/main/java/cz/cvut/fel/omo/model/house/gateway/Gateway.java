package cz.cvut.fel.omo.model.house.gateway;

import cz.cvut.fel.omo.model.creature.Creature;
import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents a gateway between 2 rooms (e.g., stairs or doors)
 */
public class Gateway {
    private final Room firstRoom; // the room on one side of the gateway
    private final Room secondRoom; // the room on the other side of the gateway

    public Gateway(Room firstRoom, Room secondRoom) {
        this.firstRoom = firstRoom;
        this.secondRoom = secondRoom;
    }

    /**
     * This method returns the room on the first side of the gateway.
     * @return the room on the first side of the gateway
     */
    public Room getFirstRoom() {
        return firstRoom;
    }

    /**
     * This method returns the room on the second side of the gateway.
     * @return the room on the second side of the gateway
     */
    public Room getSecondRoom() {
        return secondRoom;
    }


    /**
     * This method returns the room that is not the parameter of this method
     * @return the other room
     */
    public Room getOtherRoom(Room currentRoom) {
        return currentRoom.equals(firstRoom) ? secondRoom : firstRoom;
    }

    /**
     * This method returns whether a creature can pass this gateway
     * @return boolean of ability to pass
     */
    public boolean canCreaturePass(Creature creature) {
        return true;
    }
}
