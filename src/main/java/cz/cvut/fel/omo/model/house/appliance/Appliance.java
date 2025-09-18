package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.creature.Person;
import cz.cvut.fel.omo.model.house.InanimateObject;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.event.Event;
import cz.cvut.fel.omo.model.simulation.event.EventType;
import cz.cvut.fel.omo.model.simulation.event.Events;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class represents an appliance.
 */
public class Appliance extends InanimateObject {
    private ApplianceState state;
    private int condition;
    private Documentation documentation;
    // constants
    private final static int DEFAULT_CONDITION = 3000;
    // consumption attributes
    private double idleElectricityConsumptionPerTenMinutes;
    private double activeElectricityConsumptionPerTenMinutes;
    private double electricityConsumptionSinceLastBill;
    private double overallElectricityConsumption; // electricity consumption in the last 3 months
    private double idleWaterConsumptionPerTenMinutes;
    private double activeWaterConsumptionPerTenMinutes;
    private double waterConsumptionSinceLastBill;
    private double overallWaterConsumption; // water consumption in the last 3 months
    private double idleGasConsumptionPerTenMinutes;
    private double activeGasConsumptionPerTenMinutes;
    private double gasConsumptionSinceLastBill;
    private double overallGasConsumption; // gas consumption in the last 3 months

    public Appliance(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room);
        this.state = ApplianceState.OFF;
        this.condition = DEFAULT_CONDITION;
        this.documentation = new Documentation();
        this.activeElectricityConsumptionPerTenMinutes = activeElectricityConsumptionPerTenMinutes;
        this.idleElectricityConsumptionPerTenMinutes = activeElectricityConsumptionPerTenMinutes * 0.05;
        this.activeGasConsumptionPerTenMinutes = activeGasConsumptionPerTenMinutes;
        this.idleGasConsumptionPerTenMinutes = activeGasConsumptionPerTenMinutes * 0.05;
        this.activeWaterConsumptionPerTenMinutes = activeWaterConsumptionPerTenMinutes;
        this.idleWaterConsumptionPerTenMinutes = activeWaterConsumptionPerTenMinutes * 0.05;
        this.overallGasConsumption = 0;
        this.overallElectricityConsumption = 0;
        this.overallWaterConsumption = 0;
    }

    /**
     * This method returns what state the appliance is currently in
     * @return state of the appliance
     */
    public ApplianceState getState() {
        return this.state;
    }

    /**
     * This method sets the turned on state of the appliance.
     * @param state the new state of the appliance
     */
    public void setState(ApplianceState state) {
        this.state = state;
    }

    /**
     * This method replaces the appliance without any cost.
     */
    public void replace() {
        this.state = ApplianceState.OFF;
        if (this instanceof Fridge){state = ApplianceState.ACTIVE;}
        this.condition = DEFAULT_CONDITION;
    }

    /**
     * This method repairs the person try to repair the appliance, the success chance is determined by the repairDifficulty.
     */
    public void repair(){
        Random rand = new Random();
        if (rand.nextInt(100) > documentation.getRepairDifficulty()){
            this.state = ApplianceState.OFF;
            this.condition = DEFAULT_CONDITION;
        }
    }

    /**
     * This method returns the sum of all overall consumption costs.
     */
    public int getOverallCost(){
        int overAllCost = 0;
        overAllCost += overallWaterConsumption;
        overAllCost += overallGasConsumption;
        overAllCost += overallElectricityConsumption;
        return overAllCost;
    }

    @Override
    public void tenMinutesPassed(){
        // if the appliance isn't broken
        if (this.state != ApplianceState.BROKEN) {
            // determine consumption and condition decrease
            double electricityConsumed = 0;
            double waterConsumed = 0;
            double gasConsumed = 0;
            int conditionDecrease = 0;
            switch (this.state) {
                case ACTIVE:
                    // increase consumption
                    electricityConsumed += this.activeElectricityConsumptionPerTenMinutes;
                    waterConsumed += this.activeWaterConsumptionPerTenMinutes;
                    gasConsumed += this.idleGasConsumptionPerTenMinutes;
                    // increase condition decrease
                    conditionDecrease += 2;
                    // break
                    break;
                case IDLE:
                    // increase consumption
                    electricityConsumed += this.idleElectricityConsumptionPerTenMinutes;
                    waterConsumed += this.idleWaterConsumptionPerTenMinutes;
                    gasConsumed += this.idleGasConsumptionPerTenMinutes;
                    // increase condition decrease
                    conditionDecrease += 1;
                    // break
                    break;
                case OFF:
                    break;
                default:
                    Simulation.log(new Log("Unimplemented ApplianceState in tenMinutesPassed.", LogType.ERROR));
                    break;
            }

            // increase consumption since last bill
            this.electricityConsumptionSinceLastBill += electricityConsumed;
            this.waterConsumptionSinceLastBill += waterConsumed;
            this.gasConsumptionSinceLastBill += gasConsumed;
            // increase overall consumption
            this.overallElectricityConsumption += electricityConsumed;
            this.overallWaterConsumption += waterConsumed;
            this.overallGasConsumption += gasConsumed;
            // decrease condition
            this.condition -= conditionDecrease;
            // handle condition <= 0
            if (this.condition <= 0) {
                // set condition to 0, so it isn't negative
                this.condition = 0;
                // set the appliance state to broken
                this.state = ApplianceState.BROKEN;
                // create an event for the appliance break
                Simulation.log(new Log(Appliance.class.getSimpleName() + " created an event to be repaired or replaced.", LogType.EVENT));
                Events.addEvent(new Event(EventType.APPLIANCE_BROKE, new ArrayList<>(List.of(this))));
            } else {
                // handle a random event
                super.tenMinutesPassed();
            }
        }
    }

    public double getGasConsumptionSinceLastBill() {
        return gasConsumptionSinceLastBill;
    }

    public void setGasConsumptionSinceLastBill(long gasConsumptionSinceLastBill) {
        this.gasConsumptionSinceLastBill = gasConsumptionSinceLastBill;
    }

    public double getWaterConsumptionSinceLastBill() {
        return waterConsumptionSinceLastBill;
    }

    public void setWaterConsumptionSinceLastBill(double waterConsumptionSinceLastBill) {
        this.waterConsumptionSinceLastBill = waterConsumptionSinceLastBill;
    }

    public double getElectricityConsumptionSinceLastBill() {
        return electricityConsumptionSinceLastBill;
    }

    public void setElectricityConsumptionSinceLastBill(double electricityConsumptionSinceLastBill) {
        this.electricityConsumptionSinceLastBill = electricityConsumptionSinceLastBill;
    }

    public double getOverallWaterConsumption() {
        return overallWaterConsumption;
    }

    public void setOverallWaterConsumption(double overallWaterConsumption) {
        this.overallWaterConsumption = overallWaterConsumption;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public double getOverallElectricityConsumption() {
        return overallElectricityConsumption;
    }

    public double getOverallGasConsumption() {
        return overallGasConsumption;
    }

    public void resetOverallConsumption() {
        this.overallWaterConsumption = 0;
        this.overallGasConsumption = 0;
        this.overallElectricityConsumption = 0;
    }
}
