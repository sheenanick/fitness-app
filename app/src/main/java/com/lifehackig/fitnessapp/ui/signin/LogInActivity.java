package com.lifehackig.fitnessapp.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends BaseActivity implements LogInContract.MvpView {
    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;

    private LogInPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        hideBottomNav();
        mPresenter = new LogInPresenter(this);
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
    public void onStart() {
        super.onStart();
        mPresenter.addAuthStateListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.removeAuthStateListener();
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
