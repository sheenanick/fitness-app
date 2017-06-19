package com.lifehackig.fitnessapp.ui.new_exercise;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.models.Exercise;
import com.lifehackig.fitnessapp.util.UserManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sheena on 3/9/17.
 */

public class NewExercisePresenter implements NewExerciseContract.Presenter {
    private NewExerciseContract.MvpView mView;
    private FirebaseDatabase mDatabase;
    private String mCurrentUid;
    private DatabaseReference mDateRef;
    private DatabaseReference mDateCaloriesRef;
    private int mTotalCalories;

    public NewExercisePresenter(NewExerciseContract.MvpView view) {
        mView = view;
        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUid = UserManager.getCurrentUser().getUid();
    }

    @Override
    public void saveExercise(String name, int reps, int minutes, int intWeight, String muscle, int intCalories, Date date) {
        DateFormat refIdFormatter = new SimpleDateFormat("MMddyyyy", Locale.US);
        String stringDate = refIdFormatter.format(date);
        mDateRef = mDatabase.getReference("members").child(mCurrentUid).child("days").child(stringDate);

        DatabaseReference exerciseRef = mDateRef.child("exercises").push();
        String pushId = exerciseRef.getKey();
        Exercise exercise = new Exercise(name, reps, minutes, intWeight, muscle, intCalories, pushId);
        exerciseRef.setValue(exercise);

        mDateRef.child("date").setValue(stringDate);

        updateCalories(intCalories);
        mView.navigateToDayActivity();
    }

    private void updateCalories(final Integer exerciseCalories) {
        mDateCaloriesRef = mDateRef.child("calories");
        mDateCaloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mTotalCalories = 0;
                } else {
                    mTotalCalories = Integer.parseInt(dataSnapshot.getValue().toString());
                }
                int newTotalCalories = mTotalCalories + exerciseCalories;
                mDateCaloriesRef.setValue(newTotalCalories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void detach() {
        mView = null;
        mDatabase = null;
        mDateRef = null;
        mDateCaloriesRef = null;
    }
}
