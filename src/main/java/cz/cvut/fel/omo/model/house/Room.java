package cz.cvut.fel.omo.model.house;

import cz.cvut.fel.omo.model.creature.Creature;
import cz.cvut.fel.omo.model.house.appliance.*;
import cz.cvut.fel.omo.model.house.gateway.Gateway;
import cz.cvut.fel.omo.model.simulation.event.BaseDoesStuffEveryTenMinutes;
import cz.cvut.fel.omo.model.simulation.weather.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a room in the house, but can also represent be a hallway.
 */
public class Room extends BaseDoesStuffEveryTenMinutes {
    private List<InanimateObject> inanimateObjects; // the inanimate objects belonging to this room
    private List<Gateway> gateways; // the gateways leading in or out of the room
    private List<Window> windows; // the windows in the room
    private List<Creature> creatures; // the creatures in the room
    private Floor floor; // the floor the room is on
    private double temperature = 20; // the room's temperature
    private int humidity = 25; // the room's humidity in percentage points

    public Room(Floor floor) {
        this.floor = floor;
        this.gateways = new ArrayList<>();
        this.windows = new ArrayList<>();
        this.inanimateObjects = new ArrayList<>();
        this.creatures = new ArrayList<>();
    }

    /**
     * This method returns the inanimate objects belonging to this room.
     * @return the inanimate objects belonging to this room
     */
    public List<InanimateObject> getInanimateObjects() {
        return inanimateObjects;
    }

    /**
     * This method adds an inanimate object to the room
     * @param inanimateObject the inanimate object to add
     */
    public void addInanimateObject(InanimateObject inanimateObject) {
        this.inanimateObjects.add(inanimateObject);
    }

    /**
     * This method returns the gateways leading in or out of the room.
     * @return the gateways in the room
     */
    public List<Gateway> getGateways() {
        return gateways;
    }

    /**
     * This method adds a gateway to the room.
     * @param gateway the gateway to add
     */
    public void addGateway(Gateway gateway) {
        this.gateways.add(gateway);
    }

    /**
     * This method returns the room's floor.
     * @return the room's floor
     */
    public Floor getFloor() {
        return floor;
    }

    /**
     * This method returns the windows in the room.
     * @return the room's windows
     */
    public List<Window> getWindows() {
        return windows;
    }

    /**
     * This method adds a window to the room.
     * @param window the window to add
     */
    public void addWindow(Window window) {
        if (this.windows == null) {
            throw new NullPointerException("The room was not setup correctly.");
        }
        this.windows.add(window);
    }

    /**
     * This method returns a list of creatures in the room.
     * @return the creatures in the room
     */
    public List<Creature> getCreatures() {
        return creatures;
    }

    /**
     * This method adds a creature to the room.
     * @param creature the creature to add
     */
    public void addCreature(Creature creature) {
        this.creatures.add(creature);
    }

    /**
     * This method removes a creature from the room.
     * @param creature the creature to remove
     */
    public void removeCreature(Creature creature) {
        this.creatures.remove(creature);
    }

    /**
     * This method returns an appliance of the chosen class that isn't broken.
     * @param applianceClass the appliance class
     * @return the appliance object
     * @param <T>
     */
    public <T extends InanimateObject> T getAppliance(Class<T> applianceClass) {
        for (InanimateObject inanimateObject : inanimateObjects) {
            if (applianceClass.isInstance(inanimateObject) && ((Appliance) inanimateObject).getState() != ApplianceState.BROKEN) {
                return applianceClass.cast(inanimateObject);
            }
        }
        return null;
    }

    /**
     * Set all lights in the room to idle.
     */
    public void setAllLightsToIdle() {
        // loop through all inanimate objects
        for (InanimateObject inanimateObject : inanimateObjects) {
            // if the inanimate object is a light and not broken
            if (inanimateObject.getClass() == Light.class && ((Appliance) inanimateObject).getState() != ApplianceState.BROKEN) {
                // set the light's state to idle
                ((Light) inanimateObject).setState(ApplianceState.IDLE);
            }
        }
    }

    /**
     * Set all lights in the room to active.
     */
    public void setAllLightsToActive() {
        // loop through all inanimate objects
        for (InanimateObject inanimateObject : inanimateObjects) {
            // if the inanimate object is a light and not broken
            if (inanimateObject.getClass() == Light.class && ((Appliance) inanimateObject).getState() != ApplianceState.BROKEN) {
                // set the light's state to active
                ((Light) inanimateObject).setState(ApplianceState.ACTIVE);
            }
        }
    }

    /**
     * Set all heaters in the room to idle.
     */
    public void setAllHeatersToIdle() {
        // loop through all inanimate objects
        for (InanimateObject inanimateObject : inanimateObjects) {
            // if the inanimate object is a heater and not broken
            if (inanimateObject.getClass() == Heater.class && ((Appliance) inanimateObject).getState() != ApplianceState.BROKEN) {
                // set the heater's state to idle
                ((Heater) inanimateObject).setState(ApplianceState.IDLE);
            }
        }
    }

    /**
     * Set all heaters in the room to active.
     */
    public void setAllHeatersToActive() {
        // loop through all inanimate objects
        for (InanimateObject inanimateObject : inanimateObjects) {
            // if the inanimate object is a heater and not broken
            if (inanimateObject.getClass() == Heater.class && ((Appliance) inanimateObject).getState() != ApplianceState.BROKEN) {
                // set the light's state to active
                ((Heater) inanimateObject).setState(ApplianceState.ACTIVE);
            }
        }
    }

    /**
     * Set all dehumidifiers in the room to idle.
     */
    public void setAllDehumidifiersToIdle() {
        // loop through all inanimate objects
        for (InanimateObject inanimateObject : inanimateObjects) {
            // if the inanimate object is a dehumidifier and not broken
            if (inanimateObject.getClass() == Dehumidifier.class && ((Appliance) inanimateObject).getState() != ApplianceState.BROKEN) {
                // set the dehumidifier's state to idle
                ((Dehumidifier) inanimateObject).setState(ApplianceState.IDLE);
            }
        }
    }

    /**
     * Set all dehumidifiers in the room to active.
     */
    public void setAllDehumidifiersToActive() {
        // loop through all inanimate objects
        for (InanimateObject inanimateObject : inanimateObjects) {
            // if the inanimate object is a dehumidifier and not broken
            if (inanimateObject.getClass() == Dehumidifier.class && ((Appliance) inanimateObject).getState() != ApplianceState.BROKEN) {
                // set the dehumidifier's state to active
                ((Dehumidifier) inanimateObject).setState(ApplianceState.ACTIVE);
            }
        }
    }

    @Override
    public void tenMinutesPassed() {
        super.tenMinutesPassed();
        // change temperature and humidity based on weather
        if (this.temperature > Weather.getTemperature()) {
            this.temperature--;
        } else if (this.temperature < Weather.getTemperature()){
            this.temperature++;
        }
        if (this.humidity > Weather.getHumidity()) {
            this.humidity--;
        } else if (this.humidity < Weather.getHumidity()){
            this.humidity++;
        }
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}