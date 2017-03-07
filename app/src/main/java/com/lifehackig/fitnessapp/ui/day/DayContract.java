package com.lifehackig.fitnessapp.ui.day;

import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 3/6/17.
 */

public interface DayContract {
    interface Presenter extends BasePresenter {
        void initFirebaseAdapter();
        void getExercises();
        void getCalories();
        void saveWorkout(String name);
    }
    interface MvpView extends BaseMvpView {
        void setupFirebaseAdapter(DatabaseReference exercises, DatabaseReference calories);
        void noExercisesView();
        void hasExercisesView();
        void setCaloriesTextView(int totalCalories);
        void setSaveButtonVisibility(int visibility);
        void displayWorkoutSaved();
    }
}
