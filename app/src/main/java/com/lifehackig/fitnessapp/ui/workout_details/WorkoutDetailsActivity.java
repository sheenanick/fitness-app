package com.lifehackig.fitnessapp.ui.workout_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseExerciseViewHolder;
import com.lifehackig.fitnessapp.adapters.WorkoutExerciseListAdapter;
import com.lifehackig.fitnessapp.models.Exercise;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutDetailsActivity extends BaseActivity implements WorkoutDetailsContract.MvpView{
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    private WorkoutExerciseListAdapter mAdapter;
    private WorkoutDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String workoutId = intent.getStringExtra("workoutId");
        String workoutName = intent.getStringExtra("workoutName");

        setAppBarTitle(workoutName);
        setBottomNavChecked(1);

        mPresenter = new WorkoutDetailsPresenter(this);
        mPresenter.getWorkoutDetails(workoutId);
    }

    @Override
    public void setupFirebaseAdapter(DatabaseReference exercises) {
        mAdapter = new WorkoutExerciseListAdapter(Exercise.class, R.layout.exercise_list_item, FirebaseExerciseViewHolder.class, exercises);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        attachItemTouchHelper(mRecyclerView, mAdapter);
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
