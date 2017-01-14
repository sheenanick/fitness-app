package com.lifehackig.fitnessapp.adapters;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.lifehackig.fitnessapp.models.Exercise;

import java.util.ArrayList;

public class FirebaseExerciseListAdapter extends FirebaseRecyclerAdapter<Exercise, FirebaseExerciseViewHolder> {
    private DatabaseReference mRef;
    private ChildEventListener mChildEventListener;
    private ArrayList<Exercise> mExercises;

    public FirebaseExerciseListAdapter(Class<Exercise> modelClass, int modelLayout, Class<FirebaseExerciseViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
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
        mExercises.remove(position);
        getRef(position).removeValue();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mRef.removeEventListener(mChildEventListener);
    }
}
