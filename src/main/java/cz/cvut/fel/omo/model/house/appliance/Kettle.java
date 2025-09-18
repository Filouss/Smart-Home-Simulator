package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.house.Room;

/**
 * This class represents a kettle.
 */
public class Kettle extends Appliance {
    private int waterCapacity;
    private final int maxWaterCapacity = 100;

    public Kettle(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
        this.waterCapacity = 0;
    }

    /**
     * This method refills the water capacity of this kettle
     */
    public void refillWater(){
        // refill the water
        double waterToRefill = maxWaterCapacity - waterCapacity;
        this.waterCapacity = maxWaterCapacity;
        // increase water consumption
        this.setWaterConsumptionSinceLastBill(this.getWaterConsumptionSinceLastBill() + waterToRefill);
        this.setOverallWaterConsumption(this.getOverallWaterConsumption() + waterToRefill);
    }

    /**
     * This method makes tea, decreasing water capacity
     */
    public void makeTea(){
        waterCapacity--;
    }

    /**
     * This method returns the current water capacity (not the max.)
     * @return the current water capacity (not the max.)
     */
    public int getCapacity(){
        return waterCapacity;
    }
}
