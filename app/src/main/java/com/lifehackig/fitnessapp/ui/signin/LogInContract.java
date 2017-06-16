package com.lifehackig.fitnessapp.ui.signin;

import com.facebook.AccessToken;
import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 3/6/17.
 */

public interface LogInContract {
    interface Presenter extends BasePresenter {
        void addAuthStateListener();
        void removeAuthStateListener();
        void signInWithEmailAndPassword(String email, String password);
        void handleFacebookAccessToken(AccessToken token);
    }
    interface MvpView extends BaseMvpView {
        void displayLogInError();
    }
}
