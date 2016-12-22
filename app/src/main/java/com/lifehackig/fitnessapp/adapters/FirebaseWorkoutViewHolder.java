package com.lifehackig.fitnessapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.models.Workout;

public class FirebaseWorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private View mView;
    private Context mContext;
    private TextView mDetails;

    public FirebaseWorkoutViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindWorkout(Workout workout) {
        TextView workoutName = (TextView) mView.findViewById(R.id.workoutName);
        mDetails = (TextView) mView.findViewById(R.id.details);

        workoutName.setText(workout.getName());
        mDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mDetails) {
            Toast.makeText(mContext, "Details Coming Soon!", Toast.LENGTH_SHORT).show();
        }
    }
}
