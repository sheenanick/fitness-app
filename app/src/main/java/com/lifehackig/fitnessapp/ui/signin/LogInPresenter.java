package com.lifehackig.fitnessapp.ui.signin;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lifehackig.fitnessapp.util.UserManager;

import java.util.concurrent.Executor;

/**
 * Created by Sheena on 3/6/17.
 */

public class LogInPresenter implements LogInContract.Presenter {
    private LogInContract.MvpView mView;
    private Activity mActivity;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;

    public LogInPresenter(LogInContract.MvpView view, Activity activity) {
        mView = view;
        mActivity = activity;
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
    public void signInWithEmailAndPassword(String email, String password) {
        mView.displayLoadingAnimation();
        Log.d("SignIn", email + " " +  password);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mView.hideLoadingAnimation();
                if (!task.isSuccessful()) {
                    mView.displayLogInError();
                }
            }
        });
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        mView.displayLoadingAnimation();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        mView.hideLoadingAnimation();
                    }
                });
    }

    @Override
    public void detach() {
        mView = null;
        mAuthStateListener = null;
        mAuth = null;
    }
}
