package cz.cvut.fel.omo.model.house.gateway.door;

/**
 * Class for closed door implementation
 * Design pattern: State
 */
public class ClosedState extends DoorState{

    public ClosedState(Door door) {
        super(door);
    }

    /**
     * This method contains behaviour of opening closed door
     */
    @Override
    public void open() {
        door.setDoorState(new OpenState(door));
    }

    /**
     * This method contains behaviour of closing closed door
     */
    @Override
    public void close() {
        door.setDoorState(this);
    }

    /**
     * This method contains behaviour of locking closed door
     */
    @Override
    public void lock() {
        door.setDoorState(new LockedState(door));
    }

    /**
     * This method contains behaviour of unlocking closed door
     */
    @Override
    public void unlock() {
        door.setDoorState(new UnlockedState(door));
        door.setPreviousDoorState(this);
    }
}
