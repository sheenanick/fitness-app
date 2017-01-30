package com.lifehackig.fitnessapp.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Sheena on 1/29/17.
 */

public class UserManager {
    private static FirebaseUser currentUser;

    public static void logoutActiveUser() {
        FirebaseAuth.getInstance().signOut();
        currentUser = null;
    }

    public static void setCurrentUser(FirebaseUser user) {
        currentUser = user;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }
}
