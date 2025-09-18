package cz.cvut.fel.omo.model.house.gateway.door;

import cz.cvut.fel.omo.model.creature.Creature;
import cz.cvut.fel.omo.model.creature.Person;
import cz.cvut.fel.omo.model.creature.Role;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.house.gateway.Gateway;

/**
 * This class represents a door of the house.
 * Design pattern: State
 */
public class Door extends Gateway {
    private DoorState doorState;
    private DoorState previousDoorState;

    /**
     * This constructor constructs a new door between 2 rooms.
     * @param firstRoom the room on one side
     * @param secondRoom the room on the other side
     */
    public Door(Room firstRoom, Room secondRoom) {
        super(firstRoom, secondRoom);
        this.doorState = new UnlockedState(this);
    }


    public void setDoorState(DoorState doorState) {
        this.doorState = doorState;
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public void setPreviousDoorState(DoorState previousDoorState) {
        this.previousDoorState = previousDoorState;
    }

    public void open() {
        doorState.open();
    }

    public void close() {
        doorState.close();
    }

    public void lock() {
        doorState.lock();
    }

    public void unlock() {
        doorState.unlock();
    }

    @Override
    public boolean canCreaturePass(Creature creature) {
        if (doorState instanceof LockedState) {
            // If the door is locked, only a Person can pass through
            if (creature instanceof Person person) {
                return  true;
            } else {
                return false; // Cats cannot pass through locked doors
            }
        } else {
            return true;
        }
    }
}

