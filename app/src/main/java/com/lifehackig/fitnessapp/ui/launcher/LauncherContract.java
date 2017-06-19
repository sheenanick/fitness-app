package com.lifehackig.fitnessapp.ui.launcher;

import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 6/19/17.
 */

public interface LauncherContract {
    interface Presenter extends BasePresenter {

    }
    interface MvpView extends BaseMvpView {
        void navigateToWelcome();
    }
}
