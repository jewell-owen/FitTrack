package com.example.fittrack.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Represents a logged workout, containing information such as the name, type,
 * muscle targeted, equipment used, difficulty level, instructions, weights, reps,
 * sets, and date of the workout.
 */
@IgnoreExtraProperties
public class LogWorkout {

    /** The field name for the workout name. */
    public static final String FIELD_NAME = "name";

    /** The field name for the workout type. */
    public static final String FIELD_TYPE = "type";

    /** The field name for the muscle targeted by the workout. */
    public static final String FIELD_MUSCLE = "muscle";

    /** The field name for the workout difficulty. */
    public static final String FIELD_DIFFICULTY = "difficulty";

    /** The field name for the workout instructions. */
    public static final String FIELD_INSTRUCTIONS = "instructions";

    /** The field name for the equipment used in the workout. */
    public static final String FIELD_EQUIPMENT = "equipment";

    /** The field name for the first weight used in the workout. */
    public static final String FIELD_ONE_WEIGHT = "oneWeight";

    /** The field name for the second weight used in the workout. */
    public static final String FIELD_TWO_WEIGHT = "twoWeight";

    /** The field name for the third weight used in the workout. */
    public static final String FIELD_THREE_WEIGHT = "threeWeight";

    /** The field name for the fourth weight used in the workout. */
    public static final String FIELD_FOUR_WEIGHT = "fourWeight";

    /** The field name for the fifth weight used in the workout. */
    public static final String FIELD_FIVE_WEIGHT = "fiveWeight";

    /** The field name for the sixth weight used in the workout. */
    public static final String FIELD_SIX_WEIGHT = "sixWeight";

    /** The field name for the first set of reps in the workout. */
    public static final String FIELD_ONE_REPS = "oneReps";

    /** The field name for the second set of reps in the workout. */
    public static final String FIELD_TWO_REPS = "twoReps";

    /** The field name for the third set of reps in the workout. */
    public static final String FIELD_THREE_REPS = "threeReps";

    /** The field name for the fourth set of reps in the workout. */
    public static final String FIELD_FOUR_REPS = "fourReps";

    /** The field name for the fifth set of reps in the workout. */
    public static final String FIELD_FIVE_REPS = "fiveReps";

    /** The field name for the sixth set of reps in the workout. */
    public static final String FIELD_SIX_REPS = "sixReps";

    /** The field name for the number of sets in the workout. */
    public static final String FIELD_SETS = "sets";

    /** The field name for the date of the workout. */
    public static final String FIELD_DATE = "date";

    /** The name of the workout. */
    private String name;

    /** The type of workout (e.g., strength, cardio). */
    private String type;

    /** The muscle group targeted by the workout. */
    private String muscle;

    /** The equipment used in the workout (e.g., dumbbells, resistance bands). */
    private String equipment;

    /** The difficulty level of the workout (e.g., easy, medium, hard). */
    private String difficulty;

    /** Instructions for performing the workout. */
    private String instructions;

    /** The weight used in the first set of the workout. */
    private String oneWeight;

    /** The weight used in the second set of the workout. */
    private String twoWeight;

    /** The weight used in the third set of the workout. */
    private String threeWeight;

    /** The weight used in the fourth set of the workout. */
    private String fourWeight;

    /** The weight used in the fifth set of the workout. */
    private String fiveWeight;

    /** The weight used in the sixth set of the workout. */
    private String sixWeight;

    /** The number of reps in the first set of the workout. */
    private String oneReps;

    /** The number of reps in the second set of the workout. */
    private String twoReps;

    /** The number of reps in the third set of the workout. */
    private String threeReps;

    /** The number of reps in the fourth set of the workout. */
    private String fourReps;

    /** The number of reps in the fifth set of the workout. */
    private String fiveReps;

    /** The number of reps in the sixth set of the workout. */
    private String sixReps;

    /** The number of sets performed during the workout. */
    private int sets = 1;

    /** The date the workout was logged. */
    private String date;

    /**
     * Default constructor for creating an empty LogWorkout object.
     */
    public LogWorkout() {}

    /**
     * Constructs a LogWorkout object with specified attributes.
     *
     * @param name The name of the workout.
     * @param type The type of workout (e.g., strength, cardio).
     * @param muscle The muscle group targeted by the workout.
     * @param equipment The equipment used in the workout.
     * @param difficulty The difficulty level of the workout.
     * @param instructions Instructions for performing the workout.
     * @param oneWeight The weight used in the first set of the workout.
     * @param twoWeight The weight used in the second set of the workout.
     * @param threeWeight The weight used in the third set of the workout.
     * @param fourWeight The weight used in the fourth set of the workout.
     * @param fiveWeight The weight used in the fifth set of the workout.
     * @param sixWeight The weight used in the sixth set of the workout.
     * @param oneReps The number of reps in the first set of the workout.
     * @param twoReps The number of reps in the second set of the workout.
     * @param threeReps The number of reps in the third set of the workout.
     * @param fourReps The number of reps in the fourth set of the workout.
     * @param fiveReps The number of reps in the fifth set of the workout.
     * @param sixReps The number of reps in the sixth set of the workout.
     * @param sets The number of sets performed in the workout.
     * @param date The date the workout was logged.
     */
    public LogWorkout(String name, String type, String muscle, String equipment, String difficulty, String instructions, String oneWeight, String twoWeight, String threeWeight, String fourWeight, String fiveWeight, String sixWeight, String oneReps, String twoReps, String threeReps, String fourReps, String fiveReps, String sixReps, int sets, String date) {
        this.name = name;
        this.type = type;
        this.muscle = muscle;
        this.equipment = equipment;
        this.difficulty = difficulty;
        this.instructions = instructions;
        this.oneWeight = oneWeight;
        this.twoWeight = twoWeight;
        this.threeWeight = threeWeight;
        this.fourWeight = fourWeight;
        this.fiveWeight = fiveWeight;
        this.sixWeight = sixWeight;
        this.oneReps = oneReps;
        this.twoReps = twoReps;
        this.threeReps = threeReps;
        this.fourReps = fourReps;
        this.fiveReps = fiveReps;
        this.sixReps = sixReps;
        this.sets = sets;
        this.date = date;
    }

    /**
     * Gets the name of the workout.
     *
     * @return The workout name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workout.
     *
     * @param name The workout name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the workout.
     *
     * @return The workout type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the workout.
     *
     * @param type The workout type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the muscle group targeted by the workout.
     *
     * @return The muscle group.
     */
    public String getMuscle() {
        return muscle;
    }

    /**
     * Sets the muscle group targeted by the workout.
     *
     * @param muscle The muscle group.
     */
    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    /**
     * Gets the equipment used in the workout.
     *
     * @return The equipment.
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment used in the workout.
     *
     * @param equipment The equipment.
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets the difficulty level of the workout.
     *
     * @return The difficulty level.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the workout.
     *
     * @param difficulty The difficulty level.
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the instructions for the workout.
     *
     * @return The instructions.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the instructions for the workout.
     *
     * @param instructions The instructions.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Gets the weight used in the first set of the workout.
     *
     * @return The weight for the first set.
     */
    public String getOneWeight() {
        return oneWeight;
    }

    /**
     * Sets the weight used in the first set of the workout.
     *
     * @param oneWeight The weight for the first set.
     */
    public void setOneWeight(String oneWeight) {
        this.oneWeight = oneWeight;
    }

    /**
     * Gets the weight used in the second set of the workout.
     *
     * @return The weight for the second set.
     */
    public String getTwoWeight() {
        return twoWeight;
    }

    /**
     * Sets the weight used in the second set of the workout.
     *
     * @param twoWeight The weight for the second set.
     */
    public void setTwoWeight(String twoWeight) {
        this.twoWeight = twoWeight;
    }

    /**
     * Gets the weight used in the third set of the workout.
     *
     * @return The weight for the third set.
     */
    public String getThreeWeight() {
        return threeWeight;
    }

    /**
     * Sets the weight used in the third set of the workout.
     *
     * @param threeWeight The weight for the third set.
     */
    public void setThreeWeight(String threeWeight) {
        this.threeWeight = threeWeight;
    }

    /**
     * Gets the weight used in the fourth set of the workout.
     *
     * @return The weight for the fourth set.
     */
    public String getFourWeight() {
        return fourWeight;
    }

    /**
     * Sets the weight used in the fourth set of the workout.
     *
     * @param fourWeight The weight for the fourth set.
     */
    public void setFourWeight(String fourWeight) {
        this.fourWeight = fourWeight;
    }

    /**
     * Gets the weight used in the fifth set of the workout.
     *
     * @return The weight for the fifth set.
     */
    public String getFiveWeight() {
        return fiveWeight;
    }

    /**
     * Sets the weight used in the fifth set of the workout.
     *
     * @param fiveWeight The weight for the fifth set.
     */
    public void setFiveWeight(String fiveWeight) {
        this.fiveWeight = fiveWeight;
    }

    /**
     * Gets the weight used in the sixth set of the workout.
     *
     * @return The weight for the sixth set.
     */
    public String getSixWeight() {
        return sixWeight;
    }

    /**
     * Sets the weight used in the sixth set of the workout.
     *
     * @param sixWeight The weight for the sixth set.
     */
    public void setSixWeight(String sixWeight) {
        this.sixWeight = sixWeight;
    }

    /**
     * Gets the number of reps in the first set of the workout.
     *
     * @return The reps for the first set.
     */
    public String getOneReps() {
        return oneReps;
    }

    /**
     * Sets the number of reps in the first set of the workout.
     *
     * @param oneReps The reps for the first set.
     */
    public void setOneReps(String oneReps) {
        this.oneReps = oneReps;
    }

    /**
     * Gets the number of reps in the second set of the workout.
     *
     * @return The reps for the second set.
     */
    public String getTwoReps() {
        return twoReps;
    }

    /**
     * Sets the number of reps in the second set of the workout.
     *
     * @param twoReps The reps for the second set.
     */
    public void setTwoReps(String twoReps) {
        this.twoReps = twoReps;
    }

    /**
     * Gets the number of reps in the third set of the workout.
     *
     * @return The reps for the third set.
     */
    public String getThreeReps() {
        return threeReps;
    }

    /**
     * Sets the number of reps in the third set of the workout.
     *
     * @param threeReps The reps for the third set.
     */
    public void setThreeReps(String threeReps) {
        this.threeReps = threeReps;
    }

    /**
     * Gets the number of reps in the fourth set of the workout.
     *
     * @return The reps for the fourth set.
     */
    public String getFourReps() {
        return fourReps;
    }

    /**
     * Sets the number of reps in the fourth set of the workout.
     *
     * @param fourReps The reps for the fourth set.
     */
    public void setFourReps(String fourReps) {
        this.fourReps = fourReps;
    }

    /**
     * Gets the number of reps in the fifth set of the workout.
     *
     * @return The reps for the fifth set.
     */
    public String getFiveReps() {
        return fiveReps;
    }

    /**
     * Sets the number of reps in the fifth set of the workout.
     *
     * @param fiveReps The reps for the fifth set.
     */
    public void setFiveReps(String fiveReps) {
        this.fiveReps = fiveReps;
    }

    /**
     * Gets the number of reps in the sixth set of the workout.
     *
     * @return The reps for the sixth set.
     */
    public String getSixReps() {
        return sixReps;
    }

    /**
     * Sets the number of reps in the sixth set of the workout.
     *
     * @param sixReps The reps for the sixth set.
     */
    public void setSixReps(String sixReps) {
        this.sixReps = sixReps;
    }

    /**
     * Gets the number of sets performed in the workout.
     *
     * @return The number of sets.
     */
    public int getSets() {
        return sets;
    }

    /**
     * Sets the number of sets performed in the workout.
     *
     * @param sets The number of sets.
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     * Gets the date the workout was logged.
     *
     * @return The workout date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the workout was logged.
     *
     * @param date The workout date.
     */
    public void setDate(String date) {
        this.date = date;
    }
}
