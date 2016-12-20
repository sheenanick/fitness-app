package com.lifehackig.fitnessapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lifehackig.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewExerciseActivity extends AppCompatActivity {
    @Bind(R.id.muscleSpinner) Spinner mMuscleSpinner;

    private int mYear;
    private int mMonth;
    private int mDay;

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
    }
}
