package com.lifehackig.fitnessapp.ui.workouts;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.util.UserManager;

/**
 * Created by Sheena on 3/9/17.
 */

public class WorkoutsPresenter implements WorkoutsContract.Presenter {
    private WorkoutsContract.MvpView mView;

    public WorkoutsPresenter(WorkoutsContract.MvpView view) {
        mView = view;
    }

    @Override
    public void getWorkouts() {
        FirebaseUser user = UserManager.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference workouts = FirebaseDatabase.getInstance().getReference("workouts").child(uid);
            workouts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        mView.showEmptyView();
                    } else {
                        mView.hideEmptyView();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            mView.setupFirebaseAdapter(workouts);
        }
    }

    @Override
    public void detach() {
        mView = null;
    }
}
