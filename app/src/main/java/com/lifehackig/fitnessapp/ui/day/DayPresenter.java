package com.lifehackig.fitnessapp.ui.day;

import android.view.View;

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
 * Created by Sheena on 3/6/17.
 */

public class DayPresenter implements DayContract.Presenter {
    private DayContract.MvpView mView;
    private String mCurrentUid;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mExercises;
    private DatabaseReference mCalories;

    public DayPresenter(DayContract.MvpView view, String dateRefId) {
        mView = view;

        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUid = UserManager.getCurrentUser().getUid();

        DatabaseReference dateRef = mDatabase.getReference("members").child(mCurrentUid).child("days").child(dateRefId);
        mExercises = dateRef.child("exercises");
        mCalories = dateRef.child("calories");
    }

    @Override
    public void initFirebaseAdapter() {
        mView.setupFirebaseAdapter(mExercises, mCalories);
    }

    @Override
    public void getExercises() {
        mExercises.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mView.noExercisesView();
                } else {
                    mView.hasExercisesView();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getCalories() {
        mCalories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    int totalCalories = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (mView != null) {
                        mView.setCaloriesTextView(totalCalories);
                        if (totalCalories == 0) {
                            mView.setSaveButtonVisibility(View.GONE);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveWorkout(String name) {
        final DatabaseReference workoutRef = mDatabase.getReference("workouts").child(mCurrentUid).push();
        String pushId = workoutRef.getKey();

        final Workout workout = new Workout(name, pushId);

        mExercises.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Exercise> workoutExercises = new ArrayList<>();
                for (DataSnapshot exerciseSnapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = exerciseSnapshot.getValue(Exercise.class);
                    workoutExercises.add(exercise);
                }

                workout.setExercises(workoutExercises);
                workoutRef.setValue(workout);

                mView.displayWorkoutSaved();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void detach() {
        mView = null;
        mCurrentUid = null;
        mDatabase = null;
        mExercises = null;
        mCalories = null;
    }
}
