package com.cragchat.mobile.mvp.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.authentication.AuthenticatedUser;
import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.data.authentication.AuthenticationCallback;
import com.cragchat.mobile.mvp.view.fragments.NotificationDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Inject
    Authentication authentication;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.register_button)
    public void register(final View v) {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        if (password.equals(editPasswordVerify.getText().toString().trim()) && !email.isEmpty()) {
            authentication.register(v.getContext(), username, password, email, true, new AuthenticationCallback() {
                @Override
                public void onAuthenticateSuccess(AuthenticatedUser justLoggedIn) {
                    String str = "Logged in an registered as " + justLoggedIn.getName();
                    Toast.makeText(v.getContext(), str, Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                }

                @Override
                public void onAuthenticateFailed(String message) {
                    Toast.makeText(v.getContext(), "Error:" + message, Toast.LENGTH_LONG).show();
                    editPasswordVerify.setText(null);
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

}

