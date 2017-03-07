package com.lifehackig.fitnessapp.ui.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.DayActivity;
import com.lifehackig.fitnessapp.ui.WorkoutsActivity;
import com.lifehackig.fitnessapp.ui.account.AccountActivity;
import com.lifehackig.fitnessapp.ui.signin.LogInActivity;
import com.lifehackig.fitnessapp.util.UserManager;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.date) TextView mDateTextView;
    @Bind(R.id.calories) TextView mCalories;
    @Bind(R.id.seeDetailsButton) Button mSeeDetailsButton;
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    private DatabaseReference mMemberRef;

    private DateFormat dateFormatter;
    private DateFormat refIdFormatter;
    private Date mDate;

    private CaldroidFragment caldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseUser user = UserManager.getCurrentUser();
        mMemberRef = FirebaseDatabase.getInstance().getReference("members").child(user.getUid());
        getSupportActionBar().setTitle(user.getDisplayName());
        setCalendarBackgroundColors();

        mDate = new Date();
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormatter.format(mDate);
        mDateTextView.setText(formattedDate);

        refIdFormatter = new SimpleDateFormat("MMddyyyy");
        String dateRefId = refIdFormatter.format(mDate);
        setCaloriesTextView(dateRefId);

        caldroidFragment = new CaldroidFragment();
        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            caldroidFragment.setArguments(args);
        }

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                mDate = date;

                caldroidFragment.clearSelectedDates();
                caldroidFragment.setSelectedDates(date, date);
                caldroidFragment.refreshView();

                String formattedDate = dateFormatter.format(date);
                mDateTextView.setText(formattedDate);

                String dateRefId = refIdFormatter.format(date);
                setCaloriesTextView(dateRefId);
            }
            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    setCalendarBackgroundColors();
                }
            }
        });

        mSeeDetailsButton.setOnClickListener(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_workouts:
                        Intent workoutIntent = new Intent(MainActivity.this, WorkoutsActivity.class);
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

    public void setCalendarBackgroundColors() {
        mMemberRef.child("days").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ColorDrawable yellow = new ColorDrawable(getResources().getColor(R.color.colorAccent));
                for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                    String stringDate = daySnapshot.child("date").getValue().toString();
                    try {
                        Date date = refIdFormatter.parse(stringDate);
                        caldroidFragment.setBackgroundDrawableForDate(yellow, date);
                        caldroidFragment.refreshView();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void setCaloriesTextView(String dateRefId) {
        DatabaseReference caloriesRef = mMemberRef.child("days").child(dateRefId).child("calories");
        caloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalCalories;
                if (dataSnapshot.getValue() == null) {
                    totalCalories = "0";
                } else {
                    totalCalories = dataSnapshot.getValue().toString();
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
            DateFormat yearFormat = new SimpleDateFormat("yyyy");
            DateFormat monthFormat = new SimpleDateFormat("MM");
            DateFormat dayFormat = new SimpleDateFormat("dd");

            intent.putExtra("year", yearFormat.format(mDate));
            intent.putExtra("month", monthFormat.format(mDate));
            intent.putExtra("day", dayFormat.format(mDate));
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
        UserManager.logoutActiveUser();
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

}
