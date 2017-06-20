package com.lifehackig.fitnessapp.adapters;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lifehackig.fitnessapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheena on 6/19/17.
 */

public class WorkoutExerciseListAdapter extends FirebaseRecyclerAdapter<Exercise, FirebaseExerciseViewHolder> implements FirebaseListAdapterInterface {
    private DatabaseReference mExercisesRef;
    private List<Exercise> mExercises;

    public WorkoutExerciseListAdapter(Class<Exercise> modelClass, int modelLayout, Class<FirebaseExerciseViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mExercisesRef = ref;
        mExercises = new ArrayList<>();
        mExercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot exerciseSnapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = exerciseSnapshot.getValue(Exercise.class);
                    mExercises.add(exercise);
                }
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

    @Override
    public void onItemDismiss(int position) {
        mExercises.remove(position);
        mExercisesRef.setValue(mExercises);
    }
}