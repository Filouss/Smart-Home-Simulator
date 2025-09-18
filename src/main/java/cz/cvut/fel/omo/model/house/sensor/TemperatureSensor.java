package cz.cvut.fel.omo.model.house.sensor;

import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.house.appliance.ApplianceState;
import cz.cvut.fel.omo.model.house.appliance.Dehumidifier;
import cz.cvut.fel.omo.model.house.appliance.Heater;

import java.util.List;

/**
 * This class represents a temperature sensor.
 * A TemperatureSensor interacts with the room's API to control relevant appliances.
 */
public class TemperatureSensor extends Sensor {
    private final double targetTemperature;
    private final double acceptableOffsetInTemperature;

    public TemperatureSensor(Room room) {
        super(room);
        targetTemperature = 22;
        acceptableOffsetInTemperature = 4;
    }

    /**
     * This method periodically checks the rooms temperature
     */
    @Override
    public void tenMinutesPassed() {
        super.tenMinutesPassed();
        if (getRoom().getTemperature() < targetTemperature - acceptableOffsetInTemperature) {
            getRoom().setAllHeatersToActive();
        } else if (getRoom().getHumidity() > targetTemperature + acceptableOffsetInTemperature) {
            getRoom().setAllHeatersToIdle();
        }
    }
}