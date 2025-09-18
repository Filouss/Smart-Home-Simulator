package cz.cvut.fel.omo.model.simulation;

import cz.cvut.fel.omo.Config;
import cz.cvut.fel.omo.model.creature.Cat;
import cz.cvut.fel.omo.model.creature.Creature;
import cz.cvut.fel.omo.model.creature.Person;
import cz.cvut.fel.omo.model.house.House;
import cz.cvut.fel.omo.model.house.InanimateObject;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.house.appliance.Appliance;
import cz.cvut.fel.omo.model.house.sport.SportsGear;
import cz.cvut.fel.omo.model.house.sport.SportsGearType;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    /**
     * This method returns a list of setSimulationArgs from the Config.
     * @return a list of setSimulationArgs from the Config
     */
    static Stream<SetSimulationArgs> configurations() {
        return Stream.of(
                Config.configurationOne,
                Config.congigurationTwo);
    }

    // base set simulation tests
    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_doesNotThrowException(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);
    }

    // house configuration tests (as per project requirements)
    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_generatesAtLeastTwentyAppliances(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);
        // get the appliance count
        long totalAppliances = Simulation.getAppliances().size();

        // assert if at least 20 appliances are in the house
        Assertions.assertTrue(totalAppliances > 20);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_generatesAtLeastSixRooms(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);
        // get the total room count
        long roomCount = House.getRooms().size();

        // assert if at least 6 rooms are in the house
        Assertions.assertTrue(roomCount >= 6);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_generatesAtLeastThreeCats(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);

        int counter = 0;

        for (Creature creature : House.getCreatures()){
            if (creature instanceof Cat){
                counter++;
            }
        }

        Assertions.assertTrue(counter >= 3);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_generatesAtLeastSixPeople(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);

        int counter = 0;

        for (Creature creature : House.getCreatures()){
            if (creature instanceof Person){
                counter++;
            }
        }

        Assertions.assertTrue(counter >= 6);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_generatesAtLeastEightTypesOfAppliances(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);

        Set<Class<?>> distinctApplianceTypes = new HashSet<>();

        for (Room room : House.getRooms()) {
            for (InanimateObject object : room.getInanimateObjects()) {
                if (object instanceof Appliance) {
                    distinctApplianceTypes.add(object.getClass());
                }
            }
        }
        Assertions.assertTrue(distinctApplianceTypes.size() >= 8);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_generatesAtLeastTwoBikes(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);

        int bikeCounter = 0;
        for (Room room : House.getRooms()){
            for (InanimateObject object : room.getInanimateObjects()){
                if (object instanceof SportsGear && ((SportsGear) object).getType() == SportsGearType.BIKE){
                    bikeCounter++;
                }
            }
        }
        Assertions.assertTrue(bikeCounter >= 2);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_generatesAtLeastOneSki(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);

        int skiCounter = 0;
        for (Room room : House.getRooms()){
            for (InanimateObject object : room.getInanimateObjects()){
                if (object instanceof SportsGear && ((SportsGear) object).getType() == SportsGearType.SKIS){
                    skiCounter++;
                }
            }
        }
        Assertions.assertTrue(skiCounter >= 1);
    }

    // base passTenMinutes tests
    @ParameterizedTest
    @MethodSource("configurations")
    public void passTenMinutes_doesNotThrowException(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);
        // pass ten minutes
        Simulation.passTenMinutes();
    }


    // time tracking tests
    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_timeStartsAtZeroMinutes(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);

        // asset if minutes passed is 0
        assertEquals(TimeTracker.getMinutesPassed(), 0);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void passTenMinutes_changesTimeByTenMinutes(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);
        // pass ten minutes
        Simulation.passTenMinutes();

        // assert if 10 minutes passed
        assertEquals(TimeTracker.getMinutesPassed(), 10);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void setSimulation_resetsTimeTracker(SetSimulationArgs setSimulationArgs) {
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);
        // pass ten minutes
        Simulation.passTenMinutes();
        // pass ten minutes
        Simulation.passTenMinutes();
        // set the simulation
        Simulation.setSimulation(setSimulationArgs);

        // assert if the time tracker was reset
        assertEquals(TimeTracker.getMinutesPassed(), 0);
    }
}