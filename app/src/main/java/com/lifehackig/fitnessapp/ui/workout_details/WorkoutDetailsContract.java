package com.lifehackig.fitnessapp.ui.workout_details;

import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 3/9/17.
 */

public interface WorkoutDetailsContract {
    interface Presenter extends BasePresenter {
        void getWorkoutDetails(String workoutId);
    }
    interface MvpView extends BaseMvpView {
        void setupFirebaseAdapter(DatabaseReference exercises);
    }
}
