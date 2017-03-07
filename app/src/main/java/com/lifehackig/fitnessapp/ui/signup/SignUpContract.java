package com.lifehackig.fitnessapp.ui.signup;

import com.lifehackig.fitnessapp.ui.base.BaseMvpView;
import com.lifehackig.fitnessapp.ui.base.BasePresenter;

/**
 * Created by Sheena on 3/6/17.
 */

public interface SignUpContract {
    interface Presenter extends BasePresenter {
        void addAuthStateListener();
        void removeAuthStateListener();
        void createUserWithEmailAndPassword(String email, String password, String fullName);
    }
    interface MvpView extends BaseMvpView {
        void displaySignUpError();
    }
}
