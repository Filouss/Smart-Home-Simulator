package cz.cvut.fel.omo.model.house;

import cz.cvut.fel.omo.model.creature.Creature;
import cz.cvut.fel.omo.model.house.car.Car;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the house.
 * Design pattern: Singleton
 */
public final class House {
    private static List<Floor> floors = null; // the floors of the house
    private static List<Creature> creatures = null; // the inhabitants of the house (they may be outside)
    private static List<Car> cars = null; // list of cars belonging to this household

    /**
     * This constructor constructs a new house with no floors.
     */
    private House() {}

    /**
     * This method resets the house.
     */
    public static void reset() {
        floors = new ArrayList<>();
        creatures = new ArrayList<>();
        cars = new ArrayList<>();
    }

    /**
     * This method adds a floor to the house.
     * @param floor the floor to add
     */
    public static void addFloor(Floor floor) {
        if (floors == null) {
            Simulation.log(new Log("The house was not setup properly.", LogType.ERROR));
        }

        floors.add(floor);
    }

    /**
     * This method adds a creature as an inhabitant of the house.
     * This doesn't place the creature inside a room or register it as an observer.
     * This just associates the creature with the house for HouseConfigurationReport generation and similar purposes.
     * @param creature the creature to add
     */
    public static void addCreature(Creature creature) {
        if (creatures == null) {
            Simulation.log(new Log("The house was not setup properly.", LogType.ERROR));
        }
        creatures.add(creature);
    }

    /**
     * This method adds a car to the house.
     * @param car the car to add
     */
    public static void addCar(Car car) {
        if (cars == null) {
            Simulation.log(new Log("The house was not setup properly.", LogType.ERROR));
        }
        cars.add(car);
    }

    /**
     * This method returns the floors of the house.
     * @return the floors of the house
     */
    public static List<Floor> getFloors() {
        return floors;
    }

    /**
     * This method returns a list of inhabitants of the house.
     * The list may include creatures that are currently outside the house.
     * @return the inhabitants of the house
     */
    public static List<Creature> getCreatures() {
        return creatures;
    }

    /**
     * This method returns a list of all rooms in the house.
     * @return a list of rooms in the house
     */
    public static List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        for (Floor floor: floors) {
            rooms.addAll(floor.getRooms());
        }
        return rooms;
    }

    /**
     * This method returns a random room.
     * @return a random room
     */
    public static Room getRandomRoom() {
        List<Room> allRooms = House.getRooms();
        int randomRoomIndex = Simulation.getRand().nextInt(allRooms.size());
        return allRooms.get(randomRoomIndex);
    }

    /**
     * This method returns list of all cars assigned to this house.
     * @return List of the cars.
     */
    public static List<Car> getCars() {
        return cars;
    }
}
