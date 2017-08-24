package com.cragchat.mobile.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.search.SearchableActivity;
import com.cragchat.mobile.sql.LoginTask;
import com.cragchat.mobile.sql.SendResetTask;

public class LoginActivity extends SearchableActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);

    }

    public void auth(View v) {
        if (hasConnection()) {
            String username = ((EditText) findViewById(R.id.login_username)).getText().toString();
            String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
            new LoginTask(this, username, password).execute();
        } else {
            Toast.makeText(this, "Must have data connection to log in", Toast.LENGTH_LONG).show();
        }
    }

    public void register(View v) {
        if (hasConnection()) {
            Intent reg = new Intent(this, RegisterActivity.class);
        startActivity(reg);
        } else {
            Toast.makeText(this, "Must have data connection to register", Toast.LENGTH_LONG).show();
        }
    }

    static String email;

    public void requestReset(View v) {

        if (hasConnection()) {
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
                    new SendResetTask(act, email).execute();
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

}

