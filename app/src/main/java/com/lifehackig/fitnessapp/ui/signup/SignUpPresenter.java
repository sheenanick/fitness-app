package com.lifehackig.fitnessapp.ui.signup;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.lifehackig.fitnessapp.util.UserManager;

/**
 * Created by Sheena on 3/6/17.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.MvpView mView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public SignUpPresenter(SignUpContract.MvpView view) {
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
    public void createUserWithEmailAndPassword(String email, String password) {
        mView.displayLoadingAnimation();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mView.hideLoadingAnimation();

                if (!task.isSuccessful()) {
                    mView.displaySignUpError();
                }
            }
        });
    }

    @Override
    public void detach() {
        mView = null;
        mAuth = null;
        mAuthStateListener = null;
    }
}
