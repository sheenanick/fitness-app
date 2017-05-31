package com.lifehackig.fitnessapp.ui.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;
import com.lifehackig.fitnessapp.ui.day.DayActivity;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainContract.MvpView, View.OnClickListener{
    @Bind(R.id.date) TextView mDateTextView;
    @Bind(R.id.calories) TextView mCalories;
    @Bind(R.id.seeDetailsButton) Button mSeeDetailsButton;

    private MainPresenter mPresenter;
    private CaldroidFragment mCaldroidFragment;

    private Date mDate;
    private DateFormat mRefIdFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new MainPresenter(this);
        mPresenter.getUser();

        mDate = new Date();
        mRefIdFormatter = new SimpleDateFormat("MMddyyyy", Locale.US);

        setDateTextView(mDate);
        getCalories(mDate);

        initCaldroidFragment();
        setBottomNavChecked(0);

        mSeeDetailsButton.setOnClickListener(this);
    }

    private void setDateTextView(Date date) {
        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        mDateTextView.setText(dateFormatter.format(date));
    }

    private void getCalories(Date date) {
        String dateRefId = mRefIdFormatter.format(date);
        mPresenter.getCalories(dateRefId);
    }

    private void initCaldroidFragment() {
        mCaldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        mCaldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, mCaldroidFragment);
        t.commit();

        setCaldroidListener();
    }

    private void setCaldroidListener() {
        mCaldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                mDate = date;

                mCaldroidFragment.clearSelectedDates();
                mCaldroidFragment.setSelectedDates(date, date);
                mCaldroidFragment.refreshView();

                setDateTextView(date);
                getCalories(date);
            }
            @Override
            public void onCaldroidViewCreated() {
                Button leftButton = mCaldroidFragment.getLeftArrowButton();
                Button rightButton = mCaldroidFragment.getRightArrowButton();
                leftButton.setBackground(getResources().getDrawable(R.drawable.left));
                rightButton.setBackground(getResources().getDrawable(R.drawable.right));

                mPresenter.getExercisedDays();
            }
        });
    }

    @Override
    public void setCalendarBackgroundColors(DataSnapshot dataSnapshot) {
        ColorDrawable yellow = new ColorDrawable(getResources().getColor(R.color.colorAccent));
        for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
            Object day = daySnapshot.child("date").getValue();
            if (day != null) {
                String stringDate = day.toString();
                try {
                    Date date = mRefIdFormatter.parse(stringDate);
                    mCaldroidFragment.setBackgroundDrawableForDate(yellow, date);
                    mCaldroidFragment.refreshView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setCaloriesTextView(String totalCalories) {
        mCalories.setText(totalCalories);
    }

    @Override
    public void onClick(View v) {
        if (v == mSeeDetailsButton) {
            Intent intent = new Intent(MainActivity.this, DayActivity.class);
            intent.putExtra("date", mDate.getTime());
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
