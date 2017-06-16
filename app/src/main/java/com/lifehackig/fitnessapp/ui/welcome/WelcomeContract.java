package com.lifehackig.fitnessapp.ui.welcome;

import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 6/16/17.
 */

public interface WelcomeContract {
    interface Presenter extends BasePresenter {
        void addAuthStateListener();
        void removeAuthStateListener();
    }
    interface MvpView extends BaseMvpView {

    }
}
