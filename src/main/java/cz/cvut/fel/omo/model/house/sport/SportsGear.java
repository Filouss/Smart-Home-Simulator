package cz.cvut.fel.omo.model.house.sport;

import cz.cvut.fel.omo.model.house.InanimateObject;
import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents sports gear.
 */
public class SportsGear extends InanimateObject {
    private SportsGearType type; // the sports gear's type
    private boolean borrowed; // whether someone borrowed the sports gear

    /**
     * This constructor constructs a new SportsGear object with the specified parameters/
     * @param room the room the sports gear is in
     * @param type the sports gear's type (eg. skies, bike)
     */
    public SportsGear(Room room, SportsGearType type) {
        super(room);
        this.type = type;
        this.borrowed = false;
    }

    /**
     * This method returns whether the sports gear is borrowed.
     * @return whether the sports gear is borrowed
     */
    public boolean isBorrowed() {
        return borrowed;
    }

    /**
     * This method sets the sports gear's borrowed status.
     * @param borrowed the new borrowed status
     */
    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    /**
     * This method returns the sports gear's type.
     * @return the sports gear's type
     */
    public SportsGearType getType() {
        return type;
    }
}
