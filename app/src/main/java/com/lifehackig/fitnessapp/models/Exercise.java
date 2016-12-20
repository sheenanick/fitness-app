package com.lifehackig.fitnessapp.models;

public class Exercise {
    private String name;
    private Integer reps;
    private Integer duration;
    private Integer weight;
    private String muscle;
    private Integer calories;



    private String pushId;

    public Exercise() {}

    public Exercise(String name, Integer reps, Integer duration, Integer weight, String muscle, Integer calories, String pushId) {
        this.name = name;
        this.reps = reps;
        this.duration = duration;
        this.weight = weight;
        this.muscle = muscle;
        this.calories = calories;
        this.pushId = pushId;
    }

    public String getName() {
        return name;
    }

    public Integer getReps() {
        return reps;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getMuscle() {
        return muscle;
    }

    public Integer getCalories() {
        return calories;
    }

    public String getPushId() {
        return pushId;
    }
}
