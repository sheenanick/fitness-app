package com.lifehackig.fitnessapp.ui.welcome;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lifehackig.fitnessapp.util.UserManager;

/**
 * Created by Sheena on 6/16/17.
 */

public class WelcomePresenter implements WelcomeContract.Presenter {
    private WelcomeContract.MvpView mView;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;

    public WelcomePresenter(WelcomeContract.MvpView view) {
        mView = view;
        mAuth = FirebaseAuth.getInstance();
        createAuthStateListener();
    }

    private void createAuthStateListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    UserManager.setCurrentUser(user);
                    mView.navigateToMain();
                }
            }
        };
    }

    @Override
    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void removeAuthStateListener() {
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void detach() {
        mView = null;
        mAuthStateListener = null;
        mAuth = null;
    }
}
