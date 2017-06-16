package com.lifehackig.fitnessapp.ui.signup;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lifehackig.fitnessapp.R;
import com.lifehackig.fitnessapp.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends BaseActivity implements SignUpContract.MvpView, View.OnClickListener{
    @BindView(R.id.firstName) EditText mFirstName;
    @BindView(R.id.lastName) EditText mLastName;
    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.confirmPassword) EditText mConfirmPassword;
    @BindView(R.id.signUpButton) Button mSignUpButton;

    private SignUpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        hideBottomNav();

        mPresenter = new SignUpPresenter(this);

        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSignUpButton) {
            createNewUser();
        }
    }

    private void createNewUser() {
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String fullName = firstName + " " + lastName;
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();

        boolean validFirstName = isValidName(firstName, mFirstName);
        boolean validLastName = isValidName(lastName, mLastName);
        boolean validEmail = isValidEmail(email);
        boolean validPassword = isValidPassword(password, confirmPassword);

        if (validFirstName && validLastName && validEmail && validPassword) {
            mPresenter.createUserWithEmailAndPassword(email, password, fullName);
        }
    }

    public boolean isValidName(String name, View v) {
        if (name.equals("")) {
            if (v == mFirstName) mFirstName.setError("Please enter your first name");
            if (v == mLastName) mLastName.setError("Please enter your last name");
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmail.setError("Please enter a valid email address");
        }
        return isGoodEmail;
    }

    public boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPassword.setError("Passwords must be at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    @Override
    public void displaySignUpError() {
        Toast.makeText(SignUpActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.addAuthStateListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.removeAuthStateListener();
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

}
