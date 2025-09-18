package cz.cvut.fel.omo.model.house.sensor;

import cz.cvut.fel.omo.model.simulation.event.DoesStuffEveryTenMinutes;
import cz.cvut.fel.omo.model.house.InanimateObject;
import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents a sensor.
 * For the purposes of this project, sensors have no energy consumption.
 */
public class Sensor extends InanimateObject {

    /**
     * This constructor constructs a new Sensor object with the specified parameters.
     * @param room the room the sensor is in
     */
    public Sensor(Room room) {
        super(room);
    }
}
