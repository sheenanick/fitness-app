package com.lifehackig.fitnessapp.ui.main;

import com.google.firebase.auth.FirebaseUser;
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
    public void getUser() {
        FirebaseUser user = UserManager.getCurrentUser();
        mMemberRef = FirebaseDatabase.getInstance().getReference("members").child(user.getUid());
        mView.setAppBarTitle(user.getDisplayName());
    }

    @Override
    public void getExercisedDays() {
        mMemberRef.child("days").addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void getCalories(String dateRefId) {
        DatabaseReference caloriesRef = mMemberRef.child("days").child(dateRefId).child("calories");
        caloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalCalories;
                if (dataSnapshot.getValue() == null) {
                    totalCalories = "0";
                } else {
                    totalCalories = dataSnapshot.getValue().toString();
                }
                mView.setCaloriesTextView(totalCalories);
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
