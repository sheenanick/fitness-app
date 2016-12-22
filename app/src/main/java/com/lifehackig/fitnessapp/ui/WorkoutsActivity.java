package com.lifehackig.fitnessapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.adapters.FirebaseWorkoutViewHolder;
import com.lifehackig.fitnessapp.models.Workout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkoutsActivity extends AppCompatActivity {
    @Bind(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.emptyView) TextView mEmptyView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mWorkouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    mWorkouts = FirebaseDatabase.getInstance().getReference("workouts").child(uid);
                    mWorkouts.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            } else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mEmptyView.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    setupFirebaseAdapter();
                }
            }
        };

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

    private void setupFirebaseAdapter() {
        mAdapter = new FirebaseRecyclerAdapter<Workout, FirebaseWorkoutViewHolder>(Workout.class, R.layout.workout_list_item, FirebaseWorkoutViewHolder.class, mWorkouts) {
            @Override
            protected void populateViewHolder(FirebaseWorkoutViewHolder viewHolder, Workout model, int position) {
                viewHolder.bindWorkout(model);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
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

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(WorkoutsActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

}
