package com.cragchat.mobile.mvp.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.authentication.AuthenticatedUser;
import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.data.authentication.AuthenticationCallback;
import com.cragchat.mobile.data.util.NetworkUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends CragChatActivity {

    @BindView(R.id.username_input)
    TextInputEditText usernameInput;
    @BindView(R.id.password_input)
    TextInputEditText passwordInput;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    Authentication authentication;

    @OnClick(R.id.login_button)
    void login(final View view) {
        authentication.login(view.getContext(), usernameInput.getText().toString(),
                passwordInput.getText().toString(), new AuthenticationCallback() {
                    @Override
                    public void onAuthenticateSuccess(AuthenticatedUser user) {
                        String str = "Logged in as " + user.getName();
                        Toast.makeText(view.getContext(), str, Toast.LENGTH_SHORT).show();
                        view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class));
                    }

                    @Override
                    public void onAuthenticateFailed(String message) {
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                        passwordInput.setText("");
                    }
                });
    }

    @OnClick(R.id.register_button)
    void register(View view) {
        if (NetworkUtil.isConnected(view.getContext())) {
            view.getContext().startActivity(new Intent(view.getContext(), RegisterActivity.class));
        } else {
            Toast.makeText(view.getContext(), "Must have data connection to register", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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
}

