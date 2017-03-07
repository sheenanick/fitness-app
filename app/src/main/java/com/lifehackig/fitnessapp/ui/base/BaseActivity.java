package com.lifehackig.fitnessapp.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lifehackig.fitnessapp.ui.LogInActivity;
import com.lifehackig.fitnessapp.util.UserManager;

public class BaseActivity extends AppCompatActivity implements BaseMvpView {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
    }

    @Override
    public boolean displayLoadingAnimation() {
        mProgressDialog.show();
        return false;
    }

    @Override
    public void hideLoadingAnimation() {
        mProgressDialog.dismiss();
    }

    @Override
    public void logout() {
        UserManager.logoutActiveUser();
        Intent intent = new Intent(BaseActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
