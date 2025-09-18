package cz.cvut.fel.omo.model.house.sensor;

import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents a creature sensor.
 * A CreatureSensor interacts with the room's API to control relevant appliances.
 */
public class CreatureSensor extends Sensor {
    private boolean creaturesDetected;

    public CreatureSensor(Room room) {
        super(room);
        creaturesDetected = false;
    }

    @Override
    public void tenMinutesPassed() {
        // if the sensor's room isn't empty and the last state of creaturesDetected is false
        if (!this.getRoom().getCreatures().isEmpty() && !creaturesDetected) {
            // set creaturesDetected to true
            creaturesDetected = true;
            // turn on all lights in the room
            this.getRoom().setAllLightsToActive();
        // else if the room is empty, but the last state of creaturesDetected is true
        } else if (this.getRoom().getCreatures().isEmpty() && creaturesDetected) {
            // set creaturesDetected to false
            creaturesDetected = false;
            // set all lights in the room to idle
            this.getRoom().setAllLightsToIdle();
        }
    }
}
