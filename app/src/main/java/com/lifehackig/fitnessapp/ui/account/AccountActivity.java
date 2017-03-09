package com.lifehackig.fitnessapp.ui.account;

import android.os.Bundle;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setAppBarTitle("Account Settings");
        setBottomNavChecked(2);
    }
}
