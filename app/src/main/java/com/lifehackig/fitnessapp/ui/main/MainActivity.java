package com.lifehackig.fitnessapp.ui.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

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

public class MainActivity extends BaseActivity implements MainContract.MvpView{
    private MainPresenter mPresenter;
    private CaldroidFragment mCaldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);
        initCaldroidFragment();
        setBottomNavChecked(0);
    }

    private void initCaldroidFragment() {
        mCaldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidCustomTheme);
        mCaldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, mCaldroidFragment);
        t.commit();

        Drawable border = getResources().getDrawable(R.drawable.today_border, null);
        mCaldroidFragment.setBackgroundDrawableForDate(border, new Date());
        mCaldroidFragment.refreshView();

        setCaldroidListener();
    }

    private void setCaldroidListener() {
        mCaldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                mCaldroidFragment.clearSelectedDates();
                mCaldroidFragment.setSelectedDates(date, date);
                mCaldroidFragment.refreshView();

                Intent intent = new Intent(MainActivity.this, DayActivity.class);
                intent.putExtra("date", date.getTime());
                startActivity(intent);
            }
            @Override
            public void onCaldroidViewCreated() {
                Button leftButton = mCaldroidFragment.getLeftArrowButton();
                Button rightButton = mCaldroidFragment.getRightArrowButton();
                leftButton.setBackground(getResources().getDrawable(R.drawable.left, null));
                rightButton.setBackground(getResources().getDrawable(R.drawable.right, null));

                mPresenter.getExercisedDays();
            }
        });
    }

    @Override
    public void setCalendarBackgroundColors(DataSnapshot dataSnapshot) {
        ColorDrawable highlight = new ColorDrawable(getResources().getColor(R.color.colorAccent));
        for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
            Object day = daySnapshot.child("date").getValue();
            if (day != null) {
                String stringDate = day.toString();
                try {
                    DateFormat dateFormatter = new SimpleDateFormat("MMddyyyy", Locale.US);
                    Date date = dateFormatter.parse(stringDate);
                    if (dateFormatter.format(date).equals(dateFormatter.format(new Date()))) {
                        Drawable today = getResources().getDrawable(R.drawable.today_background, null);
                        mCaldroidFragment.setBackgroundDrawableForDate(today, date);
                    } else {
                        mCaldroidFragment.setBackgroundDrawableForDate(highlight, date);
                    }
                    mCaldroidFragment.refreshView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
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
