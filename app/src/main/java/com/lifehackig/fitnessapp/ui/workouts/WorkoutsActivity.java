package com.lifehackig.fitnessapp.ui.workouts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseWorkoutViewHolder;
import com.lifehackig.fitnessapp.models.Workout;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutsActivity extends BaseActivity implements WorkoutsContract.MvpView {
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.emptyView) TextView mEmptyView;

    private WorkoutsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        ButterKnife.bind(this);

        setAppBarTitle(getString(R.string.my_workouts));
        setBottomNavChecked(1);

        mPresenter = new WorkoutsPresenter(this);
        mPresenter.getWorkouts();
    }

    @Override
    public void setupFirebaseAdapter(DatabaseReference workouts) {
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Workout, FirebaseWorkoutViewHolder>(Workout.class, R.layout.workout_list_item, FirebaseWorkoutViewHolder.class, workouts) {
            @Override
            protected void populateViewHolder(FirebaseWorkoutViewHolder viewHolder, Workout model, int position) {
                viewHolder.bindWorkout(model);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
