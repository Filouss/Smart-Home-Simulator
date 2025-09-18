package cz.cvut.fel.omo.model.house;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a floor of the house (e.g., the first floor).
 */
public class Floor {
    private List<Room> rooms; // the rooms on this floor

    /**
     * This constructor constructs a new Floor object.
     */
    public Floor() {
        this.rooms = new ArrayList<Room>();
    }

    /**
     * This method adds a room to the floor.
     * @param room the room to add
     */
    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    /**
     * This method returns the rooms on this floor.
     * @return the rooms on this floor
     */
    public List<Room> getRooms() {
        return rooms;
    }
}
