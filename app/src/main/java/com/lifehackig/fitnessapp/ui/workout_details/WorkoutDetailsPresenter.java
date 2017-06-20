package com.lifehackig.fitnessapp.ui.workout_details;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lifehackig.fitnessapp.util.UserManager;

/**
 * Created by Sheena on 3/9/17.
 */

public class WorkoutDetailsPresenter implements WorkoutDetailsContract.Presenter {
    private WorkoutDetailsContract.MvpView mView;

    public WorkoutDetailsPresenter(WorkoutDetailsContract.MvpView view) {
        mView = view;
    }

    public void getWorkoutDetails(String workoutId) {
        FirebaseUser user = UserManager.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference exercises = FirebaseDatabase.getInstance().getReference("workouts").child(uid).child(workoutId).child("exercises");
            mView.setupFirebaseAdapter(exercises);
        }
    }

    @Override
    public void detach() {
        mView = null;
    }
}
