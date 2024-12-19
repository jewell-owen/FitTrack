package com.example.fittrack.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Represents an exercise with its various attributes such as name, type, muscle group, equipment,
 * difficulty level, and instructions.
 *
 * <p>This class is used for storing and retrieving exercise data from Firestore. It includes
 * fields for the exercise details, as well as getter and setter methods for each field.</p>
 */
@IgnoreExtraProperties
public class Exercise {

    /**
     * Firestore field name constants for query purposes.
     */
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MUSCLE = "muscle";
    public static final String FIELD_DIFFICULTY = "difficulty";
    public static final String FIELD_INSTRUCTIONS = "instructions";
    public static final String FIELD_EQUIPMENT = "equipment";

    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;

    /**
     * Default no-argument constructor required for Firestore deserialization.
     */
    public Exercise() {}


    /**
     * Constructs an Exercise object with the specified attributes.
     *
     * @param name         The name of the exercise.
     * @param type         The type of exercise (e.g., strength, cardio).
     * @param muscle       The targeted muscle group (e.g., chest, legs).
     * @param equipment    The equipment required for the exercise.
     * @param difficulty   The difficulty level of the exercise (e.g., beginner, intermediate).
     * @param instructions The instructions for performing the exercise.
     */
    public Exercise(String name, String type, String muscle, String equipment, String difficulty, String instructions) {
        this.name = name;
        this.type = type;
        this.muscle = muscle;
        this.equipment = equipment;
        this.difficulty = difficulty;
        this.instructions = instructions;
    }

    /**
     * Gets the name of the exercise.
     *
     * @return The name of the exercise.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the exercise.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the exercise.
     *
     * @return The type of the exercise.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the exercise.
     *
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the primary muscle targeted by the exercise.
     *
     * @return The primary muscle targeted by the exercise.
     */
    public String getMuscle() {
        return muscle;
    }

    /**
     * Sets the primary muscle targeted by the exercise.
     *
     * @param muscle The muscle to set.
     */
    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    /**
     * Gets the equipment needed for the exercise.
     *
     * @return The equipment needed for the exercise.
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment needed for the exercise.
     *
     * @param equipment The equipment to set.
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets the difficulty level of the exercise.
     *
     * @return The difficulty level of the exercise.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the exercise.
     *
     * @param difficulty The difficulty level to set.
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the step-by-step instructions for performing the exercise.
     *
     * @return The instructions for the exercise.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the step-by-step instructions for performing the exercise.
     *
     * @param instructions The instructions to set.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}