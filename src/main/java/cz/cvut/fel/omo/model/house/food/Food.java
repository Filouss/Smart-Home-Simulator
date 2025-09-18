package cz.cvut.fel.omo.model.house.food;

/**
 * This class represents food.
 */
public class Food {
    private String name; // the name of the food
    private int calories; // the total calories in the food
    private FoodType type; // the food's type (e.g. CAT_FOOD, HUMAN_FOOD)

    /**
     * This constructor constructs a new Food object using the specified paramaters.
     * @param name the name of the food
     * @param calories the total calories in the food
     * @param type the food's type (e.g. CAT_FOOD, HUMAN_FOOD)
     */
    public Food(String name,int calories, FoodType type) {
        this.name = name;
        this.calories = calories;
        this.type = type;
    }

    /**
     * This method returns the food's name.
     * @return the food's name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the food's calories.
     * @return the food's calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * This method returns the food's type.
     * @return the food's type
     */
    public FoodType getType() {
        return type;
    }
}
