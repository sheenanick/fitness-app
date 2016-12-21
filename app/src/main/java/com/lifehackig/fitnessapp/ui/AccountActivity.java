package com.lifehackig.fitnessapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.lifehackig.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity {
    @Bind(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent homeIntent = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.action_workouts:
                        Intent accountIntent = new Intent(AccountActivity.this, SaveWorkoutsActivity.class);
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });
    }
}
