package com.lifehackig.fitnessapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.main.MainActivity;
import com.lifehackig.fitnessapp.ui.workouts.WorkoutsActivity;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends BaseActivity {
    @Bind(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        setAppBarTitle("Account Settings");

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent homeIntent = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.action_workouts:
                        Intent accountIntent = new Intent(AccountActivity.this, WorkoutsActivity.class);
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });
        mBottomNavigationView.getMenu().getItem(2).setChecked(true);
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
}
