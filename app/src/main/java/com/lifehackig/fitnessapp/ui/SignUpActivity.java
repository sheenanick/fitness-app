package com.lifehackig.fitnessapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.lifehackig.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.name) EditText mName;
    @Bind(R.id.email) EditText mEmail;
    @Bind(R.id.password) EditText mPassword;
    @Bind(R.id.confirmPassword) EditText mConfirmPassword;
    @Bind(R.id.signUpButton) Button mSignUpButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog mAuthProgressDialog;
    private String mNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mSignUpButton.setOnClickListener(this);

        createProgressDialog();
        createAuthStateListener();
    }

    private void createProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    private void createAuthStateListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == mSignUpButton) {
            createNewUser();
        }
    }

    private void createNewUser() {
        mNameString = mName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();

        boolean validName = isValidName(mNameString);
        boolean validEmail = isValidEmail(email);
        boolean validPassword = isValidPassword(password, confirmPassword);

        if (!validName || !validEmail || !validPassword) return;

        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                mAuthProgressDialog.dismiss();

                if (task.isSuccessful()) {
                    addNameToFirebaseProfile(task.getResult().getUser());
                } else {
                    Toast.makeText(SignUpActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValidName(String name) {
        if (name.equals("")) {
            mName.setError("Please enter your name");
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

    public void addNameToFirebaseProfile(FirebaseUser user) {
        UserProfileChangeRequest addName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mNameString).build();
        user.updateProfile(addName);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
