package com.example.fittrack.model;

public class FoodItem {
    private String foodName;

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    private double calories;



    private String meal;// Example attribute, adjust based on the API response.


    public FoodItem() {}

    public FoodItem(String foodName, double calories, String meal) {
        this.foodName = foodName;
        this.calories = calories;
        this.meal = meal;
    }

    // Getters and setters
    public String getFoodName() {
        return foodName;
    }


    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public double getCalories() {
        return calories;
    }
}

