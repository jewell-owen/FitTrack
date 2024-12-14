package com.example.fittrack.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Class to represent a saved workout.
 */
@IgnoreExtraProperties
public class SavedWorkout {

    public static final String FIELD_NAME= "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MUSCLE = "muscle";
    public static final String FIELD_DIFFICULTY = "difficulty";
    public static final String FIELD_INSTRUCTIONS = "instructions";

    private String name;
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();


    public SavedWorkout() {}

    public SavedWorkout(String name, ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Exercise> getType() {return exercises;}

    public void setType(ArrayList<Exercise> exercises) { this.exercises = exercises; }


}