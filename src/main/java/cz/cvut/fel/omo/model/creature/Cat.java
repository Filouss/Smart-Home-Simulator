package cz.cvut.fel.omo.model.creature;

import cz.cvut.fel.omo.model.house.House;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.house.food.Food;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.TimeTracker;
import cz.cvut.fel.omo.model.simulation.event.Event;
import cz.cvut.fel.omo.model.simulation.event.Events;
import cz.cvut.fel.omo.model.simulation.event.EventType;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a cat (the animal).
 */
public class Cat extends Creature {
    /**
     * This method makes the cat ask for food.
     */
    public void askForFood() {
        Events.addEvent(new Event(EventType.CAT_ASKED_FOR_FOOD, List.of(this)));
        Simulation.log(new Log((this.getName() + " created an event to be fed") , LogType.EVENT));
        this.setState(CreatureState.WAITING_TO_BE_FED);
        Simulation.log(new Log(this.getName() + " asked for food.", LogType.ACTIVITY));
    }

    /**
     * This constructor constructs a new Cat object.
     * @param name the name of the cat
     * @param room the room the cat is in
     */
    public Cat(String name, Room room) {
        super(name, 350, room);
    }

    @Override
    public void tenMinutesPassed() {
        // decrease the creature's calories
        this.decreaseCaloriesByTenMinutes();

        // if the cat should be sleeping
        if (shouldSleep()){
            // and isn't sleeping already
            if (getState() != CreatureState.SLEEPING){
                // make it sleep and log the activity
                this.setState(CreatureState.SLEEPING);
                Simulation.log(new Log(this.getName() + " went to bed.", LogType.ACTIVITY));
            }
        // else if not waiting to be fed
        } else if (getState() != CreatureState.WAITING_TO_BE_FED) {
            // if the cat just got out of bed
            if (getState() == CreatureState.SLEEPING) {
                // log it
                Simulation.log(new Log(this.getName() + " got out of bed.", LogType.ACTIVITY));
            }
            // go idle
            this.setState(CreatureState.IDLE);
            // travel to a random room
            int roomOrder = Simulation.getRand().nextInt(House.getRooms().size());
            travelToRoom(House.getRooms().get(roomOrder));
            // log travel
            Simulation.log(new Log(this.getName() + " travelled to a random room.", LogType.ACTIVITY));
            // if hungry
            if (this.shouldEatFood()) {
                // ask for food
                this.askForFood();
                // log activity
                Simulation.log(new Log(this.getName() + " asked for food.", LogType.ACTIVITY));
            // else (if not hungry)
            } else {
                // try to handle an event
                super.tenMinutesPassed();
            }
        // else (if waiting for food)
        } else {
            // log activity
            Simulation.log(new Log(this.getName() + " is waiting for food.", LogType.ACTIVITY));
        }
    }

    /**
     * This method returns true if the cat should be sleeping right now.
     * @return whether the cat should be sleeping right now
     */
    public boolean shouldSleep() {
        return TimeTracker.getHoursPassedThisDay() < 8 || TimeTracker.getHoursPassedThisDay() > 16;
    }

    /**
     * This method makes the cat consume food that was brought by a person
     * @param person the person that feeds the cat
     * @param catFood the food for cat to consume
     */
    public void beFed(Person person,Food catFood){
        person.travelToRoom(this.getRoom());
        if (person.getRoom() == this.getRoom() && catFood != null) {
            consumeFood(catFood);
            setState(CreatureState.IDLE);
            setCurrentCalories(catFood.getCalories());
            Simulation.log(new Log(person.getName() + " fed " + this.getName(), LogType.ACTIVITY));
        } else {
            Simulation.log(new Log("Something is trying to feed this cat without being in the same room, it must travel there first.", LogType.ERROR));
        }
    }
}
