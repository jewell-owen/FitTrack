package com.example.fittrack.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class LogWorkout {

    public static final String FIELD_NAME= "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MUSCLE = "muscle";
    public static final String FIELD_DIFFICULTY = "difficulty";
    public static final String FIELD_INSTRUCTIONS = "instructions";
    public static final String FIELD_EQUIPMENT = "equipment";
    public static final String FIELD_ONE_WEIGHT = "oneWeight";
    public static final String FIELD_TWO_WEIGHT = "twoWeight";
    public static final String FIELD_THREE_WEIGHT = "threeWeight";
    public static final String FIELD_FOUR_WEIGHT = "fourWeight";
    public static final String FIELD_FIVE_WEIGHT = "fiveWeight";
    public static final String FIELD_SIX_WEIGHT = "sixWeight";
    public static final String FIELD_ONE_REPS = "oneReps";
    public static final String FIELD_TWO_REPS = "twoReps";
    public static final String FIELD_THREE_REPS = "threeReps";
    public static final String FIELD_FOUR_REPS = "fourReps";
    public static final String FIELD_FIVE_REPS = "fiveReps";
    public static final String FIELD_SIX_REPS = "sixReps";
    public static final String FIELD_SETS = "sets";
    public static final String FIELD_DATE = "date";


    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;
    private String oneWeight;
    private String twoWeight;
    private String threeWeight;
    private String fourWeight;
    private String fiveWeight;
    private String sixWeight;
    private String oneReps;
    private String twoReps;
    private String threeReps;
    private String fourReps;
    private String fiveReps;
    private String sixReps;
    private int sets = 1;
    private String date;



    public LogWorkout() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {return type;}

    public void setType(String type) { this.type = type; }

    public String getMuscle() {return muscle;}

    public void setMuscle(String muscle) { this.muscle = muscle; }

    public String getEquipment() {return equipment;}

    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getDifficulty () { return difficulty; }

    public void setDifficulty (String difficulty) {this.difficulty = difficulty; }

    public String getInstructions () { return instructions; }

    public void setInstructions (String instructions) { this.instructions = instructions; }

    public String getOneWeight () { return oneWeight; }

    public void setOneWeight (String oneWeight) { this.oneWeight = oneWeight; }

    public String getTwoWeight () { return twoWeight; }

    public void setTwoWeight (String twoWeight) { this.twoWeight = twoWeight; }

    public String getThreeWeight () { return threeWeight; }

    public void setThreeWeight (String threeWeight) { this.threeWeight = threeWeight; }

    public String getFourWeight () { return fourWeight; }

    public void setFourWeight (String fourWeight) { this.fourWeight = fourWeight; }

    public String getFiveWeight () { return fiveWeight; }

    public void setFiveWeight (String fiveWeight) { this.fiveWeight = fiveWeight; }

    public String getSixWeight() {return sixWeight;}

    public void setSixWeight(String sixWeight) {this.sixWeight = sixWeight;}

    public String getOneReps () { return oneReps; }

    public void setOneReps (String oneReps) { this.oneReps = oneReps; }

    public String getTwoReps () { return twoReps; }

    public void setTwoReps (String twoReps) { this.twoReps = twoReps; }

    public String getThreeReps () { return threeReps; }

    public void setThreeReps (String threeReps) { this.threeReps = threeReps; }

    public String getFourReps () { return fourReps; }

    public void setFourReps (String fourReps) { this.fourReps = fourReps; }

    public String getFiveReps () { return fiveReps; }

    public void setFiveReps (String fiveReps) { this.fiveReps = fiveReps; }

    public String getSixReps () { return sixReps; }

    public void setSixReps (String sixReps) { this.sixReps = sixReps; }

    public int getSets () { return sets; }

    public void setSets (int sets) { this.sets = sets; }

    public String getDate () { return date; }

    public void setDate (String date) { this.date = date; }

}
