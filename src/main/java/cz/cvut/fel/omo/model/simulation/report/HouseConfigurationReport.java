package cz.cvut.fel.omo.model.simulation.report;

import cz.cvut.fel.omo.model.creature.Creature;
import cz.cvut.fel.omo.model.house.*;
import cz.cvut.fel.omo.model.house.gateway.Gateway;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;
import cz.cvut.fel.omo.model.simulation.report.util.FileWriterUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Design pattern: Singleton
 */
public class HouseConfigurationReport {
    private HouseConfigurationReport() {}

    /**
     * This method generates a HouseConfigurationReport based on the House singleton.
     */
    public static void generate(String subfolder) {
        // get the content from the House singleton
        String content = "Creatures: \n";
        for (Creature c: House.getCreatures()) {
            content += "- " + c.getName() + "\n";
        }
        for (int floorIndex = 0; floorIndex < House.getFloors().size(); floorIndex++) {
            content += "\nFloor " + floorIndex + ": \n";;
            Floor floor = House.getFloors().get(floorIndex);
            for (int roomIndex = 0; roomIndex < floor.getRooms().size(); roomIndex++) {
                Room room = floor.getRooms().get(roomIndex);
                content += "- Room " + roomIndex + ": \n";
                for (int inanimateObjectIndex = 0; inanimateObjectIndex < room.getInanimateObjects().size(); inanimateObjectIndex++) {
                    InanimateObject object = room.getInanimateObjects().get(inanimateObjectIndex);
                    content += "  - " + object.getClass().getName().replace("cz.cvut.fel.omo.model.house.appliance.","").replace("cz.cvut.fel.omo.model.house.sport.","").replace("cz.cvut.fel.omo.model.house.sensor.","") + "\n";
                }
                for (int gatewayIndex = 0; gatewayIndex < room.getGateways().size(); gatewayIndex++) {
                    Gateway gateway = room.getGateways().get(gatewayIndex);
                    content += "  - " + gateway.getClass().getName().replace("cz.cvut.fel.omo.model.house.gateway.door.","").replace("cz.cvut.fel.omo.model.house.gateway.","") + "\n";
                }
                if (!room.getWindows().isEmpty()) {
                    for (int windowIndex = 0; windowIndex < room.getWindows().size(); windowIndex++) {
                        content += "  - Window\n";
                    }
                }
            }
        }
        // add cars
        content += "\nCars: \n";
        for (int carIndex = 0; carIndex < House.getCars().size(); carIndex++) {
            content += "- Car " + carIndex + "\n";
        }

        // save the content to a file
        FileWriterUtil.writeFile(subfolder, "HouseConfigurationReport", content);
    }
}