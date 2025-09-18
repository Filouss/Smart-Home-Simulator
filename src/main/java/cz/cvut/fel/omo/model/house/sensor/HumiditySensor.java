package cz.cvut.fel.omo.model.house.sensor;

import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.house.appliance.ApplianceState;
import cz.cvut.fel.omo.model.house.appliance.Dehumidifier;

/**
 * This class represents a humidity sensor.
 * A HumiditySensor interacts with the room's API to control relevant appliances.
 */
public class HumiditySensor extends Sensor {
    private final int targetHumidityInPercentage;
    private final int acceptableIncreaseFromTargetHumidity;

    public HumiditySensor(Room room) {
        super(room);
        targetHumidityInPercentage = 40;
        acceptableIncreaseFromTargetHumidity = 10;
    }

    /**
     * This method periodically checks the rooms humidity
     */
    @Override
    public void tenMinutesPassed() {
        super.tenMinutesPassed();
        if (getRoom().getHumidity() > targetHumidityInPercentage + acceptableIncreaseFromTargetHumidity) {
            getRoom().setAllDehumidifiersToActive();
        } else if (getRoom().getHumidity() < targetHumidityInPercentage - acceptableIncreaseFromTargetHumidity) {
            getRoom().setAllDehumidifiersToIdle();
        }
    }
}
