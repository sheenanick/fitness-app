package com.lifehackig.fitnessapp.ui.new_exercise;

import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 3/9/17.
 */

public interface NewExerciseContract {
    interface Presenter extends BasePresenter {
        void saveExercise(String name, int reps, int minutes, int intWeight, String muscle, int intCalories, String date);
    }
    interface MvpView extends BaseMvpView {
        void navigateToDayActivity();
    }
}
