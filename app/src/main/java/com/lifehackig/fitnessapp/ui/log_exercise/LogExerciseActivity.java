package com.lifehackig.fitnessapp.ui.log_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.day.DayActivity;
import com.lifehackig.fitnessapp.ui.new_exercise.NewExerciseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LogExerciseActivity extends BaseActivity implements LogExerciseContract.MvpView, View.OnClickListener {
    @Bind(R.id.workoutSpinner) Spinner mWorkoutSpinner;
    @Bind(R.id.selectButton) Button mSelectButton;
    @Bind(R.id.newExerciseButton) Button mNewExerciseButton;

    private Date mDate;
    private LogExercisePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_exercise);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mDate = new Date();
        mDate.setTime(intent.getLongExtra("date", -1));

        setAppBarTitle("Log Exercise");
        setBottomNavChecked(0);

        mSelectButton.setOnClickListener(this);
        mNewExerciseButton.setOnClickListener(this);

        mPresenter = new LogExercisePresenter(this);
        mPresenter.getWorkoutNames();
    }

    @Override
    public void setupWorkoutSpinner(ArrayList<String> workoutNames) {
        ArrayAdapter<String> workoutAdapter = new ArrayAdapter<>(LogExerciseActivity.this, android.R.layout.simple_spinner_item, workoutNames);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWorkoutSpinner.setAdapter(workoutAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mSelectButton) {
            if (!mWorkoutSpinner.getSelectedItem().toString().equals("No saved workouts")) {
                int position = mWorkoutSpinner.getSelectedItemPosition();
                DateFormat refIdFormatter = new SimpleDateFormat("MMddyyyy", Locale.US);
                mPresenter.addSavedWorkout(position, refIdFormatter.format(mDate));
            }
        }
        if (v == mNewExerciseButton) {
            navigateToNewExerciseActivity();
        }
    }

    private void navigateToNewExerciseActivity() {
        Intent intent = new Intent(LogExerciseActivity.this, NewExerciseActivity.class);
        intent.putExtra("date", mDate.getTime());
        startActivity(intent);
    }

    @Override
    public void navigateToDayActivity() {
        Intent intent = new Intent(LogExerciseActivity.this, DayActivity.class);
        intent.putExtra("date", mDate.getTime());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
