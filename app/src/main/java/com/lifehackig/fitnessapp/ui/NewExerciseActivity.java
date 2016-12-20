package com.lifehackig.fitnessapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.lifehackig.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewExerciseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    @Bind(R.id.nameEditText) EditText mExerciseName;
    @Bind(R.id.repsOrDuration) EditText mRepsOrDuration;
    @Bind(R.id.reps) RadioButton mReps;
    @Bind(R.id.minutes) RadioButton mMinutes;
    @Bind(R.id.weightEditText) EditText mWeight;
    @Bind(R.id.calories) EditText mCalories;
    @Bind(R.id.muscleSpinner) Spinner mMuscleSpinner;
    @Bind(R.id.addButton) Button mAddButton;

    private int mYear;
    private int mMonth;
    private int mDay;

    private String mMuscle;

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
        mMuscleSpinner.setOnItemSelectedListener(this);

        mAddButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            saveNewExercise();
        }
    }

    private void saveNewExercise() {
        String name = mExerciseName.getText().toString().trim();
        Integer repsOrDuration = Integer.parseInt(mRepsOrDuration.getText().toString().trim());
        boolean reps = mReps.isChecked();
        boolean minutes = mMinutes.isChecked();
        Integer weight = Integer.parseInt(mWeight.getText().toString().trim());
        Integer calories = Integer.parseInt(mCalories.getText().toString().trim());

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (view == mMuscleSpinner) {
            mMuscle = adapterView.getItemAtPosition(pos).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
