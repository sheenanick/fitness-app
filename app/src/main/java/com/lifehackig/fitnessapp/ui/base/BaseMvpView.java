package com.lifehackig.fitnessapp.ui.base;

/**
 * Created by Sheena on 3/6/17.
 */

public interface BaseMvpView {
    boolean displayLoadingAnimation();
    void hideLoadingAnimation();
    void navigateToMain();
    void logout();
}
