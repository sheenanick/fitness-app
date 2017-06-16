package com.lifehackig.fitnessapp.util;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Sheena on 1/29/17.
 */

public class UserManager {
    private static FirebaseUser mCurrentUser;

    public static void logoutActiveUser() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        mCurrentUser = null;
    }

    public static void setCurrentUser(FirebaseUser user) {
        mCurrentUser = user;
    }

    public static FirebaseUser getCurrentUser() {
        return mCurrentUser;
    }
}
