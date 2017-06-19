package com.lifehackig.fitnessapp.ui.launcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lifehackig.fitnessapp.util.UserManager;

/**
 * Created by Sheena on 6/19/17.
 */

public class LauncherPresenter implements LauncherContract.Presenter {
    LauncherContract.MvpView mView;

    public LauncherPresenter(LauncherContract.MvpView view) {
        mView = view;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserManager.setCurrentUser(user);
            mView.navigateToMain();
        } else {
            mView.navigateToWelcome();
        }
    }

    @Override
    public void detach() {
        mView = null;
    }
}
