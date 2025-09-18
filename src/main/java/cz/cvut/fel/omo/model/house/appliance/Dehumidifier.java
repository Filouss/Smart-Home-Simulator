package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.creature.Person;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.event.Event;
import cz.cvut.fel.omo.model.simulation.event.EventType;
import cz.cvut.fel.omo.model.simulation.event.Events;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.List;

/**
 * This class represents a Dehumidifier.
 */
public class Dehumidifier extends Appliance {
    private int waterCapacity = 0;
    private final int maxWaterCapacity = 1000;

    public Dehumidifier(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
    }

    /**
     * This method periodically decreases the rooms humidity if the dehumidifier is active
     */
    @Override
    public void tenMinutesPassed() {
        super.tenMinutesPassed();
        // if the dehumidifier is active, decrease humidity of the room by 1%
        if (getState() == ApplianceState.ACTIVE){
            // if the dehumidifier is full
            if (waterCapacity == maxWaterCapacity){
                // generate an event
                Events.addEvent(new Event(EventType.DEHUMIDIFIER_IS_FULL, List.of(this)));
                Simulation.log(new Log("A dehumidifier created an event to be emptied out.", LogType.EVENT));
            // else
            } else {
                // increase dehumidified water in the appliance
                waterCapacity += 1;
                // decrease the room humidity
                getRoom().setHumidity(getRoom().getHumidity()-1);
            }
        }
    }

    /**
     * This method gets called when the water capacity of this dehumidifier is full and resets it
     */
    public void emptyDehumidifierWater() {
        this.waterCapacity = 0;
    }
}
