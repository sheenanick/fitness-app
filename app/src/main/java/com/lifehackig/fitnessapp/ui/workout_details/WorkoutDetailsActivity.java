package com.lifehackig.fitnessapp.ui.workout_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseExerciseViewHolder;
import com.lifehackig.fitnessapp.models.Exercise;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkoutDetailsActivity extends BaseActivity implements WorkoutDetailsContract.MvpView{
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    private FirebaseRecyclerAdapter mAdapter;
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
        mAdapter = new FirebaseRecyclerAdapter<Exercise, FirebaseExerciseViewHolder>(Exercise.class, R.layout.exercise_list_item, FirebaseExerciseViewHolder.class, exercises) {
            @Override
            protected void populateViewHolder(FirebaseExerciseViewHolder viewHolder, Exercise model, int position) {
                viewHolder.bindExercise(model);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
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
