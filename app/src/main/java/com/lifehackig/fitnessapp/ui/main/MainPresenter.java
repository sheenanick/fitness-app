package com.lifehackig.fitnessapp.ui.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.util.UserManager;

/**
 * Created by Sheena on 3/6/17.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.MvpView mView;
    private DatabaseReference mMemberRef;

    public MainPresenter(MainContract.MvpView view) {
        mView = view;
    }

    @Override
    public void getExercisedDays() {
        String userId = UserManager.getCurrentUser().getUid();
        mMemberRef = FirebaseDatabase.getInstance().getReference("members").child(userId).child("days");
        mMemberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mView.setCalendarBackgroundColors(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void detach() {
        mView = null;
        mMemberRef = null;
    }
}
