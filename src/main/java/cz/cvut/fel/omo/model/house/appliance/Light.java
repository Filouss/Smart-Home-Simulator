package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.house.Room;

public class Light extends Appliance {
    public Light(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
    }
}