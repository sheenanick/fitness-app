package com.lifehackig.fitnessapp.ui.log_exercise;

import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sheena on 3/9/17.
 */

public interface LogExerciseContract {
    interface Presenter extends BasePresenter {
        void getWorkoutNames();
        void addSavedWorkout(int position, Date date);
    }
    interface MvpView extends BaseMvpView {
        void setupWorkoutSpinner(ArrayList<String> workoutNames);
        void navigateToDayActivity();
    }
}
