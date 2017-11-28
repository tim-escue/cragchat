package com.cragchat.mobile.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.authentication.AuthenticationCallback;
import com.cragchat.mobile.network.Network;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends CragChatActivity implements View.OnClickListener {

    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.username_input)
    TextInputEditText usernameInput;
    @BindView(R.id.password_input)
    TextInputEditText passwordInput;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginButton.setOnClickListener(this);

        setupActionBar();
    }

    protected void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void register(View v) {
        if (Network.isConnected(this)) {
            Intent reg = new Intent(this, RegisterActivity.class);
            startActivity(reg);
        } else {
            Toast.makeText(this, "Must have data connection to register", Toast.LENGTH_LONG).show();
        }
    }

    static String email;

    public void requestReset(View v) {

        if (Network.isConnected(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Account Email:");

            final Activity act = this;
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    email = input.getText().toString().trim();
                    ///new SendResetTask(act, email).onSuccess();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button) {
            Authentication.login(this, usernameInput.getText().toString(),
                    passwordInput.getText().toString(), new AuthenticationCallback() {
                        @Override
                        public void onAuthenticateSuccess(AuthenticatedUser user) {
                            String str = "Logged in as " + user.getName();
                            Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onAuthenticateFailed(String message) {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
        } else if (view.getId() == R.id.register_button) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}

