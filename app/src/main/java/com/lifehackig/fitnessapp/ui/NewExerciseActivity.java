package com.lifehackig.fitnessapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewExerciseActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.nameEditText) EditText mExerciseName;
    @Bind(R.id.repsOrDuration) EditText mRepsOrDuration;
    @Bind(R.id.reps) RadioButton mReps;
    @Bind(R.id.minutes) RadioButton mMinutes;
    @Bind(R.id.weightEditText) EditText mWeight;
    @Bind(R.id.caloriesEditText) EditText mCalories;
    @Bind(R.id.muscleSpinner) Spinner mMuscleSpinner;
    @Bind(R.id.addButton) Button mAddButton;

    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;

    private String mCurrentUid;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Integer mTotalCalories;
    private Integer mIntCalories;
    private DatabaseReference mDateCaloriesRef;
    DatabaseReference mDateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mYear = intent.getIntExtra("year", 0);
        mMonth = intent.getIntExtra("month", 0);
        mDay = intent.getIntExtra("day", 0);
        String date = mMonth + "/" + mDay + "/" + mYear;
        getSupportActionBar().setTitle(date);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.muscles_array, android.R.layout.simple_spinner_dropdown_item);
        mMuscleSpinner.setAdapter(adapter);

        mAddButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mCurrentUid = user.getUid();
                }
            }
        };
    }


    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            saveNewExercise();
            updateCalories();
            returnToDayActivity();
        }
    }

    public void saveNewExercise() {
        String name = mExerciseName.getText().toString().trim();
        Integer repsOrDuration = Integer.parseInt(mRepsOrDuration.getText().toString().trim());
        boolean isReps = mReps.isChecked();
        Integer reps = 0;
        Integer minutes = 0;
        mIntCalories = Integer.parseInt(mCalories.getText().toString().trim());
        String muscle = mMuscleSpinner.getSelectedItem().toString();

        String weight = mWeight.getText().toString().trim();
        Integer intWeight = 0;
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
        Exercise exercise = new Exercise(name, reps, minutes, intWeight, muscle, mIntCalories, pushId);
        exerciseRef.setValue(exercise);
    }

    public void updateCalories() {
        mDateCaloriesRef = mDateRef.child("calories");
        mDateCaloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mTotalCalories = 0;
                } else {
                    mTotalCalories = Integer.parseInt(dataSnapshot.getValue().toString());
                }
                Integer newTotalCalories = mTotalCalories + mIntCalories;
                mDateCaloriesRef.setValue(newTotalCalories);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (mTotalCalories != null) {
            Integer newTotalCalories = mTotalCalories + mIntCalories;
            mDateCaloriesRef.setValue(newTotalCalories);
        }
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
