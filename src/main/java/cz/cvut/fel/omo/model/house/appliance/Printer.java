package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.simulation.Simulation;

/**
 * This class represents a printer.
 */
public class Printer extends Appliance {
    private int paperCapacity;
    private final int maxPaperCapacity = 50;
    /**
     * This method constructs a printer with the specified parameters.
     * @param room the room the printer is in
     */
    public Printer(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
    }

    /**
     * This method refills the paper capacity of this printer
     */
    public void refillPaper(){
        int paperToRefill = maxPaperCapacity - paperCapacity;
        paperCapacity = maxPaperCapacity;
        Simulation.increaseExtraApplianceCostsSinceLastBill(paperToRefill);
    }

    /**
     * This method prints documents, decreasing paper capacity
     */
    public void printDocuments(int totalPapers){
        paperCapacity -= totalPapers;
    }

    /**
     * This method return the current paper capacity (not the max).
     * @return the current paper capacity (not the max)Q
     */
    public int getPaperCapacity(){
        return paperCapacity;
    }
}
