package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents a lavalamp.
 */
public class LavaLamp extends Light {
    public LavaLamp(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
    }
}
