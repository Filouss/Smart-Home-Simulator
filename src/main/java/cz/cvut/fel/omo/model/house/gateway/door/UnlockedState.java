package cz.cvut.fel.omo.model.house.gateway.door;

/**
 * Class for locked door implementation
 * Design pattern: State
 */
public class UnlockedState extends DoorState {

    public UnlockedState(Door door) {
        super(door);
    }

    /**
     * This method contains behaviour of opening unlocked door
     */
    @Override
    public void open() {
        door.setDoorState(new OpenState(door));
    }

    /**
     * This method contains behaviour of closing unlocked door
     */
    @Override
    public void close() {
        door.setDoorState(new ClosedState(door));
    }

    /**
     * This method contains behaviour of locking unlocked door
     */
    @Override
    public void lock() {
        door.setDoorState(new LockedState(door));
    }

    /**
     * This method contains behaviour of unlocking unlocked door
     */
    @Override
    public void unlock() {
        door.setDoorState(this);
    }
}
