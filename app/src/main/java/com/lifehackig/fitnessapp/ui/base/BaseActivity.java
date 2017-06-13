package com.lifehackig.fitnessapp.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.account.AccountActivity;
import com.lifehackig.fitnessapp.ui.main.MainActivity;
import com.lifehackig.fitnessapp.ui.signin.LogInActivity;
import com.lifehackig.fitnessapp.ui.workouts.WorkoutsActivity;
import com.lifehackig.fitnessapp.util.UserManager;

public class BaseActivity extends AppCompatActivity implements BaseMvpView {
    private ProgressDialog mProgressDialog;
    public BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void setContentView(int layoutResID) {
        RelativeLayout fullView = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        initBottomNav();
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
    public void setAppBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void initBottomNav() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        navigateToMain();
                        break;
                    case R.id.action_workouts:
                        navigateToWorkouts();
                        break;
                    case R.id.action_account:
                        navigateToAccount();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void setBottomNavChecked(int position) {
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void hideBottomNav() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToWorkouts() {
        Intent workoutIntent = new Intent(BaseActivity.this, WorkoutsActivity.class);
        startActivity(workoutIntent);
    }

    private void navigateToAccount() {
        Intent accountIntent = new Intent(BaseActivity.this, AccountActivity.class);
        startActivity(accountIntent);
    }

    private void logout() {
        UserManager.logoutActiveUser();
        Intent intent = new Intent(BaseActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
