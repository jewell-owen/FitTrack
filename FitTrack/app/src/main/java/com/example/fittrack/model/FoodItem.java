package com.example.fittrack.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

public class FoodItem {
    private String foodName;
    private String brandName;
    private int calories;     // Example attribute, adjust based on the API response.

    public FoodItem(String foodName, String brandName, int calories) {
        this.foodName = foodName;
        this.brandName = brandName;
        this.calories = calories;
    }

    // Getters and setters
    public String getFoodName() {
        return foodName;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getCalories() {
        return calories;
    }
}

