package com.lifehackig.fitnessapp.adapters;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.models.Exercise;

import java.util.ArrayList;

public class FirebaseExerciseListAdapter extends FirebaseRecyclerAdapter<Exercise, FirebaseExerciseViewHolder> {
    private DatabaseReference mRef;
    private DatabaseReference mCaloriesRef;
    private DatabaseReference mDateRef;
    private ChildEventListener mChildEventListener;
    private ArrayList<Exercise> mExercises;
    private Integer mCalories;

    public FirebaseExerciseListAdapter(Class<Exercise> modelClass, int modelLayout, Class<FirebaseExerciseViewHolder> viewHolderClass, DatabaseReference ref, DatabaseReference caloriesRef, DatabaseReference dateRef) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref;
        mCaloriesRef = caloriesRef;
        mDateRef = dateRef;
        mExercises = new ArrayList<>();
        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mExercises.add(dataSnapshot.getValue(Exercise.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void populateViewHolder(FirebaseExerciseViewHolder viewHolder, Exercise model, int position) {
        viewHolder.bindExercise(model);
    }

    public void onItemDismiss(int position) {
        updateCalories(mExercises.get(position));
        getRef(position).removeValue();
        mExercises.remove(position);

        if (mExercises.size() == 0) {
            deleteDayFromFirebase();
        }
    }

    private void updateCalories(final Exercise exercise) {
        mCaloriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCalories = Integer.parseInt(dataSnapshot.getValue().toString());
                mCalories -= exercise.getCalories();
                mCaloriesRef.setValue(mCalories);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteDayFromFirebase() {
        mDateRef.removeValue();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mRef.removeEventListener(mChildEventListener);
    }
}
