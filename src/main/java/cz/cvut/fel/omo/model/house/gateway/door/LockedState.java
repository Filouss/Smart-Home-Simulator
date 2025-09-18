package cz.cvut.fel.omo.model.house.gateway.door;

/**
 * Class for locked door implementation
 * Design pattern: State
 */

public class LockedState extends DoorState {

    public LockedState(Door door) {
        super(door);
    }

    /**
     * This method contains behaviour of opening locked door
     */
    @Override
    public void open() {
        //can't open locked doors
        door.setDoorState(this);
    }

    /**
     * This method contains behaviour of closing locked door
     */
    @Override
    public void close() {
        //Door is locked, so it's already closed
        door.setDoorState(this);
    }

    /**
     * This method contains behaviour of locking locked door
     */
    @Override
    public void lock() {
        door.setDoorState(this);
    }

    /**
     * This method contains behaviour of unlocking locked door
     */
    @Override
    public void unlock() {
        door.setDoorState(new UnlockedState(door));
    }
}
