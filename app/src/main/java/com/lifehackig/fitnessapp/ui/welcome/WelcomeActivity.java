package com.lifehackig.fitnessapp.ui.welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.signin.LogInActivity;
import com.lifehackig.fitnessapp.ui.signup.SignUpActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity implements WelcomeContract.MvpView {
    private WelcomePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        hideBottomNav();
        mPresenter = new WelcomePresenter(this);
    }

    @OnClick(R.id.signUpButton)
    public void navigateToSignUp() {
        Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logInButton)
    public void navigateToLogIn() {
        Intent intent = new Intent(WelcomeActivity.this, LogInActivity.class);
        startActivity(intent);
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
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

}
