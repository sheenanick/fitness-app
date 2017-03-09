package com.lifehackig.fitnessapp.ui.workouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseWorkoutViewHolder;
import com.lifehackig.fitnessapp.models.Workout;
import com.lifehackig.fitnessapp.ui.account.AccountActivity;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkoutsActivity extends BaseActivity implements WorkoutsContract.MvpView {
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.emptyView) TextView mEmptyView;

    private WorkoutsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        ButterKnife.bind(this);

        setAppBarTitle(getString(R.string.my_workouts));
        initBottomNav();
        setBottomNavChecked();

        mPresenter = new WorkoutsPresenter(this);
        mPresenter.getWorkouts();
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
                        Intent homeIntent = new Intent(WorkoutsActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.action_account:
                        Intent accountIntent = new Intent(WorkoutsActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });
    }

    private void setBottomNavChecked() {
        mBottomNavigationView.getMenu().getItem(1).setChecked(true);
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
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
