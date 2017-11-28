package com.cragchat.mobile.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.authentication.AuthenticationCallback;
import com.cragchat.mobile.fragments.NotificationDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends CragChatActivity {

    @BindView(R.id.register_password)
    EditText editPassword;
    @BindView(R.id.register_username)
    EditText editUsername;
    @BindView(R.id.register_password_verify)
    EditText editPasswordVerify;
    @BindView(R.id.register_email)
    EditText editEmail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupActionBar();
    }

    protected void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    public void register(View v) {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        if (password.equals(editPasswordVerify.getText().toString().trim()) && !email.isEmpty()) {
            Authentication.register(this, username, password, email, true, new AuthenticationCallback() {
                @Override
                public void onAuthenticateSuccess(AuthenticatedUser justLoggedIn) {
                    String str = "Logged in an registered as " + justLoggedIn.getName();
                    Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }

                @Override
                public void onAuthenticateFailed(String message) {
                    notifyError(message);
                }
            });
        } else if (email.isEmpty()) {
            DialogFragment fragment = NotificationDialog.newInstance("Email cannot be empty! It is " +
                    "only used to reset your password in case you forget it. " +
                    "It will never be shared or spammed by us.");
            fragment.show(getSupportFragmentManager(), "dialog");
        } else {
            DialogFragment fragment = NotificationDialog.newInstance("Passwords did not match. Please try again.");
            fragment.show(getSupportFragmentManager(), "dialog");
            editPassword.setText("");
            editPasswordVerify.setText("");
        }
    }

    private void notifyError(String error) {
        Toast.makeText(RegisterActivity.this, "Error:" + error, Toast.LENGTH_LONG).show();
        editPasswordVerify.setText(null);
    }

}

