package cz.cvut.fel.omo;

import cz.cvut.fel.omo.model.simulation.SetSimulationArgs;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.TimeTracker;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;
import cz.cvut.fel.omo.model.simulation.report.ActivityAndUsageReport;
import cz.cvut.fel.omo.model.simulation.report.ConsumptionReport;
import cz.cvut.fel.omo.model.simulation.report.EventReport;
import cz.cvut.fel.omo.model.simulation.report.HouseConfigurationReport;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // get simulation length from args, or keep default length of 3 years
        long simulationLengthInDays = 360*3;
        if (args.length > 0) {
            try  {
                simulationLengthInDays = Long.parseLong(args[0]);
                if (simulationLengthInDays < 1) {
                    System.out.println("Invalid simulation length: " + simulationLengthInDays + ". Simulation length muset be 1 or higher.");
                    System.exit(1);
                }
            } catch (Exception e) {
                System.out.println("Invalid argument provided. Please provide a valid number for the simulation length.");
                System.exit(1);
            }
        }

        // get the setSimulation configurations from the config
        List<SetSimulationArgs> configurations = new ArrayList<SetSimulationArgs>();
        configurations.add(Config.configurationOne);
        configurations.add(Config.congigurationTwo);

        // loop though the configs
        for (int configurationIndex = 0; configurationIndex < configurations.size(); configurationIndex++) {
            // set the simulation to the current configuration
            Simulation.setSimulation(configurations.get(configurationIndex));

            // get the configuration index as a string
            String configurationIndexAsString = Integer.toString(configurationIndex);
            // log info
            Simulation.log(new Log("Generating a HouseConfigurationReport for configuration " + configurationIndexAsString + ".", LogType.INFO));
            // generate a house configuration report
            HouseConfigurationReport.generate(configurationIndexAsString);

            // loop for simulation length
            while (TimeTracker.getDaysPassed() < simulationLengthInDays) {
                // pass ten minutes
                Simulation.passTenMinutes();
                // if 3 months without 10 minutes passed
                if (TimeTracker.getDaysPassed() != 0 && ((double)TimeTracker.getDaysPassed())%89==0 && TimeTracker.getMinutesPassedThisDay() == 60*24-10) {
                    // log info
                    Simulation.log(new Log("Generating seasonal reports for configuration " + configurationIndexAsString +", year "+ TimeTracker.getCurrentYear() +", months " + (TimeTracker.getCurrentMonthThisYear()-2) + " to " + TimeTracker.getCurrentMonthThisYear()+ ".", LogType.INFO));
                    // generate seasonal reports
                    String subfolder = configurationIndexAsString + "/Year " + TimeTracker.getCurrentYear() + "/Month " + (TimeTracker.getCurrentMonthThisYear()-2) + " to " + TimeTracker.getCurrentMonthThisYear();
                    ActivityAndUsageReport.generate(subfolder);
                    ConsumptionReport.generate(subfolder);
                    EventReport.generate(subfolder);
                }
            }
        }
    }
}