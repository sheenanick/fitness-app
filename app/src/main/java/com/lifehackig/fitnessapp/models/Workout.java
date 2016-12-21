package com.lifehackig.fitnessapp.models;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    private String name;
    private String pushId;
    private List<Exercise> exercises = new ArrayList<>();

    public Workout(){}

    public Workout(String name, String pushId) {
        this.name = name;
        this.pushId = pushId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
