package com.lifehackig.fitnessapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.models.Exercise;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class FirebaseExerciseViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private Context mContext;
    private static final int MAX_WIDTH = 100;
    private static final int MAX_HEIGHT = 100;

    public FirebaseExerciseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindExercise(Exercise exercise) {
        TextView exerciseName = (TextView) mView.findViewById(R.id.name);
        TextView calories = (TextView) mView.findViewById(R.id.calories);

        exerciseName.setText(exercise.getName());
        calories.setText(String.format(Locale.US, "%d Calories", exercise.getCalories()));

        String muscle = exercise.getMuscle();
        setImageView(muscle);

    }

    public void setImageView(String muscle) {
        ImageView imageView = (ImageView)  mView.findViewById(R.id.muscleImage);

        int muscleDrawable;

        if (muscle.equals("Abs")) {
            muscleDrawable = R.drawable.abs;
        } else if (muscle.equals("Arms")) {
            muscleDrawable = R.drawable.arms;
        }else if (muscle.equals("Back")) {
            muscleDrawable = R.drawable.back;
        }else if (muscle.equals("Cardio")) {
            muscleDrawable = R.drawable.cardio;
        }else if (muscle.equals("Chest")) {
            muscleDrawable = R.drawable.chest;
        }else if (muscle.equals("Legs")) {
            muscleDrawable = R.drawable.legs;
        }else if (muscle.equals("Shoulders")) {
            muscleDrawable = R.drawable.shoulders;
        } else {
            muscleDrawable = R.drawable.other;
        }

        Picasso.with(mContext)
                .load(muscleDrawable)
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(imageView);
    }
}
