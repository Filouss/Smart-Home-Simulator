package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents an electric heater.
 */
public class Heater extends Appliance {

    public Heater(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
    }

    /**
     * This method periodically increases the rooms temperature if the heater is active
     */
    @Override
    public void tenMinutesPassed() {
        super.tenMinutesPassed();
        // if the heater was turned on, increase temperature of the room by 0.5 degree Celsius per ten minutes
        if (getState() == ApplianceState.ACTIVE){
            getRoom().setTemperature(getRoom().getTemperature()+0.5);
        }
    }
}
