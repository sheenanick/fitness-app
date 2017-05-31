package com.lifehackig.fitnessapp.ui.log_exercise;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.models.Exercise;
import com.lifehackig.fitnessapp.models.Workout;
import com.lifehackig.fitnessapp.util.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheena on 3/9/17.
 */

public class LogExercisePresenter implements LogExerciseContract.Presenter {
    private LogExerciseContract.MvpView mView;
    private FirebaseDatabase mDatabase;
    private String mCurrentUid;
    private DatabaseReference mDateRef;
    private DatabaseReference mSavedWorkoutsRef;
    private List<Workout> mWorkoutObjects;
    private int mTotalCalories;
    private ValueEventListener mWorkoutsListener;

    public LogExercisePresenter(LogExerciseContract.MvpView view) {
        mView = view;
        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUid = UserManager.getCurrentUser().getUid();
        mWorkoutObjects = new ArrayList<>();
    }

    @Override
    public void getWorkoutNames() {
        mSavedWorkoutsRef = mDatabase.getReference("workouts").child(mCurrentUid);
        mWorkoutsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> workoutNames = new ArrayList<>();
                if (dataSnapshot.getValue() == null) {
                    workoutNames.add("No saved workouts");
                } else {
                    for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {
                        Workout workout = workoutSnapshot.getValue(Workout.class);
                        mWorkoutObjects.add(workout);
                        workoutNames.add(workout.getName());
                    }
                }
                mView.setupWorkoutSpinner(workoutNames);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mSavedWorkoutsRef.addValueEventListener(mWorkoutsListener);
    }

    @Override
    public void addSavedWorkout(int position, String date) {
        Workout workout = mWorkoutObjects.get(position);
        List<Exercise> exercises = workout.getExercises();

        mDateRef = mDatabase.getReference("members").child(mCurrentUid).child("days").child(date);
        DatabaseReference exercisesRef = mDateRef.child("exercises");

        Integer workoutTotalCalories = 0;
        for (Exercise exercise :  exercises) {
            workoutTotalCalories += exercise.getCalories();
            DatabaseReference exerciseRef = exercisesRef.push();
            String pushId = exerciseRef.getKey();
            exercise.setPushId(pushId);
            exerciseRef.setValue(exercise);
        }

        mDateRef.child("date").setValue(date);
        updateCalories(workoutTotalCalories);

        mView.navigateToDayActivity();
    }

    private void updateCalories(final Integer exerciseCalories) {
        final DatabaseReference dateCaloriesRef = mDateRef.child("calories");
        dateCaloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mTotalCalories = 0;
                } else {
                    mTotalCalories = Integer.parseInt(dataSnapshot.getValue().toString());
                }
                int newTotalCalories = mTotalCalories + exerciseCalories;
                dateCaloriesRef.setValue(newTotalCalories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void detach() {
        mView = null;
        mDatabase = null;
        mDateRef = null;
        mWorkoutObjects = null;
        mSavedWorkoutsRef.removeEventListener(mWorkoutsListener);
    }
}
