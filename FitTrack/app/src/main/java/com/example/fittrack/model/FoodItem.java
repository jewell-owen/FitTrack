package com.example.fittrack.model;

/**
 * Represents a food item with its name, calorie count, and associated meal.
 */
public class FoodItem {

    /** The name of the food item. */
    private String foodName;

    /** The calorie count of the food item. */
    private double calories;

    /** The meal associated with the food item (e.g., breakfast, lunch). */
    private String meal;

    /**
     * Default constructor for creating a food item.
     */
    public FoodItem() {}

    /**
     * Constructs a food item with specified name, calories, and meal.
     *
     * @param foodName The name of the food item.
     * @param calories The calorie count of the food item.
     * @param meal The meal type for the food item (e.g., breakfast, lunch).
     */
    public FoodItem(String foodName, double calories, String meal) {
        this.foodName = foodName;
        this.calories = calories;
        this.meal = meal;
    }

    /**
     * Gets the name of the food item.
     *
     * @return The food name.
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * Gets the meal associated with the food item.
     *
     * @return The meal type (e.g., breakfast, lunch).
     */
    public String getMeal() {
        return meal;
    }

    /**
     * Sets the meal type for the food item.
     *
     * @param meal The meal type (e.g., breakfast, lunch).
     */
    public void setMeal(String meal) {
        this.meal = meal;
    }

    /**
     * Gets the calorie count of the food item.
     *
     * @return The calorie count.
     */
    public double getCalories() {
        return calories;
    }

    /**
     * Sets the calorie count for the food item.
     *
     * @param calories The calorie count of the food item.
     */
    public void setCalories(double calories) {
        this.calories = calories;
    }

    /**
     * Sets the name of the food item.
     *
     * @param foodName The name of the food item.
     */
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}

