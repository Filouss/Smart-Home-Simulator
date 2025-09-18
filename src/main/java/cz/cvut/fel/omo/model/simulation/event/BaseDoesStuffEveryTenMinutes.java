package cz.cvut.fel.omo.model.simulation.event;

import cz.cvut.fel.omo.model.bill.Bill;
import cz.cvut.fel.omo.model.creature.Cat;
import cz.cvut.fel.omo.model.creature.Person;
import cz.cvut.fel.omo.model.house.appliance.*;
import cz.cvut.fel.omo.model.house.car.Car;
import cz.cvut.fel.omo.model.house.food.Food;
import cz.cvut.fel.omo.model.simulation.Simulation;
import cz.cvut.fel.omo.model.simulation.TimeTracker;
import cz.cvut.fel.omo.model.simulation.logger.Log;
import cz.cvut.fel.omo.model.simulation.logger.LogType;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the base implementation of DoesStuffEveryTenMinutes that other classes build off of.
 */
public class BaseDoesStuffEveryTenMinutes implements DoesStuffEveryTenMinutes {
    protected List<EventType> eventTypesItCanHandle = new ArrayList<EventType>(); // the event types the observer can handle

    /**
     * This method adds an event type to the eventTypesItCanHandle list.
     * @param eventType the event type to add
     */
    public void addEventTypeItCanHandle(EventType eventType) {
        eventTypesItCanHandle.add(eventType);
    }

    @Override
    public void tenMinutesPassed() {
        if (!eventTypesItCanHandle.isEmpty()) {
            for (Event e: Events.getEvents()) {
                if (eventTypesItCanHandle.contains(e.type())) {
                    switch (e.type()) {
                        case EventType.CAT_ASKED_FOR_FOOD:
                            //get the cat to feed
                            Cat cat = (Cat) e.objects().getFirst();
                            //get the person that will feed the cat
                            Person person = (Person) this;
                            // attempt to feed the cat
                            cat.beFed(person,person.getCatFood());
                            // if the cat doesn't need to be fed anymore
                            if (!cat.shouldEatFood()) {
                                // remove the event from the list
                                Simulation.log(new Log(((Person) this).getName() + " fed " + cat.getName(), LogType.EVENT));
                                Events.removeEvent(e);
                            } else {
                                Simulation.log(new Log(((Person) this).getName() + " fed " + cat.getName() + ", but they are still hungry.", LogType.EVENT));
                            }
                            break;
                        case EventType.DEHUMIDIFIER_IS_FULL:
                            // get the appliance to repair
                            Dehumidifier dehumidifier = (Dehumidifier) e.objects().getFirst();
                            // travel to the dehumifier's room
                            ((Person) this).travelToRoom(dehumidifier.getRoom());
                            // empty the dehumidifier
                            dehumidifier.emptyDehumidifierWater();
                            // log activity
                            Simulation.log(new Log(((Person) this).getName() + " emptied out a Dehumidifier.", LogType.ACTIVITY));
                            Simulation.log(new Log(((Person) this).getName() + " emptied out a Dehumidifier.", LogType.EVENT));
                            // remove the event from the list
                            Events.removeEvent(e);
                            break;
                        case EventType.APPLIANCE_BROKE:
                            handleApplianceBrokeEvent(e, (Person) this);
                            break;
                        case EventType.BILL_IS_WAITING_FOR_PAYMENT:
                            // pay the bill
                            ((Bill) e.objects().getFirst()).payBill();
                            // remove the event from the list
                            Events.removeEvent(e);
                            // log activity
                            Simulation.log(new Log(((Person) this).getName() + " paid a bill.", LogType.ACTIVITY));
                            break;
                        case EventType.CAR_OUT_OF_FUEL:
                            person = (Person) this;
                            person.travelRoomWithDoorToOutside();
                            Car car = (Car) e.objects().getFirst();
                            car.refuel();
                            person.travelRoomWithDoorToOutside();
                            // log
                            Simulation.log(new Log(((Person) this).getName() + " refuelled a car.", LogType.ACTIVITY));
                            Simulation.log(new Log(((Person) this).getName() + " refuelled a car.", LogType.EVENT));
                            // remove the event from the list
                            Events.removeEvent(e);
                            break;
                        default:
                            Simulation.log(new Log("Unimplemented EventType in BaseDoesStuffEveryTenMinutes.", LogType.ERROR));
                            break;
                    }
                    return;
                }
            }
        }
    }

    public <T> void handleRandomApplianceOfClassBrokeEvent(Person person, Class<T> applianceClass) {
        Event applianceEvent = null;
        if (!eventTypesItCanHandle.isEmpty()) {
            for (Event e : Events.getEvents()) {
                if (eventTypesItCanHandle.contains(e.type())) {
                    if (e.type() == EventType.APPLIANCE_BROKE && e.objects().getFirst().getClass() == applianceClass) {
                        applianceEvent = e;
                        break;
                    }
                }
            }
        }

        if (applianceEvent != null) {
            handleApplianceBrokeEvent(applianceEvent, person);
        }
    }

    public void handleApplianceBrokeEvent(Event e, Person person) {
        // get the appliance to repair
        Appliance appliance = (Appliance) e.objects().getFirst();
        // go to the appliance's room
        person.travelToRoom(appliance.getRoom());
        // get the documentation
        Documentation documentation = appliance.getDocumentation();
        // if the appliance is still under warranty
        if (TimeTracker.getMinutesPassed() <= documentation.getWarrantyExpirationTimeInMinutes()) {
            appliance.replace();
            // log activity
            Simulation.log(new Log(((Person) this).getName() + " replaced an appliance under warranty.", LogType.ACTIVITY));
            // remove the event from the list
            Events.removeEvent(e);
            // else, try to repair it
        } else {
            // try to repair the appliance
            appliance.repair();
            // if you succeeded
            if (appliance.getState() != ApplianceState.BROKEN) {
                // remove the event from the list
                Events.removeEvent(e);
                // log activity
                Simulation.log(new Log(((Person) this).getName() + " repaired an appliance.", LogType.ACTIVITY));
                Simulation.log(new Log(((Person) this).getName() + " repaired a " + appliance.getClass().getSimpleName(), LogType.EVENT));
            // else if you fail
            } else {
                // log activity
                Simulation.log(new Log(((Person) this).getName() + " failed to repair an appliance.", LogType.ACTIVITY));
                Simulation.log(new Log(((Person) this).getName() + " failed to repair a " + appliance.getClass().getSimpleName(), LogType.EVENT));

            }
        }
    }
}
