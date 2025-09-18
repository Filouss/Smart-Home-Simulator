package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.TimeTracker;

import java.util.Random;

/**
 * This class represents appliance documentation which is studied when an appliance is broken.
 */
public class Documentation {
    private final long warrantyExpirationTimeInMinutes;
    private final int repairDifficulty;

    public Documentation() {
        //warranty is always 2 years
        this.warrantyExpirationTimeInMinutes = TimeTracker.getMinutesPassed()+ Simulation.getRand().nextInt(2*365*24*60);
        //random repair difficulty from 0 to 90
        repairDifficulty = Simulation.getRand().nextInt(20);
    }

    public long getWarrantyExpirationTimeInMinutes() {
        return warrantyExpirationTimeInMinutes;
    }

    public int getRepairDifficulty() {
        return repairDifficulty;
    }
}
