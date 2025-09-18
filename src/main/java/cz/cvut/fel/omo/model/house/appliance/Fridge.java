package cz.cvut.fel.omo.model.house.appliance;

import cz.cvut.fel.omo.model.bill.ApplianceBill;
import cz.cvut.fel.omo.model.bill.FoodBill;
import cz.cvut.fel.omo.model.house.food.Food;
import cz.cvut.fel.omo.model.house.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a fridge.
 */
public class Fridge extends Appliance {
    private List<Food> foodList; // a list of food in the fridge

    /**
     * This constructor constructs a new Fridge object using the specified parameters.
     * @param room the room the fridge is in
     */
    public Fridge(Room room, double activeElectricityConsumptionPerTenMinutes, double activeGasConsumptionPerTenMinutes, double activeWaterConsumptionPerTenMinutes) {
        super(room,activeElectricityConsumptionPerTenMinutes,activeGasConsumptionPerTenMinutes,activeWaterConsumptionPerTenMinutes);
        this.foodList = new ArrayList<Food>();
        //fridge is always idle
        this.setState(ApplianceState.IDLE);
    }

    /**
     * This method returns a list of food in the fridge.
     * @return a list of food in the fridge
     */
    public List<Food> getFoodList() {
        return foodList;
    }

    /**
     * This method adds the specified food to the list.
     * @param food the food to add
     */
    public void addFood(Food food) {
        this.foodList.add(food);
    }

    /**
     * This method removes the specified food from the list.
     * @param food the food to remove
     */
    public void removeFood(Food food){foodList.remove(food);}
}
