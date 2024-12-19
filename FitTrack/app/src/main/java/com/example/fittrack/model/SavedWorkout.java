package com.example.fittrack.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Class to represent a saved workout.
 * This class contains the name of the workout and a list of exercises included in the workout.
 * The workout data is used in the app to track and manage saved workout routines.
 */
@IgnoreExtraProperties
public class SavedWorkout {

    /**
     * Field name for workout name in the database.
     */
    public static final String FIELD_NAME = "name";

    /**
     * Field name for workout type in the database.
     */
    public static final String FIELD_TYPE = "type";

    /**
     * Field name for muscle group targeted in the workout.
     */
    public static final String FIELD_MUSCLE = "muscle";

    /**
     * Field name for workout difficulty in the database.
     */
    public static final String FIELD_DIFFICULTY = "difficulty";

    /**
     * Field name for workout instructions in the database.
     */
    public static final String FIELD_INSTRUCTIONS = "instructions";

    /**
     * Name of the saved workout.
     */
    private String name;

    /**
     * List of exercises included in the saved workout.
     */
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();

    /**
     * Default constructor for the SavedWorkout class.
     * This constructor is required for Firebase deserialization.
     */
    public SavedWorkout() {}

    /**
     * Constructor to create a SavedWorkout object with specified name and list of exercises.
     *
     * @param name The name of the saved workout.
     * @param exercises A list of exercises included in the workout.
     */
    public SavedWorkout(String name, ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    /**
     * Gets the name of the saved workout.
     *
     * @return The name of the workout.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the saved workout.
     *
     * @param name The name of the workout.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of exercises included in the saved workout.
     *
     * @return A list of exercises in the workout.
     */
    public ArrayList<Exercise> getType() {
        return exercises;
    }

    /**
     * Sets the list of exercises included in the saved workout.
     *
     * @param exercises A list of exercises to include in the workout.
     */
    public void setType(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
