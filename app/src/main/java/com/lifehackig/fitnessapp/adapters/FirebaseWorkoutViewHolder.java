package com.lifehackig.fitnessapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.models.Workout;


public class FirebaseWorkoutViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private Context mContext;

    public FirebaseWorkoutViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindWorkout(Workout workout) {
        TextView workoutName = (TextView) mView.findViewById(R.id.workoutName);
        workoutName.setText(workout.getName());
    }
}
