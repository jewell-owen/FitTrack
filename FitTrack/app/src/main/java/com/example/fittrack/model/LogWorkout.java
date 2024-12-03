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

    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;


    public LogWorkout() {}

    public LogWorkout(String name, String type, String muscle, String equipment, String difficulty, String instructions) {
        this.name = name;
        this.type = type;
        this.muscle = muscle;
        this.equipment = equipment;
        this.difficulty = difficulty;
        this.instructions = instructions;
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
}
