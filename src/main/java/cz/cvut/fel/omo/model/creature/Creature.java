package cz.cvut.fel.omo.model.creature;


import cz.cvut.fel.omo.model.house.House;
import cz.cvut.fel.omo.model.house.gateway.door.Door;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.event.BaseDoesStuffEveryTenMinutes;
import cz.cvut.fel.omo.model.house.food.Food;
import cz.cvut.fel.omo.model.house.gateway.Gateway;
import cz.cvut.fel.omo.model.house.Room;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.*;

/**
 * This class represents a creature that can move around the house.
 */
public class Creature extends BaseDoesStuffEveryTenMinutes {
    private String name; // the creature's name
    private int dailyCalories; // the creature's ideal daily calorie intake
    private int currentCalories; // the creature's current calories
    private Room room; // the room the creature is in
    private CreatureState state; // the state the creature is in

    /**
     * This constructor constructs a new Creature object.
     * @param name the name of the creature
     * @param dailyCalories the creature's ideal daily calorie intake
     * @param room the room the creature is in
     */
    public Creature(String name, int dailyCalories, Room room) {
        this.name = name;
        this.dailyCalories = dailyCalories;
        this.currentCalories = dailyCalories;
        this.room = room;
        this.state = CreatureState.IDLE;
    }

    /**
     * This method makes the creature travel to the target room.
     */
    public void travelToRoom(Room destinationRoom) {
        // if the room is null the person was outside and should travel back inside
        if (this.room == null && this instanceof Person person){
            person.travelRoomWithDoorToOutside();
        }

        //if the room is already the destination room, return
        if (this.room.equals(destinationRoom)) {
            return;
        }
        //else iterate through gateways and try to find te correct room
        Queue<Room> queue = new LinkedList<>();
        Map<Room, Room> cameFrom = new HashMap<>(); // Tracks the path
        Set<Room> visited = new HashSet<>();
        queue.add(this.room);
        visited.add(this.room);

        while (!queue.isEmpty()) {
            Room currentRoom = queue.poll();

            // If we find the target room, reconstruct the path
            if ((currentRoom == null && destinationRoom == null) || (currentRoom != null && currentRoom.equals(destinationRoom))) {
                reconstructPath(cameFrom, destinationRoom);
                return;
            }

            // if the current room isn't null (outside)
            if (currentRoom != null) {
                // Explore neighbors through gateways
                for (Gateway gateway : currentRoom.getGateways()) {
                    Room neighbor = gateway.getOtherRoom(currentRoom);
                    //check if the gateway is door which can be in unpassable states
                    if (gateway instanceof Door door){
                        //check if the creature can pass the gateway before adding to path
                        if (door.canCreaturePass(this)) {
                            if (!visited.contains(neighbor)) {
                                visited.add(neighbor);
                                cameFrom.put(neighbor, currentRoom);
                                queue.add(neighbor);
                            }
                        }
                    } else {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            cameFrom.put(neighbor, currentRoom);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
    }

    private void reconstructPath(Map<Room, Room> cameFrom, Room targetRoom) {
        List<Room> path = new ArrayList<>();
        Room current = targetRoom;

        while (current != null) {
            path.add(0, current);
            current = cameFrom.get(current);
        }

        for (int i = 0; i < path.size() - 1; i++) {
            //go through each room in path and check for correct gateway
            for(Gateway gateway : path.get(i).getGateways()) {
                if (gateway.getOtherRoom(path.get(i)) == path.get(i+1)){
                    passThroughGateway(gateway);
                }
            }
        }
    }

    /**
     * This method makes the creature pass through a gateway.
     * @param gateway the gateway the creature should pass through
     */
    public void passThroughGateway(Gateway gateway) {
        // validate that the creature is on one side of the gateway
        if (gateway.getFirstRoom() != this.getRoom() && gateway.getSecondRoom() != this.getRoom()) {
            Simulation.log(new Log("This creature is trying to pass through a gateway it's on neither side of: " + this.getName(), LogType.ERROR));
        }

        // validate that the creature can pass through the gateway
        if (!gateway.canCreaturePass(this)){return;}

        // get the room on the other side of the gateway
        Room roomOnTheOtherSide = gateway.getFirstRoom();
        if (roomOnTheOtherSide == this.getRoom()) {
            roomOnTheOtherSide = gateway.getSecondRoom();
        }

        // remove the creature from its current room
        if (this.getRoom() != null) {
            this.getRoom().removeCreature(this);
        }

        // add the creature to its new room
        this.room = roomOnTheOtherSide;
        if (roomOnTheOtherSide != null) {
            roomOnTheOtherSide.addCreature(this);
        }
    }

    /**
     * This method returns the creature's ideal daily calories.
     * @return the creature's daily calories
     */
    public int getDailyCalories() {
        return dailyCalories;
    }

    /**
     * This method returns the creature's current calories.
     * @return the creature's current calories
     */
    public int getCurrentCalories() {
        return currentCalories;
    }

    /**
     * This method decreases the creature's calories by 10 minutes.
     */
    public void decreaseCaloriesByTenMinutes() {
        this.currentCalories -= (int) (((float) dailyCalories)/(24*6));
        if (this.currentCalories < 0) {
            this.currentCalories = 0;
        }
    }

    /**
     * This method makes the creature consume the specified food.
     * @param food the food to consume
     */
    public void consumeFood(Food food) {
        this.currentCalories += food.getCalories();
        Simulation.log(new Log(this.getName() + " consumed " + food.getName() + ".", LogType.ACTIVITY));
    }

    /**
     * This method returns the creature's name.
     * @return the creature's name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the room the creature is currently in.
     * @return the room the creature is in
     */
    public Room getRoom() {
        return room;
    }

    /**
     * This method checks if the creature is outside the house.
     * @return true if the creature is outside the house
     */
    public boolean isOutside() {
        return this.room == null;
    }

    /**
     * The method checks if the creature is inside the house.
     * @return true if the creature is inside the house
     */
    public boolean isInside() {
        return !isOutside();
    }

    /**
     * This method determines whether the creature should eat food.
     * @return whether the creature should eat food
     */
    public boolean shouldEatFood() {
        return this.currentCalories <= this.getDailyCalories()*0.5;
    }

    /**
     * This method returns the creature's state (e.g. IDLE, SLEEPING)
     * @return the creature's state
     */
    public CreatureState getState() {
        return state;
    }

    /**
     * This method sets the creature's state.
     * @param state the state to set to (the creature must be able to handle it in its tenMinutesPassed method)
     */
    public void setState(CreatureState state) {
        this.state = state;
    }

    /**
     * This method sets the creature's current calorie amount.
     * @param currentCalories the amount of calories to set
     */
    public void setCurrentCalories(int currentCalories) {
        this.currentCalories = currentCalories;
    }
}