package com.lifehackig.fitnessapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseExerciseListAdapter;
import com.lifehackig.fitnessapp.adapters.FirebaseExerciseViewHolder;
import com.lifehackig.fitnessapp.models.Exercise;
import com.lifehackig.fitnessapp.models.Workout;
import com.lifehackig.fitnessapp.util.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DayActivity extends AppCompatActivity implements View.OnClickListener, SaveWorkoutDialogFragment.SaveWorkoutDialogListener {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.emptyView) TextView mEmptyView;
    @Bind(R.id.calories) TextView mCalories;
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @Bind(R.id.saveButton) Button mSaveButton;
    @Bind(R.id.fab) FloatingActionButton mFab;

    private String mYear;
    private String mMonth;
    private String mDay;

    private String mCurrentUid;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseExerciseListAdapter mAdapter;
    private DatabaseReference mExercises;
    private DatabaseReference mCaloriesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mYear = intent.getStringExtra("year");
        mMonth = intent.getStringExtra("month");
        mDay = intent.getStringExtra("day");
        String date = mMonth + "/" + mDay + "/" + mYear;
        getSupportActionBar().setTitle(date);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mCurrentUid = user.getUid();

                    String dateRefId = mMonth + mDay + mYear;
                    mExercises = FirebaseDatabase.getInstance().getReference("members").child(mCurrentUid).child("days").child(dateRefId).child("exercises");
                    mExercises.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            } else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mSaveButton.setVisibility(View.VISIBLE);
                                mEmptyView.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    mCaloriesRef = FirebaseDatabase.getInstance().getReference("members").child(mCurrentUid).child("days").child(dateRefId).child("calories");

                    setupFirebaseAdapter();
                    attachItemTouchHelper();
                    setCaloriesTextView();
                }
            }
        };

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent homeIntent = new Intent(DayActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.action_workouts:
                        Intent workoutIntent = new Intent(DayActivity.this, WorkoutsActivity.class);
                        startActivity(workoutIntent);
                        break;
                    case R.id.action_account:
                        Intent accountIntent = new Intent(DayActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });

        mSaveButton.setOnClickListener(this);
        mFab.setOnClickListener(this);
    }

    private void setupFirebaseAdapter() {
        mAdapter = new FirebaseExerciseListAdapter(Exercise.class, R.layout.exercise_list_item, FirebaseExerciseViewHolder.class, mExercises, mCaloriesRef);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void attachItemTouchHelper() {
        SimpleItemTouchHelperCallback simpleCallback = new SimpleItemTouchHelperCallback(0, ItemTouchHelper.LEFT, mAdapter, getApplicationContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setCaloriesTextView() {
        mCaloriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String totalCalories = dataSnapshot.getValue().toString();
                    mCalories.setText("Calories Burned: " + totalCalories);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mFab) {
            Intent intent = new Intent(DayActivity.this, NewExerciseActivity.class);
            intent.putExtra("year", mYear);
            intent.putExtra("month", mMonth);
            intent.putExtra("day", mDay);
            startActivity(intent);
        }
        if (v == mSaveButton) {
            launchAlertDialog();
        }
    }

    private void launchAlertDialog() {
        SaveWorkoutDialogFragment dialogFragment = new SaveWorkoutDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "saveWorkout");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        saveWorkout(name);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    private void saveWorkout(String name) {
        final DatabaseReference workoutRef = FirebaseDatabase.getInstance().getReference("workouts").child(mCurrentUid).push();
        String pushId = workoutRef.getKey();

        final Workout workout = new Workout(name, pushId);

        mExercises.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Exercise> workoutExercises = new ArrayList<>();
                for (DataSnapshot exerciseSnapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = exerciseSnapshot.getValue(Exercise.class);
                    workoutExercises.add(exercise);
                }

                workout.setExercises(workoutExercises);
                workoutRef.setValue(workout);
                Toast toast = Toast.makeText(DayActivity.this,"Workout Saved", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        Intent intent = new Intent(DayActivity.this, LogInActivity.class);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
