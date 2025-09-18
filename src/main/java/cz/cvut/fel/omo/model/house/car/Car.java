package cz.cvut.fel.omo.model.house.car;

import cz.cvut.fel.omo.model.creature.CreatureState;
import cz.cvut.fel.omo.model.creature.Person;
import cz.cvut.fel.omo.model.house.gateway.Gateway;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.event.BaseDoesStuffEveryTenMinutes;
import cz.cvut.fel.omo.model.simulation.event.Event;
import cz.cvut.fel.omo.model.simulation.event.EventType;
import cz.cvut.fel.omo.model.simulation.event.Events;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represents a car.
 */
public class Car extends BaseDoesStuffEveryTenMinutes {
    private final int maxTankCapacity;
    private final int petrolUsage;
    private int tankCapacity;
    private Queue<Person> waitingQueue;
    private List<Person> currentUsers;
    private final int seatCount;
    private CarState state;
    private String identificator;
    private boolean askedForFuel = false;

    public Car(String identificator, int maxTankCapacity, int petrolUsage, int seatCount) {
        this.maxTankCapacity = maxTankCapacity;
        this.petrolUsage = petrolUsage;
        this.tankCapacity = maxTankCapacity;
        waitingQueue = new LinkedList<>();
        currentUsers = new ArrayList<Person>();
        this.seatCount = seatCount;
        state = CarState.IDLE;
        this.identificator = identificator;
    }

    /**
     * This method refuels the car and adds to the petrol bill
     */
    public void refuel(){
        int neededPetrol = maxTankCapacity - tankCapacity;
        tankCapacity = maxTankCapacity;
        state = CarState.IDLE;
        //price for petrol is 35 units per litre
        Simulation.increaseCarCostsSinceLastBill(neededPetrol*35);
        askedForFuel = false;
    }

    /**
     * This method changes the state of the car to IDLE and adds the people from queue to the car
     */
    public void finishDriving() {
        for (Person person : currentUsers) {
            person.travelRoomWithDoorToOutside();
            person.setState(CreatureState.IDLE);
            Simulation.log(new Log(person.getName() + " Finished driving a car.", LogType.ACTIVITY));
        }
        currentUsers.clear();
        if (state != CarState.OUT_OF_FUEL) {
            state = CarState.IDLE;
        // Move people from waiting queue to the car (if any)
        while (currentUsers.size() < seatCount && !waitingQueue.isEmpty()) {
            currentUsers.add(waitingQueue.poll());
        }
        }
    }

    /**
     * This method calls appropriate methods according to the state of the car
     */
    @Override
    public void tenMinutesPassed() {
        super.tenMinutesPassed();
        if (!currentUsers.isEmpty() && state == CarState.IDLE){
            startDriving();
        }
        if (state == CarState.DRIVING) {
            finishDriving();
        } else if (state == CarState.OUT_OF_FUEL && !askedForFuel) {
            Events.addEvent(new Event(EventType.CAR_OUT_OF_FUEL, List.of(this)));
            Simulation.log(new Log(("A car created an event to get refueled") , LogType.EVENT));
            askedForFuel = true;
        }
    }

    /**
     * This method makes the Person attempt to board the car.
     * @param person the person that will attempt to board the car.
     * @return true if the person has boarded the car or was put in the queue for driving in the next tick. False otherwise
     */
    public boolean boardCar(Person person) {
        if (currentUsers.size() < seatCount) {
            moveUsersOutside();
            // Successfully boarded
            currentUsers.add(person);
            person.setCar(this);
            person.setState(CreatureState.WAITING_FOR_CAR);
            return true;
        } else if (waitingQueue.size() < seatCount - currentUsers.size()) {
            // Successfully added to waiting queue
            waitingQueue.add(person);
            person.setState(CreatureState.WAITING_FOR_CAR);
            return true;
        }
        // Car is full
        return false;
    }

    /**
     * This method starts the driving process, decreasing the tak capacity
     */
    public void startDriving() {
        if ( state == CarState.OUT_OF_FUEL) {
            return;
        }
        tankCapacity -= petrolUsage;
        if (tankCapacity <= 0) {
            state = CarState.OUT_OF_FUEL;
            finishDriving();
            return;
        }
        for (Person person : currentUsers) {
            person.setState(CreatureState.DRIVING);
            Simulation.log(new Log(person.getName() + " Was driving a car.", LogType.ACTIVITY));
        }
        state = CarState.DRIVING;
    }

    private void moveUsersOutside() {
        for (Person p : currentUsers) {
            p.travelRoomWithDoorToOutside();
            for (Gateway gateway : p.getRoom().getGateways()) {
                if (gateway.getFirstRoom() == null || gateway.getSecondRoom() == null) {
                    p.passThroughGateway(gateway);
                    break;
                }
            }
        }
    }

    public List<Person> getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(List<Person> currentUsers) {
        this.currentUsers = currentUsers;
    }

    public CarState getState() {
        return state;
    }

    public String getIdentificator() {
        return identificator;
    }
}
