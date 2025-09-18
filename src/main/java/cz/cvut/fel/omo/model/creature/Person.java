package cz.cvut.fel.omo.model.creature;

import cz.cvut.fel.omo.model.house.House;
import cz.cvut.fel.omo.model.house.InanimateObject;
import cz.cvut.fel.omo.model.house.Window;
import cz.cvut.fel.omo.model.house.appliance.*;
import cz.cvut.fel.omo.model.house.car.Car;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.house.car.CarState;
import cz.cvut.fel.omo.model.house.food.Food;
import cz.cvut.fel.omo.model.house.food.FoodType;
import cz.cvut.fel.omo.model.house.gateway.Gateway;
import cz.cvut.fel.omo.model.house.gateway.door.Door;
import cz.cvut.fel.omo.model.house.gateway.door.DoorState;
import cz.cvut.fel.omo.model.house.gateway.door.LockedState;
import cz.cvut.fel.omo.model.house.sport.SportsGear;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.TimeTracker;
import cz.cvut.fel.omo.model.simulation.event.EventType;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.*;

/**
 * This class represents a person residing in the house.
 */
public class Person extends Creature {
    private SportsGear borrowedSportsGear; // the sports gear the person is currently borrowing (if any)
    private Car car; // the car the person is currently using
    private List<Role> roles; // the person's roles (e.g. ADMIN)
    private Appliance lastUsedAppliance; //the appliance that the person was using for the ten-minute tick
    /**
     * This constructor constructs a new Person object using the specified parameters.
     * @param name the name of the person
     * @param room the room the person is in
     */
    public Person(String name, Room room, List<Role> roles) {
        super(name, 2000, room);
        this.addEventTypeItCanHandle(EventType.CAT_ASKED_FOR_FOOD);
        this.addEventTypeItCanHandle(EventType.APPLIANCE_BROKE);
        this.addEventTypeItCanHandle(EventType.DEHUMIDIFIER_IS_FULL);
        this.addEventTypeItCanHandle(EventType.CAR_OUT_OF_FUEL);
        this.roles = roles;
        if (this.roles.contains(Role.ADMIN)) {
            this.addEventTypeItCanHandle(EventType.BILL_IS_WAITING_FOR_PAYMENT);
        }
    }

    /**
     * This method returns the sports gear the person has borrowed.
     * @return the sports gear the person has borrowed
     */
    public SportsGear getBorrowedSportsGear() {
        return borrowedSportsGear;
    }

    /**
     * This method makes the person borrow the sports gear as long as it's in the same room as them.
     * @param sportsGear the sports gear to borrow
     */
    public void borrowSportsGear(SportsGear sportsGear) {
        // validate that the person is in the same room as the sports gear
        if (this.getRoom() != sportsGear.getRoom()) {
            Simulation.log(new Log("This object is trying to borrow sports gear in a different room, it must travel there first.", LogType.ERROR));
            return;
        }
        // validate that the sportsGear is not borrowed already
        if (sportsGear.isBorrowed()) {
            Simulation.log(new Log("This object is trying to borrow sports gear that's already borrowed.", LogType.ERROR));
            return;
        }

        sportsGear.setBorrowed(true);
        this.borrowedSportsGear = sportsGear;
    }

    /**
     * This method makes the person return the sports gear they borrowed as long as it belongs to the room they are in.
     */
    public void returnSportsGear() {
        // validate that the person is in the right room
        if (this.borrowedSportsGear.getRoom() != this.getRoom()) {
            Simulation.log(new Log("This object is trying to return sports gear without being in its original room, it must travel there first.", LogType.ERROR));
            return;
        }

        // return the sports gear
        this.borrowedSportsGear.setBorrowed(false);
        this.borrowedSportsGear = null;
        setState(CreatureState.IDLE);
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * This method returns true if the person should be sleeping right now.
     * @return whether the person should be sleeping right now
     */
    public boolean shouldSleep() {
        return TimeTracker.getHoursPassedThisDay() < 8;
    }

    /**
     * This method returns true if the person should be doing sports right now.
     * @return whether the person should be doing sports right now
     */
    public boolean shouldDoSports() {
        return TimeTracker.getHoursPassedThisDay() > 8 && TimeTracker.getHoursPassedThisDay() < 16;
    }

    /**
     * This method makes the person turn off their last used appliance on a 50% chance.
     */
    private void fiftyPercentChanceToTurnOffLastUsedAppliance() {
        if (lastUsedAppliance != null && lastUsedAppliance.getState() != ApplianceState.BROKEN) {
            if (Simulation.getRand().nextBoolean() ){
                lastUsedAppliance.setState(ApplianceState.OFF);
            } else {
                lastUsedAppliance.setState(ApplianceState.IDLE);
            }
        }
    }

    /**
     * This method makes the person go to bed, if they haven't gone already.
     */
    private void goToBed() {
        // if the person isn't already sleeping
        if (this.getState() != CreatureState.SLEEPING) {
            // make the person sleep
            this.setState(CreatureState.SLEEPING);
            // log activity
            Simulation.log(new Log(this.getName() + " went to bed.", LogType.ACTIVITY));
        }
    }

    /**
     * This method makes the person get out of bed, if they aren't out already.
     */
    private void getOutOfBed() {
        // if the person is sleeping, set them to IDLE
        if (this.getState() == CreatureState.SLEEPING) {
            // make the person IDLE
            this.setState(CreatureState.IDLE);
            // log activity
            Simulation.log(new Log(this.getName() + " got out of bed.", LogType.ACTIVITY));
        }
    }

    /**
     * This method makes the person try to go do sports.
     */
    private void doSports() {
        SportsGear toBorrow = null;
        // if the creature doesn't have sports gear
        if (this.getBorrowedSportsGear() == null) {
            // get a room with available sports gear
            Room roomWithSportsGear = null;
            if (getUnborrowedSportsGear() != null) {
                roomWithSportsGear = getUnborrowedSportsGear().getRoom();
            }
            //get the sports gear
            toBorrow = getUnborrowedSportsGear();

            // if such room exists
            if (roomWithSportsGear != null && toBorrow != null) {
                // travel to the room
                this.travelToRoom(roomWithSportsGear);
                // borrow the sports gear
                borrowSportsGear(toBorrow);
                // log activity
                Simulation.log(new Log(this.getName() + " borrowed Sports Gear.", LogType.ACTIVITY));
            } else {
                    Simulation.log(new Log(this.getName() + " failed to find a room with Sports Gear.", LogType.ACTIVITY));
                }
        }
        // if the person has sports gear now
        if (this.getBorrowedSportsGear() != null) {
            // go do sports
            setState(CreatureState.DOING_SPORTS);
            // log activity
            Simulation.log(new Log(this.getName() + " is doing sports.", LogType.ACTIVITY));
        }
    }

    /**
     * This method makes the person go do a random activity.
     */
    public void doRandomActivity() {
        // get a random activity number
        int activityNumber = Simulation.getRand().nextInt(8);
        // do the random activity
        switch (activityNumber) {
            // random activity number 0: makeCoffee
            case 0:
                makeCoffee();
                break;
            // random activity number 1: makeTea
            case 1:
                makeTea();
                break;
            // random activity number 2: print random amount of documents
            case 2:
                printDocuments(Simulation.getRand().nextInt(10));
                break;
            // random activity number 3: watchTV
            case 3:
                watchTV();
                break;
            // random activity number 4: listenToRadio
            case 4:
                listenToRadio();
                break;
            // random activity number 5: watchLavaLamp
            case 5:
                watchLavaLamp();
                break;
            //random activity number 6: lookOutOfAWindow
            case 6:
                lookOutOfAWindow();
                break;
            // random activity number 7: drive a car
            case 7:
                driveACar();
                break;
            // default (unimplemented random activity)
            default:
                Simulation.log(new Log("Unimplemented random activity number in Person: " + Integer.toString(activityNumber), LogType.ERROR));
                break;
        }
    }

    /**
     * This method makes the person try to go eat.
     */
    private void goEat() {
        // find a room with fridge
        Room room = findRoomWithAppliance(Fridge.class);
        // if you find it
        if (room != null) {
            travelToRoom(room);
            Food toConsume = getFoodFromFridge(getRoom().getAppliance(Fridge.class),FoodType.HUMAN_FOOD);
            if (toConsume != null) {
                microwaveFood();
                consumeFood(toConsume);
            } else {
                Simulation.log(new Log(this.getName() + " failed to find food to consume.", LogType.ACTIVITY));
            }
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working Fridge.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, Fridge.class);
        }
    }

    @Override
    public void tenMinutesPassed() {
        if (!this.shouldDoSports() && borrowedSportsGear != null) {
            travelToRoom(getBorrowedSportsGear().getRoom());
            returnSportsGear();
        }

        // decrease the creature's calories
        this.decreaseCaloriesByTenMinutes();

        // 50/50 chance to turn off the previously used appliance
        if (!(lastUsedAppliance instanceof Fridge)){fiftyPercentChanceToTurnOffLastUsedAppliance();}

        if (getState() == CreatureState.DRIVING){
            return;
        }
        // if the person should sleep
        if (this.shouldSleep()) {
            // make them go to bed
            goToBed();
        } else {
            // get out of bed, if you aren't out already
            getOutOfBed();

            // if the person should do sports
            if (this.shouldDoSports()) {
                // make them try to go do sports
                doSports();
            // else if the person should eat
            } else if (this.shouldEatFood()) {
                // make them try to go eat something
                goEat();
            // else
            } else {
                // 50% chance to try to handle a random event
                if (0 == Simulation.getRand().nextInt(2)) {
                    super.tenMinutesPassed();
                // else 50% chance to do a random activity
                } else {
                    doRandomActivity();
                }
            }
        }
    }

    /**
     * This method makes the person travel to a room with a CoffeeMachine and make coffee.
     */
    private void makeCoffee(){
        Room room = findRoomWithAppliance(CoffeeMachine.class);

        if (room != null) {
            travelToRoom(room);
            CoffeeMachine coffeeMachine = getRoom().getAppliance(CoffeeMachine.class);
            if (coffeeMachine.getCapacity() <= 0){
                coffeeMachine.refillCoffee();
            } else {
                coffeeMachine.makeCoffee();
            }
            coffeeMachine.setState(ApplianceState.ACTIVE);
            lastUsedAppliance = coffeeMachine;
            Simulation.log(new Log(this.getName() + " used a Coffee Machine.", LogType.ACTIVITY));
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working Coffee Machine.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, CoffeeMachine.class);
        }
    }

    /**
     * This method makes the person travel to a room with a Kettle and make tea.
     */
    private void makeTea(){
        Room room = findRoomWithAppliance(Kettle.class);

        if (room != null) {
            travelToRoom(room);
            Kettle kettle = getRoom().getAppliance(Kettle.class);
            if (kettle.getCapacity() <= 0){
                kettle.refillWater();
            } else {
                kettle.makeTea();
            }
            kettle.setState(ApplianceState.ACTIVE);
            lastUsedAppliance = kettle;
            Simulation.log(new Log(this.getName() + " used a Kettle.", LogType.ACTIVITY));
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working Kettle.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, Kettle.class);
        }
    }

    /**
     * This method makes the person travel to a room with a Printer and print documents.
     */
    private void printDocuments(int paperAmount){
        Room room = findRoomWithAppliance(Printer.class);

        if (room != null) {
            travelToRoom(room);
            Printer printer = getRoom().getAppliance(Printer.class);
            if (printer.getPaperCapacity() <= 0){
                printer.refillPaper();
            } else {
                printer.printDocuments(paperAmount);
            }
            printer.setState(ApplianceState.ACTIVE);
            lastUsedAppliance = printer;
            Simulation.log(new Log(this.getName() + " used a Printer.", LogType.ACTIVITY));
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working Printer.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, Printer.class);
        }
    }

    /**
     * This method makes the person travel to a room with a TV and watch TV.
     */
    private void watchTV(){
        Room room = findRoomWithAppliance(Television.class);

        if (room != null) {
            travelToRoom(room);
            Television tv = getRoom().getAppliance(Television.class);
            tv.setState(ApplianceState.ACTIVE);
            lastUsedAppliance = tv;
            Simulation.log(new Log(this.getName() + " watched a TV.", LogType.ACTIVITY));
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working TV.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, Television.class);
        }
    }

    /**
     * This method makes the person travel to a room with a Radio and listen to it.
     */
    private void listenToRadio(){
        Room room = findRoomWithAppliance(Radio.class);

        if (room != null) {
            travelToRoom(room);
            Radio radio = getRoom().getAppliance(Radio.class);
            radio.setState(ApplianceState.ACTIVE);
            lastUsedAppliance = radio;
            Simulation.log(new Log(this.getName() + " listened to a Radio.", LogType.ACTIVITY));
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working Radio.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, Radio.class);
        }
    }

    /**
     * This method makes the person travel to a room with a Microwave and use it.
     */
    private void microwaveFood(){
        Room room = findRoomWithAppliance(Microwave.class);

        if (room != null) {
            travelToRoom(room);
            Microwave microwave = getRoom().getAppliance(Microwave.class);
            microwave.setState(ApplianceState.ACTIVE);
            lastUsedAppliance = microwave;
            Simulation.log(new Log(this.getName() + " microwaved food.", LogType.ACTIVITY));
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working Microwave.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, Microwave.class);
        }
    }

    private void buyFood(Fridge fridge, FoodType foodType){
        List<String> humanFoods = Arrays.asList("Pizza", "Burger", "Pasta", "Salad", "Sushi", "Steak", "Ice Cream", "Pancakes");
        List<String> catFoods = Arrays.asList("Tuna", "Chicken", "Salmon");
        String foodToBuy = null;
        int calories = 0;
        if (foodType == FoodType.CAT_FOOD){
            foodToBuy = catFoods.get(Simulation.getRand().nextInt(catFoods.size()));
            //random caloric value of the food for cats
            calories = 100 + Simulation.getRand().nextInt(251);
        } else {
            foodToBuy = humanFoods.get(Simulation.getRand().nextInt(humanFoods.size()));
            calories = 500 + Simulation.getRand().nextInt(1500);
        }
        Food food = new Food(foodToBuy,calories,foodType);
        //price of the food is calculated as half of the caloric value of the food
        int foodCost = (int) (calories * 0.5);
        fridge.addFood(food);
        Simulation.increaseFoodCostsSinceLastBill(foodCost);
        Simulation.log(new Log(this.getName() + " bought food.", LogType.ACTIVITY));
    }

    private Food getFoodFromFridge(Fridge fridge, FoodType foodType) {
        Food toConsume = null;
        if (fridge != null){
            if (!fridge.getFoodList().contains(FoodType.CAT_FOOD)){buyFood(fridge,FoodType.CAT_FOOD);}
            if (!fridge.getFoodList().contains(FoodType.HUMAN_FOOD)){buyFood(fridge,FoodType.HUMAN_FOOD);}
            Iterator<Food> iterator = fridge.getFoodList().iterator();
            while (iterator.hasNext()) {
                Food food = iterator.next();
                if (food.getType() == foodType) {
                    toConsume = food;
                    iterator.remove();  // Safely removes the food from the list
                }
            }
            Simulation.log(new Log(this.getName() + " got food from the Fridge.", LogType.ACTIVITY));
        }
        return toConsume;
    }

    /**
     * This method returns a room containing at least one appliance of the chosen class that isn't in a broken state.
     * @param applianceClass the class of the appliance
     * @return the room with the appliance
     */
    private <T> Room findRoomWithAppliance(Class<T> applianceClass){
        List<Room> viableRooms = new ArrayList<>();
        for (Room room : House.getRooms()){
            for (InanimateObject object : room.getInanimateObjects()){
                if (applianceClass.isInstance(object)){
                    if (object.getClass() == applianceClass){
                        if (((Appliance) object).getState() != ApplianceState.BROKEN) {
                            viableRooms.add(room);
                        }
                    }
                }
            }
        }
        if (!viableRooms.isEmpty()) {
            return viableRooms.get(Simulation.getRand().nextInt(viableRooms.size()));
        } else {
            return null;
        }
    }

    /**
     * This method makes the person travel to a room with a Lavalamp and watch it.
     */
    private void watchLavaLamp(){
        Room room = findRoomWithAppliance(LavaLamp.class);
        if (room != null) {
            travelToRoom(room);
            LavaLamp lavaLamp = getRoom().getAppliance(LavaLamp.class);
            lavaLamp.setState(ApplianceState.ACTIVE);
            lastUsedAppliance = lavaLamp;
            Simulation.log(new Log(this.getName() + " watched a Lavalamp intensively ", LogType.ACTIVITY));
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a working Lavalamp.", LogType.ACTIVITY));
            super.handleRandomApplianceOfClassBrokeEvent(this, LavaLamp.class);
        }
    }

    /**
     * This method makes the person travel to a room with a window and look outside.
     */
    private void lookOutOfAWindow(){
        Room windowRoom = null;
        for (Room room : House.getRooms()){
            if (!room.getWindows().isEmpty()){
                windowRoom = room;
                break;
            }
        }
        if (windowRoom != null){
            travelToRoom(windowRoom);
            //open blinds on a window
            for (Window window : windowRoom.getWindows()){
                if (window.isWindowClosed()){
                    window.setWindowClosed(false);
                }
                if (window.isBlindsClosed()){
                    window.setBlindsClosed(false);
                    break;
                }
            }
        } else {
            Simulation.log(new Log(this.getName() + " failed to find a room with a window.", LogType.ACTIVITY));
        }
    }

    /**
     * This method moves the person to the room that leads outside (gateway has one side null)
     */
    public void travelRoomWithDoorToOutside(){
        for (Room room : House.getRooms()){
            for (Gateway gateway : room.getGateways()){
                if (gateway.getFirstRoom() == null || gateway.getSecondRoom() == null) {
                    if (getRoom() != null){
                        travelToRoom(room);
                        break;
                    } else {
                        passThroughGateway(gateway);
                    }
                }
            }
        }
    }

    /**
     * This method returns a sportsgear which is not borrowed
     * @return the unborrowed sportsgear
     */
    private SportsGear getUnborrowedSportsGear() {
        //go through each room and check if the room has an object with sportsgear class
        for (Room room : House.getRooms()) {
            for (InanimateObject inanimateObject : room.getInanimateObjects()) {
                if (inanimateObject instanceof SportsGear) {
                    SportsGear sportsGear = (SportsGear) inanimateObject;
                    if (!sportsGear.isBorrowed()) {
                        return sportsGear;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method determines what state does the person leave the door in after passing through
     * @param gateway the gateway to change the state for
     */
    @Override
    public void passThroughGateway(Gateway gateway) {
        super.passThroughGateway(gateway);
        if (gateway instanceof Door){
            int chance = Simulation.getRand().nextInt(100);
            Door door = (Door) gateway;
            if (chance < 10){
                door.lock();
            } else if (chance < 40){
                door.unlock();
            } else if (chance < 70){
                door.close();
            } else if (chance < 99){
                door.open();
            }
        }
    }

    public List<Role> getRoles() {
        return roles;
    }

    /**
     * This method retrieves a catfood from a fridge
     * @return the catfood from a fridge
     */
    public Food getCatFood() {
        Room fridgeRoom = findRoomWithAppliance(Fridge.class);
        travelToRoom(fridgeRoom);
        if (fridgeRoom != null){
            return getFoodFromFridge(fridgeRoom.getAppliance(Fridge.class),FoodType.CAT_FOOD);
        }
        return null;
    }

    /**
     * This method makes the person attempt to drive a car
     */
    private void driveACar(){
        for (Car car : House.getCars()){
            if (car.getState() != CarState.DRIVING){
                car.boardCar(this);
                break;
            }
        }
    }
}
