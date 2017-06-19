package com.lifehackig.fitnessapp.ui.launcher;

import android.content.Intent;
import android.os.Bundle;

import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.welcome.WelcomeActivity;

public class LauncherActivity extends BaseActivity implements LauncherContract.MvpView {
    private LauncherPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LauncherPresenter(this);
    }

    @Override
    public void navigateToWelcome() {
        Intent intent = new Intent(LauncherActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
