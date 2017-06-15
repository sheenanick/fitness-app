package com.lifehackig.fitnessapp.ui.main;

import com.google.firebase.database.DataSnapshot;
import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 3/6/17.
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
        void getUser();
        void getExercisedDays();
    }
    interface MvpView extends BaseMvpView {
        void setCalendarBackgroundColors(DataSnapshot dataSnapshot);
    }
}
