package com.lifehackig.fitnessapp.ui;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.calendarView) CalendarView mCalendarView;
    @Bind(R.id.date) TextView mDateTextView;
    @Bind(R.id.calories) TextView mCalories;
    @Bind(R.id.seeDetailsButton) Button mSeeDetailsButton;
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mCurrentUid;

    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String date = mMonth + "/" + mDay + "/" + mYear;

        mDateTextView.setText(date);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    getSupportActionBar().setTitle(user.getDisplayName());
                    mCurrentUid = user.getUid();
                    String dateRefId = mMonth.toString() + mDay.toString() + mYear.toString();
                    setCaloriesTextView(dateRefId);
                }
            }
        };

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                mYear = year;
                mMonth = month + 1;
                mDay = day;
                String date = mMonth + "/" + mDay + "/" + mYear;
                mDateTextView.setText(date);

                String dateRefId = mMonth.toString() + mDay.toString() + mYear.toString();
                setCaloriesTextView(dateRefId);
            }
        });

        mSeeDetailsButton.setOnClickListener(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_workouts:
                        Intent workoutIntent = new Intent(MainActivity.this, SaveWorkoutsActivity.class);
                        startActivity(workoutIntent);
                        break;
                    case R.id.action_account:
                        Intent accountIntent = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });
    }

    public void setCaloriesTextView(String dateRefId) {
        DatabaseReference caloriesRef = FirebaseDatabase.getInstance().getReference("members").child(mCurrentUid).child(dateRefId).child("calories");
        caloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalCalories;
                if (dataSnapshot.getValue() == null) {
                    totalCalories = "Calories: 0";
                } else {
                    totalCalories = "Calories: " + dataSnapshot.getValue().toString();
                }
                mCalories.setText(totalCalories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mSeeDetailsButton) {
            Intent intent = new Intent(MainActivity.this, DayActivity.class);
            intent.putExtra("year", mYear);
            intent.putExtra("month", mMonth);
            intent.putExtra("day", mDay);
            startActivity(intent);
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

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
