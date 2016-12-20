package com.lifehackig.fitnessapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.models.Exercise;

import java.util.Locale;

public class FirebaseExerciseViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private Context mContext;

    public FirebaseExerciseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindExercise(Exercise exercise) {
        TextView exerciseName = (TextView) mView.findViewById(R.id.name);
        TextView calories = (TextView) mView.findViewById(R.id.calories);

        exerciseName.setText(exercise.getName());
        calories.setText(String.format(Locale.US, "%d", exercise.getCalories()));
    }
}
