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
import com.lifehackig.fitnessapp.util.Utilities;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewExerciseActivity extends BaseActivity implements NewExerciseContract.MvpView, View.OnClickListener {
    @BindView(R.id.nameEditText) EditText mExerciseName;
    @BindView(R.id.repsOrDuration) EditText mRepsOrDuration;
    @BindView(R.id.reps) RadioButton mReps;
    @BindView(R.id.weightEditText) EditText mWeight;
    @BindView(R.id.caloriesEditText) EditText mCalories;
    @BindView(R.id.muscleSpinner) Spinner mMuscleSpinner;
    @BindView(R.id.addButton) Button mAddButton;

    private Date mDate;
    private NewExercisePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);
        ButterKnife.bind(this);

        setAppBarTitle("New Exercise");
        setBottomNavChecked(0);

        Intent intent = getIntent();
        mDate = new Date();
        mDate.setTime(intent.getLongExtra("date", -1));

        mPresenter = new NewExercisePresenter(this);
        mAddButton.setOnClickListener(this);
        initMuscleSpinner();
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

        boolean validName = !Utilities.isInputEmpty(name, mExerciseName);
        boolean validNumber = !Utilities.isInputEmpty(repsDuration, mRepsOrDuration);
        boolean validCalories = !Utilities.isInputEmpty(calories, mCalories);

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

    @Override
    public void navigateToDayActivity() {
        Intent intent = new Intent(NewExerciseActivity.this, DayActivity.class);
        intent.putExtra("date", mDate.getTime());
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
