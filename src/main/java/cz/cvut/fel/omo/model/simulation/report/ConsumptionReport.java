package cz.cvut.fel.omo.model.simulation.report;

import cz.cvut.fel.omo.model.house.appliance.Appliance;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;
import cz.cvut.fel.omo.model.simulation.report.util.FileWriterUtil;

/**
 * Design pattern: Singleton
 */
public class ConsumptionReport {
    private ConsumptionReport() {}

    /**
     * This method generates a ConsumptionReport for the past 3 months.
     */
    public static void generate(String subfolder) {
        // create a new content string
        String content = "";
        // create vars for total consumption
        double totalElConsumptionSinceLastReport = 0;
        double totalGasConsumptionSinceLastReport = 0;
        double totalWaterConsumptionSinceLastReport = 0;
        // loop through all appliances
        for (Appliance appliance: Simulation.getAppliances()) {
            // get the current appliance's overall consumptions
            double elConsumptionSinceLastReport = appliance.getOverallElectricityConsumption();
            double gasConsumptionSinceLastReport = appliance.getOverallGasConsumption();
            double waterConsumptionSinceLastReport = appliance.getOverallWaterConsumption();
            // add them to the total
            totalElConsumptionSinceLastReport += elConsumptionSinceLastReport;
            totalGasConsumptionSinceLastReport += gasConsumptionSinceLastReport;
            totalWaterConsumptionSinceLastReport += waterConsumptionSinceLastReport;
            // reset them
            appliance.resetOverallConsumption();
            // add them to the content
            content += "An instance of " + appliance.getClass().getName().replace("cz.cvut.fel.omo.model.house.appliance.", "")
                    + " consumed "
                    + String.format("%.2f", elConsumptionSinceLastReport) + " electricity (" + String.format("%.2f", elConsumptionSinceLastReport * Simulation.PRICE_PER_UNIT_OF_ELECTRICITY) + " CZK), "
                    + String.format("%.2f", waterConsumptionSinceLastReport) + " water (" + String.format("%.2f", waterConsumptionSinceLastReport * Simulation.PRICE_PER_UNIT_OF_WATER) + " CZK), and "
                    + String.format("%.2f", gasConsumptionSinceLastReport) + " gas (" + String.format("%.2f", gasConsumptionSinceLastReport * Simulation.PRICE_PER_UNIT_OF_GAS) + " CZK)."
                    + "\n";
        }
        // add the total to the content
        content += "\nTOTAL: "
                + String.format("%.2f", totalElConsumptionSinceLastReport) + " electricity (" + String.format("%.2f", totalElConsumptionSinceLastReport * Simulation.PRICE_PER_UNIT_OF_ELECTRICITY) + " CZK), "
                + String.format("%.2f", totalWaterConsumptionSinceLastReport) + " water (" + String.format("%.2f", totalWaterConsumptionSinceLastReport * Simulation.PRICE_PER_UNIT_OF_WATER) + " CZK), and "
                + String.format("%.2f", totalGasConsumptionSinceLastReport) + " gas (" + String.format("%.2f", totalGasConsumptionSinceLastReport * Simulation.PRICE_PER_UNIT_OF_GAS) + " CZK)."
                + "\n";

        // save the content to a file
        FileWriterUtil.writeFile(subfolder, "ConsumptionReport", content);
    }
}
