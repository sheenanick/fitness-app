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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.models.Exercise;
import com.lifehackig.fitnessapp.models.Workout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewExerciseActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @Bind(R.id.nameEditText) EditText mExerciseName;
    @Bind(R.id.repsOrDuration) EditText mRepsOrDuration;
    @Bind(R.id.reps) RadioButton mReps;
    @Bind(R.id.minutes) RadioButton mMinutes;
    @Bind(R.id.weightEditText) EditText mWeight;
    @Bind(R.id.caloriesEditText) EditText mCalories;
    @Bind(R.id.muscleSpinner) Spinner mMuscleSpinner;
    @Bind(R.id.workoutSpinner) Spinner mWorkoutSpinner;
    @Bind(R.id.selectButton) Button mSelectButton;
    @Bind(R.id.addButton) Button mAddButton;
    @Bind(R.id.newExerciseButton) Button mNewExerciseButton;
    @Bind(R.id.cancelButton) Button mCancelButton;
    @Bind(R.id.workoutForm) LinearLayout mWorkoutForm;
    @Bind(R.id.newExerciseForm) LinearLayout mNewExerciseForm;

    private String mYear;
    private String mMonth;
    private String mDay;

    private String mCurrentUid;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Integer mTotalCalories;
    private DatabaseReference mDateCaloriesRef;
    DatabaseReference mDateRef;

    private List<Workout> mWorkoutObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mYear = intent.getStringExtra("year");
        mMonth = intent.getStringExtra("month");
        mDay = intent.getStringExtra("day");
        String date = mMonth + "/" + mDay + "/" + mYear;
        getSupportActionBar().setTitle(date);

        ArrayAdapter<CharSequence> muscleAdapter = ArrayAdapter.createFromResource(this, R.array.muscles_array, android.R.layout.simple_spinner_dropdown_item);
        mMuscleSpinner.setAdapter(muscleAdapter);

        mSelectButton.setOnClickListener(this);
        mNewExerciseButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mCurrentUid = user.getUid();
                    DatabaseReference savedWorkoutsRef = FirebaseDatabase.getInstance().getReference("workouts").child(mCurrentUid);
                    savedWorkoutsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> workoutNames = new ArrayList<>();
                            if (dataSnapshot.getValue() == null) {
                                workoutNames.add("No saved workouts");
                            } else {
                                for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {
                                    Workout workout = workoutSnapshot.getValue(Workout.class);
                                    mWorkoutObjects.add(workout);
                                    workoutNames.add(workout.getName());
                                }
                            }
                            ArrayAdapter<String> workoutAdapter = new ArrayAdapter<>(NewExerciseActivity.this, android.R.layout.simple_spinner_item, workoutNames);
                            workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mWorkoutSpinner.setAdapter(workoutAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        };

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent homeIntent = new Intent(NewExerciseActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.action_workouts:
                        Intent workoutIntent = new Intent(NewExerciseActivity.this, WorkoutsActivity.class);
                        startActivity(workoutIntent);
                        break;
                    case R.id.action_account:
                        Intent accountIntent = new Intent(NewExerciseActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mSelectButton) {
            if (!mWorkoutSpinner.getSelectedItem().toString().equals("No saved workouts")) {
                saveWorkout();
            }
        }
        if (v == mNewExerciseButton) {
            mNewExerciseForm.setVisibility(View.VISIBLE);
            mWorkoutForm.setVisibility(View.GONE);
        }
        if (v == mAddButton) {
            saveNewExercise();
        }
        if (v == mCancelButton) {
            mNewExerciseForm.setVisibility(View.GONE);
            mWorkoutForm.setVisibility(View.VISIBLE);
        }
    }

    public void saveWorkout() {
        Integer position = mWorkoutSpinner.getSelectedItemPosition();
        Workout workout = mWorkoutObjects.get(position);
        List<Exercise> exercises = workout.getExercises();

        mDateRef = FirebaseDatabase.getInstance().getReference("members").child(mCurrentUid).child(mMonth.toString() + mDay.toString() + mYear.toString());
        DatabaseReference exercisesRef = mDateRef.child("exercises");
        Integer workoutTotalCalories = 0;
        for (Exercise exercise :  exercises) {
            workoutTotalCalories += exercise.getCalories();
            DatabaseReference exerciseRef = exercisesRef.push();
            String pushId = exerciseRef.getKey();
            exercise.setPushId(pushId);
            exerciseRef.setValue(exercise);
        }

        updateCalories(workoutTotalCalories);
        returnToDayActivity();
    }

    public void saveNewExercise() {
        String name = mExerciseName.getText().toString().trim();
        String repsDuration = mRepsOrDuration.getText().toString().trim();
        String calories = mCalories.getText().toString().trim();

        boolean validName = isValidName(name);
        boolean validNumber = isValidNumber(repsDuration);
        boolean validCalories = isValidCalories(calories);

        if (!validName || !validNumber || !validCalories) return;

        String muscle = mMuscleSpinner.getSelectedItem().toString();
        Integer repsOrDuration = Integer.parseInt(repsDuration);
        Integer intCalories = Integer.parseInt(calories);

        boolean isReps = mReps.isChecked();
        Integer reps = 0;
        Integer minutes = 0;
        Integer intWeight = 0;

        String weight = mWeight.getText().toString().trim();
        if (!weight.equals("")) {
            intWeight = Integer.parseInt(weight);
        }
        if (isReps) {
            reps = repsOrDuration;
        } else {
            minutes = repsOrDuration;
        }

        mDateRef = FirebaseDatabase.getInstance().getReference("members").child(mCurrentUid).child(mMonth.toString() + mDay.toString() + mYear.toString());
        DatabaseReference exerciseRef = mDateRef.child("exercises").push();
        String pushId = exerciseRef.getKey();
        Exercise exercise = new Exercise(name, reps, minutes, intWeight, muscle, intCalories, pushId);
        exerciseRef.setValue(exercise);

        updateCalories(intCalories);
        returnToDayActivity();
    }

    public boolean isValidName(String name) {
        if (name.equals("")){
            mExerciseName.setError("Please enter the exercise name");
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidNumber(String number) {
        if (number.equals("")){
            mRepsOrDuration.setError("Please enter a number");
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidCalories(String calories) {
        if (calories.equals("")){
            mCalories.setError("Please enter calories burned");
            return false;
        } else {
            return true;
        }
    }

    public void updateCalories(final Integer exerciseCalories) {
        mDateCaloriesRef = mDateRef.child("calories");
        mDateCaloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mTotalCalories = 0;
                } else {
                    mTotalCalories = Integer.parseInt(dataSnapshot.getValue().toString());
                }
                Integer newTotalCalories = mTotalCalories + exerciseCalories;
                mDateCaloriesRef.setValue(newTotalCalories);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void returnToDayActivity() {
        Intent intent = new Intent(NewExerciseActivity.this, DayActivity.class);
        intent.putExtra("year", mYear);
        intent.putExtra("month", mMonth);
        intent.putExtra("day", mDay);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
        Intent intent = new Intent(NewExerciseActivity.this, LogInActivity.class);
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
