package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.simulation.Simulation;

/**
 * This class represents a coffee machine.
 */
public class CoffeeMachine extends Appliance {
    private int coffeeCapacity;
    private final int maxCoffeeCapacity = 100;

    public CoffeeMachine(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
        this.coffeeCapacity = 0;
    }

    /**
     * This method refills the coffee capacity of this coffee machine
     */
    public void refillCoffee(){
        int coffeeToRefill = maxCoffeeCapacity - coffeeCapacity;
        this.coffeeCapacity = maxCoffeeCapacity;
        Simulation.increaseExtraApplianceCostsSinceLastBill(coffeeToRefill);
    }

    /**
     * This method returns the current coffee capacity.
     * @return the current coffee capacity (not the max)
     */
    public int getCapacity(){
        return coffeeCapacity;
    }

    /**
     * This method makes coffee, decreasing coffee capacity
     */
    public void makeCoffee(){
        coffeeCapacity--;
    }
}
