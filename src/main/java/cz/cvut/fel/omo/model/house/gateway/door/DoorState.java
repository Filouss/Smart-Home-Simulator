package cz.cvut.fel.omo.model.house.gateway.door;

/**
 * parent method for all door state
 * Design pattern: State
 */
public abstract class DoorState {
    protected Door door;

    public DoorState(Door door) {
        this.door = door;
    }

    public abstract void open();

    public abstract void close();

    public abstract void lock();

    public abstract void unlock();
}
