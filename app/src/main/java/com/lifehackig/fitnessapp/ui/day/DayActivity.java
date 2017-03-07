package com.lifehackig.fitnessapp.ui.day;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
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

import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseExerciseListAdapter;
import com.lifehackig.fitnessapp.adapters.FirebaseExerciseViewHolder;
import com.lifehackig.fitnessapp.models.Exercise;
import com.lifehackig.fitnessapp.ui.NewExerciseActivity;
import com.lifehackig.fitnessapp.ui.SaveWorkoutDialogFragment;
import com.lifehackig.fitnessapp.ui.WorkoutsActivity;
import com.lifehackig.fitnessapp.ui.account.AccountActivity;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.util.SimpleItemTouchHelperCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DayActivity extends BaseActivity implements DayContract.MvpView, View.OnClickListener, SaveWorkoutDialogFragment.SaveWorkoutDialogListener {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.emptyView) TextView mEmptyView;
    @Bind(R.id.calories) TextView mCalories;
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @Bind(R.id.saveButton) Button mSaveButton;
    @Bind(R.id.fab) FloatingActionButton mFab;

    private String mYear;
    private String mMonth;
    private String mDay;

    private DayPresenter mPresenter;
    private FirebaseExerciseListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mYear = intent.getStringExtra("year");
        mMonth = intent.getStringExtra("month");
        mDay = intent.getStringExtra("day");

        setAppBarTitle(mMonth + "/" + mDay + "/" + mYear);

        mPresenter = new DayPresenter(this, mMonth + mDay + mYear);
        mPresenter.initFirebaseAdapter();
        mPresenter.getExercises();
        mPresenter.getCalories();

        initBottomNav();
        attachItemTouchHelper();

        mSaveButton.setOnClickListener(this);
        mFab.setOnClickListener(this);
    }

    private void setAppBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    private void initBottomNav() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        navigateToMain();
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
    }

    private void attachItemTouchHelper() {
        SimpleItemTouchHelperCallback simpleCallback = new SimpleItemTouchHelperCallback(0, ItemTouchHelper.LEFT, mAdapter, getApplicationContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void setupFirebaseAdapter(DatabaseReference exercises, DatabaseReference calories) {
        mAdapter = new FirebaseExerciseListAdapter(Exercise.class, R.layout.exercise_list_item, FirebaseExerciseViewHolder.class, exercises, calories);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void noExercisesView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hasExercisesView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mSaveButton.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void setCaloriesTextView(int totalCalories) {
        String caloriesText = "Calories Burned: " + totalCalories;
        mCalories.setText(caloriesText);
    }

    @Override
    public void setSaveButtonVisibility(int visibility) {
        mSaveButton.setVisibility(visibility);
    }

    @Override
    public void displayWorkoutSaved() {
        Toast toast = Toast.makeText(DayActivity.this,"Workout Saved", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
        mPresenter.saveWorkout(name);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

}
