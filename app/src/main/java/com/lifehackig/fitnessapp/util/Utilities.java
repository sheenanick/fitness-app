package com.lifehackig.fitnessapp.util;

import android.widget.EditText;

/**
 * Created by Sheena on 5/31/17.
 */

public class Utilities {
    public static boolean isInputEmpty(String value, EditText view) {
        boolean isEmpty = value.equals("");
        if (isEmpty){
            view.setError("This field is required");
        }
        return isEmpty;
    }
}
