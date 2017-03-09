package com.lifehackig.fitnessapp.ui.new_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.day.DayActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewExerciseActivity extends BaseActivity implements NewExerciseContract.MvpView, View.OnClickListener {
    @Bind(R.id.nameEditText) EditText mExerciseName;
    @Bind(R.id.repsOrDuration) EditText mRepsOrDuration;
    @Bind(R.id.reps) RadioButton mReps;
    @Bind(R.id.weightEditText) EditText mWeight;
    @Bind(R.id.caloriesEditText) EditText mCalories;
    @Bind(R.id.muscleSpinner) Spinner mMuscleSpinner;
    @Bind(R.id.addButton) Button mAddButton;

    private String mYear;
    private String mMonth;
    private String mDay;
    private String mDate;

    private NewExercisePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mYear = intent.getStringExtra("year");
        mMonth = intent.getStringExtra("month");
        mDay = intent.getStringExtra("day");
        mDate = mMonth + mDay + mYear;

        setAppBarTitle("New Exercise");
        setBottomNavChecked(0);
        initMuscleSpinner();
        mAddButton.setOnClickListener(this);

        mPresenter = new NewExercisePresenter(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mAddButton) {
            saveNewExercise();
        }
    }

    private void initMuscleSpinner() {
        ArrayAdapter<CharSequence> muscleAdapter = ArrayAdapter.createFromResource(this, R.array.muscles_array, android.R.layout.simple_spinner_dropdown_item);
        mMuscleSpinner.setAdapter(muscleAdapter);
    }

    private void saveNewExercise() {
        String name = mExerciseName.getText().toString().trim();
        String repsDuration = mRepsOrDuration.getText().toString().trim();
        String calories = mCalories.getText().toString().trim();

        boolean validName = isValidName(name);
        boolean validNumber = isValidNumber(repsDuration);
        boolean validCalories = isValidCalories(calories);

        if (!validName || !validNumber || !validCalories) return;

        String muscle = mMuscleSpinner.getSelectedItem().toString();
        int repsOrDuration = Integer.parseInt(repsDuration);
        int intCalories = Integer.parseInt(calories);

        boolean isReps = mReps.isChecked();
        int reps = 0;
        int minutes = 0;
        int intWeight = 0;

        String weight = mWeight.getText().toString().trim();
        if (!weight.equals("")) {
            intWeight = Integer.parseInt(weight);
        }
        if (isReps) {
            reps = repsOrDuration;
        } else {
            minutes = repsOrDuration;
        }

        mPresenter.saveExercise(name, reps, minutes, intWeight, muscle, intCalories, mDate);
    }

    private boolean isValidName(String name) {
        if (name.equals("")){
            mExerciseName.setError("Please enter the exercise name");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidNumber(String number) {
        if (number.equals("")){
            mRepsOrDuration.setError("Please enter a number");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidCalories(String calories) {
        if (calories.equals("")){
            mCalories.setError("Please enter calories burned");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void navigateToDayActivity() {
        Intent intent = new Intent(NewExerciseActivity.this, DayActivity.class);
        intent.putExtra("year", mYear);
        intent.putExtra("month", mMonth);
        intent.putExtra("day", mDay);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
