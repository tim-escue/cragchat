package com.cragchat.mobile.ui.presenter;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.authentication.AuthenticationCallback;
import com.cragchat.mobile.ui.view.activity.MainActivity;
import com.cragchat.mobile.ui.view.activity.RegisterActivity;
import com.cragchat.mobile.ui.view.fragments.NotificationDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivityPresenter {

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

    private RegisterActivity activity;
    private Authentication mAuthentication;

    public RegisterActivityPresenter(RegisterActivity activity, Authentication authentication) {
        this.activity = activity;
        this.mAuthentication = authentication;
        ButterKnife.bind(this, activity);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.register_button)
    public void register(final View v) {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        if (password.equals(editPasswordVerify.getText().toString().trim()) && !email.isEmpty()) {
            mAuthentication.register(v.getContext(), username, password, email, true, new AuthenticationCallback() {
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
            fragment.show(activity.getSupportFragmentManager(), "dialog");
        } else {
            DialogFragment fragment = NotificationDialog.newInstance("Passwords did not match. Please try again.");
            fragment.show(activity.getSupportFragmentManager(), "dialog");
            editPassword.setText("");
            editPasswordVerify.setText("");
        }
    }

}