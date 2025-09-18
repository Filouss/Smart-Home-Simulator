package cz.cvut.fel.omo.model.simulation;

import cz.cvut.fel.omo.Config;
import cz.cvut.fel.omo.model.bill.*;
import cz.cvut.fel.omo.model.creature.Cat;
import cz.cvut.fel.omo.model.creature.Person;
import cz.cvut.fel.omo.model.creature.Role;
import cz.cvut.fel.omo.model.house.*;
import cz.cvut.fel.omo.model.house.appliance.*;
import cz.cvut.fel.omo.model.house.car.Car;
import cz.cvut.fel.omo.model.house.gateway.door.Door;
import cz.cvut.fel.omo.model.house.gateway.Stairs;
import cz.cvut.fel.omo.model.house.sensor.CreatureSensor;
import cz.cvut.fel.omo.model.house.sensor.HumiditySensor;
import cz.cvut.fel.omo.model.house.sensor.TemperatureSensor;
import cz.cvut.fel.omo.model.house.sport.SportsGear;
import cz.cvut.fel.omo.model.house.sport.SportsGearType;
import cz.cvut.fel.omo.model.simulation.event.DoesStuffEveryTenMinutes;
import cz.cvut.fel.omo.model.simulation.event.Event;
import cz.cvut.fel.omo.model.simulation.event.EventType;
import cz.cvut.fel.omo.model.simulation.event.Events;
import cz.cvut.fel.omo.model.simulation.logger.*;
import cz.cvut.fel.omo.model.simulation.weather.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This Class represents a simulation of a smart home.
 * Design pattern: Singleton
 */
public final class Simulation {
    // constants
    public static final double PRICE_PER_UNIT_OF_WATER = 0.1;
    public static final double PRICE_PER_UNIT_OF_ELECTRICITY = 0.1;
    public static final double PRICE_PER_UNIT_OF_GAS = 0.1;
    private static final double RENT = 1000;

    // settings dependant properties
    private static List<DoesStuffEveryTenMinutes> doesStuffEveryTenMinutes = null; // a list of objects implementing the doesStuffEveryTenMinutes interface
    private static List<Appliance> appliances = null;
    private static ActivityLogger loggerChain;
    private static Random rand;
    // bills
    private static long lastTimeBillWasIssuedInMinutes;
    private static int extraApplianceCostsSinceLastBill;
    private static int foodCostsSinceLastBill;
    private static int carCostsSinceLastBill;

    private Simulation() {
    }

    /**
     * This method replaces the current Simulation with a new one.
     * @param setSimulationArgs the args
     */
    public static void setSimulation(SetSimulationArgs setSimulationArgs) {
        // get the vars from the setSimulationArgs
        int totalFloors = setSimulationArgs.totalFloors();
        int roomsPerFloor = setSimulationArgs.roomsPerFloor();
        int totalPeople = setSimulationArgs.totalPeople();
        int totalCats = setSimulationArgs.totalCats();
        long seed = setSimulationArgs.seed();

        // validate input (in compliance with the project requirements)
        if (totalFloors < 1) {
            throw new IllegalArgumentException("totalFloors must be 1 or higher.");
        }
        if (roomsPerFloor < 1) {
            throw new IllegalArgumentException("roomsPerFloor must be 1 or higher.");
        }
        if (totalFloors*roomsPerFloor < 6) {
            throw new IllegalArgumentException("totalFloors*roomsPerFloor must be 6 or higher.");
        }
        if (totalPeople < 3) {
            throw new IllegalArgumentException("The totalPeople arg must be 6 or higher.");
        }
        if (totalCats < 3) {
            throw new IllegalArgumentException("The totalCats arg must be 3 or higher.");
        }

        // reset the logger chain
        loggerChain = new ActivityLogger();
        ErrorLogger errorLogger = new ErrorLogger();
        loggerChain.setNextLogger(errorLogger);
        EventLogger eventLogger = new EventLogger();
        errorLogger.setNextLogger(eventLogger);
        InfoLogger infoLogger = new InfoLogger();
        eventLogger.setNextLogger(infoLogger);
        WarningLogger warningLogger = new WarningLogger();
        infoLogger.setNextLogger(warningLogger);

        // log simulation setup
        log(new Log("Setting up a new simulation.", LogType.INFO));

        // reset events
        Events.resetEvents();

        // reset the TimeTracker
        TimeTracker.reset();

        // update the Weather, this must be done after resetting the time tracker, since the weather is time based
        Weather.update();

        // set doesStuffEveryTenMinutes to an empty list
        doesStuffEveryTenMinutes = new ArrayList<>();

        // set appliances to an empty list
        appliances = new ArrayList<>();

        // reset the last time bill was issued variable
        lastTimeBillWasIssuedInMinutes = 0;

        // reset costs since last bill
        extraApplianceCostsSinceLastBill = 0;
        foodCostsSinceLastBill = 0;
        carCostsSinceLastBill = 0;

        // reset the house
        House.reset();

        // set the rand to a new random with the specified seed
        rand = new Random(seed);

        // generate floors for the house
        for (int floorIndex = 0; floorIndex < totalFloors; floorIndex++) {
            Floor floor = new Floor();
            // generate rooms for the floor object
            for (int roomIndex = 0; roomIndex < roomsPerFloor; roomIndex++) {
                Room room = new Room(floor);
                doesStuffEveryTenMinutes.add(room);
                // generate windows for the room object
                int numberOfWindows = rand.nextInt(4)+1;
                for (int windowIndex = 0; windowIndex < numberOfWindows; windowIndex++) {
                    Window window = new Window();
                    room.addWindow(window);
                }
                // generate lights
                int numberOfLights = 1+rand.nextInt(3);
                for (int lightIndex = 0; lightIndex < numberOfLights; lightIndex++) {
                    Light light = new Light(room, 0.1, 0, 0);
                    room.addInanimateObject(light);
                    appliances.add(light);
                }
                // add a creature sensor
                CreatureSensor creatureSensor = new CreatureSensor(room);
                room.addInanimateObject(creatureSensor);
                doesStuffEveryTenMinutes.add(creatureSensor);
                // add a humidity sensor and a dehumidifier
                HumiditySensor humiditySensor = new HumiditySensor(room);
                room.addInanimateObject(humiditySensor);
                doesStuffEveryTenMinutes.add(humiditySensor);
                Dehumidifier dehumidifier = new Dehumidifier(room, 0.1, 0, 0);
                room.addInanimateObject(dehumidifier);
                appliances.add(dehumidifier);
                // add a heater and temperatureSensor
                TemperatureSensor temperatureSensor = new TemperatureSensor(room);
                doesStuffEveryTenMinutes.add(temperatureSensor);
                Heater heater = new Heater(room, 1.5, 0, 0);
                room.addInanimateObject(heater);
                room.addInanimateObject(temperatureSensor);
                appliances.add(heater);
                // add the room to the floor
                floor.addRoom(room);
            }
            // add the floor to the house
            House.addFloor(floor);
        }

        // randomly place appliances into rooms
        for (int roundIndex = 0; roundIndex < 3; roundIndex++) {
            for (int applianceIndex = 0; applianceIndex < 8; applianceIndex++) {
                Room randomRoom = House.getRandomRoom();
                Appliance appliance = null;
                switch (applianceIndex) {
                    case 0:
                        appliance = new CoffeeMachine(randomRoom, 1, 0, 1);
                        break;
                    case 1:
                        appliance = new Fridge(randomRoom, 2, 0, 0);
                        break;
                    case 2:
                        appliance = new Kettle(randomRoom, 1, 0, 0);
                        break;
                    case 3:
                        appliance = new LavaLamp(randomRoom, 0.3, 0, 0);
                        break;
                    case 4:
                        appliance = new Microwave(randomRoom, 1, 0, 0);
                        break;
                    case 5:
                        appliance = new Printer(randomRoom, 0.5, 0, 0);
                        break;
                    case 6:
                        appliance = new Radio(randomRoom, 0.2, 0, 0);
                        break;
                    case 7:
                        appliance = new Television(randomRoom, 1, 0, 0);
                        break;
                }
                if (appliance != null) {
                    randomRoom.addInanimateObject(appliance);
                    appliances.add(appliance);
                } else {
                    log(new Log("Simulation generated a null appliance.", LogType.ERROR));
                }
            }
        }

        // create a list of sports gear types
        List<SportsGearType> sportsGearTypes = new ArrayList<>();
        sportsGearTypes.add(SportsGearType.BIKE);
        sportsGearTypes.add(SportsGearType.SKIS);
        // randomly generate sports gear into rooms, so there is at least 1 piece of sports gear for each person
        for (int round = 0; round < totalPeople; round++) {
            // get a random room
            Room randomRoom = House.getRandomRoom();
            randomRoom.addInanimateObject(new SportsGear(randomRoom, sportsGearTypes.get(round%2)));
        }

        // generate the main entrance door in the first room on the first floor
        Room entraceRoom = House.getFloors().getFirst().getRooms().getFirst();
        Door entraceDoor = new Door(entraceRoom, null);
        entraceRoom.addGateway(entraceDoor);

        // loop through all floors
        for (int i = 0; i < totalFloors; i++) {
            // designate the first room on each floor a hallway
            Room hallway = House.getFloors().get(i).getRooms().getFirst();
            // if the floor has more than one room (the hallway room), put doors in the hallway leading to each other room
            if (roomsPerFloor > 1) {
                for (int j = 1; j < roomsPerFloor; j++) {
                    Room room = House.getFloors().get(i).getRooms().get(j);
                    Door door = new Door(room, hallway);
                    hallway.addGateway(door);
                    room.addGateway(door);
                }
            }
            // if the house has a floor above this floor, put stairs in the hallway leading to the hallway above
            if (i+1 < totalFloors) {
                Room hallwayAbove = House.getFloors().get(i+1).getRooms().getFirst();
                Stairs stairs = new Stairs(hallway, hallwayAbove);
                hallway.addGateway(stairs);
                hallwayAbove.addGateway(stairs);
            }
        }

        // get all rooms
        List<Room> rooms = House.getRooms();

        // generate an admin
        List<Role> adminRoles = new ArrayList<>();
        adminRoles.add(Role.ADMIN);
        Person admin = new Person("Admin 0", House.getRandomRoom(), adminRoles);
        doesStuffEveryTenMinutes.add(admin);
        House.addCreature(admin);

        // generate the rest of the people
        for (int i = 0; i < totalPeople-1; i++) {
            Person person = new Person("Person " + i, House.getRandomRoom(), new ArrayList<>());
            doesStuffEveryTenMinutes.add(person);
            House.addCreature(person);
        }

        // generate cats
        for (int i = 0; i < totalCats; i++) {
            Cat cat = new Cat("Cat " + i, House.getRandomRoom());
            doesStuffEveryTenMinutes.add(cat);
            House.addCreature(cat);
        }

        // add two cars
        Car carA = new Car("CarA" ,10, 1, 2);
        Car carB = new Car("CarB" , 10, 1, 4);
        House.addCar(carA);
        House.addCar(carB);

        doesStuffEveryTenMinutes.addAll(appliances);
        doesStuffEveryTenMinutes.add(carA);
        doesStuffEveryTenMinutes.add(carB);
    }

    /**
     * This method runs the tenMinutesPassed method on all objects implementing the DoesStuffEveryTenMinutes interface.
     * Design pattern: Observer - Subject
     */
    public static void passTenMinutes() {
        // validate that the Simulation was set already
        if (House.getFloors() == null || doesStuffEveryTenMinutes == null) {
            throw new NullPointerException("The simulation was not set up correctly.");
        }

        // execute the tenMinutesPassed method on all observers
        for (DoesStuffEveryTenMinutes object : doesStuffEveryTenMinutes) {
            object.tenMinutesPassed();
        }
        // set lastTimeBillWasIssuedInMinutes to 10 more minutes
        lastTimeBillWasIssuedInMinutes += 10;
        // if a month has passed since the last time the house was billed
        if (lastTimeBillWasIssuedInMinutes >= 43200) {
            // get total appliance costs (just the extra appliance costs, since the rest of appliance costs are energies and those have separate bills)
            double totalApplianceCosts = extraApplianceCostsSinceLastBill;
            // loop through appliances and add up their energy consumptions since last bill and also reset their consumptions since last bill
            double totalGasConsumption = 0;
            double totalElectricityConsumption = 0;
            double totalWaterConsumption = 0;
            for (Appliance appliance : appliances) {
                totalGasConsumption += appliance.getGasConsumptionSinceLastBill();
                totalElectricityConsumption += appliance.getElectricityConsumptionSinceLastBill();
                totalWaterConsumption += appliance.getWaterConsumptionSinceLastBill();
                appliance.setElectricityConsumptionSinceLastBill(0);
                appliance.setWaterConsumptionSinceLastBill(0);
                appliance.setGasConsumptionSinceLastBill(0);
            }
            // get the total energy costs
            double totalGasCosts = totalGasConsumption*PRICE_PER_UNIT_OF_GAS;
            double totalElectricityCosts = totalElectricityConsumption*PRICE_PER_UNIT_OF_ELECTRICITY;
            double totalWaterCosts = totalWaterConsumption*PRICE_PER_UNIT_OF_WATER;

            // generate a bill Matryoshka
            Bill totalBill = generateFullBill(new HouseBill(RENT),totalGasCosts,totalElectricityCosts,totalWaterCosts,totalApplianceCosts,foodCostsSinceLastBill,carCostsSinceLastBill);

            // create a BILL_IS_WAITING_FOR_PAYMENT type Event with the bill as the object
            Events.addEvent(new Event(EventType.BILL_IS_WAITING_FOR_PAYMENT, new ArrayList<>(List.of(totalBill))));

            // reset costs since last bill
            extraApplianceCostsSinceLastBill = 0;
            foodCostsSinceLastBill = 0;
            carCostsSinceLastBill = 0;

            lastTimeBillWasIssuedInMinutes = 0;
        }
        // update the time tracker
        TimeTracker.tenMinutesPassed();
        // update the weather
        Weather.update();
    }

    /**
     * This method layers all the bills and generates a final bill containing all the sub-bills
     * @param houseBill the first bill containing the rent
     * @param gasCost the cost of gas to be paid
     * @param elCost the cost of electricity to be paid
     * @param waterCost the cost of water to be paid
     * @param applianceCost the cost of appliance repairs and linked costs
     * @param foodCost the cost of food to be paid
     * @param carCost the cost of petrol to be paid
     */
    public static Bill generateFullBill(Bill houseBill, double gasCost, double elCost, double waterCost, double applianceCost, double foodCost, double carCost){
        //since no appliances use gas, it's not included
        Bill total = houseBill;
        total = new ElectricityBill(total,elCost);
        total = new WaterBill(total,waterCost);
        total = new GasBill(total,gasCost);
        total = new FoodBill(total,foodCost);
        total = new ApplianceBill(total, applianceCost);
        total = new CarBill(total,carCost);
        return total;
    }

    /**
     * This method logs a log.
     */
    public static void log(Log log) {
        loggerChain.log(log);
    }

    /**
     * This method returns the Simulation's instance of Random.
     * @return the Simulation's instance of random
     */
    public static Random getRand() {
        return rand;
    }

    /**
     * This method increases the amount of extra appliance costs since last bill (everything aside from electricity, gas, and water)
     * @param amount the amount to increase by
     */
    public static void increaseExtraApplianceCostsSinceLastBill(int amount) {
        extraApplianceCostsSinceLastBill += amount;
    }

    /**
     * This method increases the food costs since the last bill.
     * @param amount the amount to increase by
     */
    public static void increaseFoodCostsSinceLastBill(int amount) {
        foodCostsSinceLastBill += amount;
    }

    /**
     * This method increases the car costs since the last bill.
     * @param amount the amount to increase by
     */
    public static void increaseCarCostsSinceLastBill(int amount) {
        carCostsSinceLastBill += amount;
    }

    /**
     * This method returns a list of all appliances in the simulation.
     * @return a list of all appliances in the simulation
     */
    public static List<Appliance> getAppliances() {
        return appliances;
    }
}
