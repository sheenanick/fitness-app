package com.lifehackig.fitnessapp.ui.base;

import android.support.v7.widget.RecyclerView;

import com.lifehackig.fitnessapp.adapters.FirebaseListAdapterInterface;

/**
 * Created by Sheena on 3/6/17.
 */

public interface BaseMvpView {
    boolean displayLoadingAnimation();
    void hideLoadingAnimation();
    void setAppBarTitle(String title);
    void initBottomNav();
    void setBottomNavChecked(int position);
    void hideBottomNav();
    void navigateToMain();
    void attachItemTouchHelper(RecyclerView recyclerView, FirebaseListAdapterInterface adapter);
}
