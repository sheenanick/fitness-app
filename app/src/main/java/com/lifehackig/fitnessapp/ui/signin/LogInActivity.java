package com.lifehackig.fitnessapp.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends BaseActivity implements LogInContract.MvpView {
    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.facebookButton) LoginButton mFacebookButton;

    private LogInPresenter mPresenter;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        hideBottomNav();
        mPresenter = new LogInPresenter(this, this);
    }

    private void initFacebookLogin() {
        mCallbackManager = CallbackManager.Factory.create();

        mFacebookButton.setReadPermissions("email", "public_profile");
        mFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.logInButton)
    public void logInWithPassword() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        boolean invalidEmail = email.equals("");
        boolean invalidPassword = password.equals("");

        if (invalidEmail) {
            mEmail.setError("Please enter your email");
        }
        if (invalidPassword) {
            mPassword.setError("Please enter your password");
        }
        if (invalidEmail || invalidPassword) return;

        mPresenter.signInWithEmailAndPassword(email, password);
    }

    @OnClick(R.id.signUpText)
    public void navigateToSignUp() {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void displayLogInError() {
        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.addAuthStateListener();
        initFacebookLogin();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.removeAuthStateListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
