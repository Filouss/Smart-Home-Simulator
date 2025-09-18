package cz.cvut.fel.omo.model.house.gateway.door;

/**
 * Class for locked door implementation
 * Design pattern: State
 */
public class OpenState extends DoorState{

    public OpenState(Door door) {
        super(door);
    }

    /**
     * This method contains behaviour of opening open door
     */
    @Override
    public void open() {
        door.setDoorState(this);
    }

    /**
     * This method contains behaviour of closing open door
     */
    @Override
    public void close() {
        door.setDoorState(new ClosedState(door));
    }

    /**
     * This method contains behaviour of locking open door
     */
    @Override
    public void lock() {
        //can't open locked doors, state unchanged
        door.setDoorState(this);
    }

    /**
     * This method contains behaviour of unlocking open door
     */
    @Override
    public void unlock() {
        door.setDoorState(new UnlockedState(door));
        door.setPreviousDoorState(this);
    }
}
