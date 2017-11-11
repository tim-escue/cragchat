package com.cragchat.mobile.activity;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.cragchat.mobile.R;
import com.cragchat.mobile.fragments.NotificationDialog;

public class RegisterActivity extends NavigableActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);

    }

    public void register(View v) {
        EditText edit_pw = ((EditText) findViewById(R.id.register_password));
        EditText edit_un = ((EditText) findViewById(R.id.register_username));
        EditText edit_verify = ((EditText) findViewById(R.id.register_password_verify));
        EditText edit_emai = ((EditText) findViewById(R.id.register_email));
        EditText edit_email_verifyy = ((EditText) findViewById(R.id.register_email_confirm));
        String username = edit_un.getText().toString().trim();
        String password = edit_pw.getText().toString().trim();
        String email = edit_emai.getText().toString().trim();
        String emailCheck = edit_email_verifyy.getText().toString().trim();
        if (password.equals(edit_verify.getText().toString().trim()) && email.equals(emailCheck)) {
            //new RegisterTask(this, username, password, email).execute();
        } else {
            if (!password.equals(edit_verify.getText().toString())) {
                DialogFragment fragment = NotificationDialog.newInstance("Passwords did not match. Please try again.");
                fragment.show(getSupportFragmentManager(), "dialog");
                edit_pw.setText("");
                edit_verify.setText("");
            }
            if (!email.equals(emailCheck)) {
                DialogFragment fragment = NotificationDialog.newInstance("Email did not match. Please try again.");
                fragment.show(getSupportFragmentManager(), "dialog");
                edit_emai.setText("");
                edit_email_verifyy.setText("");
            }
        }
    }


}

