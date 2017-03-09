package com.lifehackig.fitnessapp.ui.workouts;

import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 3/9/17.
 */

public interface WorkoutsContract {
    interface Presenter extends BasePresenter {
        void getWorkouts();
    }
    interface MvpView extends BaseMvpView {
        void setupFirebaseAdapter(DatabaseReference workouts);
        void showEmptyView();
        void hideEmptyView();
    }
}
