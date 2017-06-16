package com.lifehackig.fitnessapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.models.Workout;
import com.lifehackig.fitnessapp.ui.workout_details.WorkoutDetailsActivity;

public class FirebaseWorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private View mView;
    private Context mContext;
    private String mWorkoutId;
    private String mWorkoutName;
    private RelativeLayout mWorkoutItemLayout;

    public FirebaseWorkoutViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindWorkout(Workout workout) {
        TextView workoutNameView = (TextView) mView.findViewById(R.id.workoutName);
        mWorkoutItemLayout = (RelativeLayout) mView.findViewById(R.id.workoutItemLayout);

        mWorkoutId = workout.getPushId();
        mWorkoutName = workout.getName();

        workoutNameView.setText(workout.getName());
        mWorkoutItemLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mWorkoutItemLayout) {
            Intent intent = new Intent(mContext, WorkoutDetailsActivity.class);
            intent.putExtra("workoutId", mWorkoutId);
            intent.putExtra("workoutName", mWorkoutName);
            mContext.startActivity(intent);
        }
    }
}
