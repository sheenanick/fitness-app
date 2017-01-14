package com.lifehackig.fitnessapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.models.Workout;
import com.lifehackig.fitnessapp.ui.WorkoutDetailsActivity;

public class FirebaseWorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private View mView;
    private Context mContext;
    private TextView mDetails;
    private String mWorkoutId;
    private String mWorkoutName;

    public FirebaseWorkoutViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindWorkout(Workout workout) {
        TextView workoutName = (TextView) mView.findViewById(R.id.workoutName);
        mDetails = (TextView) mView.findViewById(R.id.details);
        mWorkoutId = workout.getPushId();
        mWorkoutName = workout.getName();
        workoutName.setText(workout.getName());
        mDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mDetails) {
            Intent intent = new Intent(mContext, WorkoutDetailsActivity.class);
            intent.putExtra("workoutId", mWorkoutId);
            intent.putExtra("workoutName", mWorkoutName);
            mContext.startActivity(intent);
        }
    }
}
