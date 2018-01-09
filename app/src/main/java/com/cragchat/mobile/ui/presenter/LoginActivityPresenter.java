package com.cragchat.mobile.ui.presenter;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.authentication.AuthenticationCallback;
import com.cragchat.mobile.ui.view.activity.LoginActivity;
import com.cragchat.mobile.ui.view.activity.MainActivity;
import com.cragchat.mobile.ui.view.activity.RegisterActivity;
import com.cragchat.mobile.util.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by timde on 1/4/2018.
 */

public class LoginActivityPresenter {

    @BindView(R.id.username_input)
    TextInputEditText usernameInput;
    @BindView(R.id.password_input)
    TextInputEditText passwordInput;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.login_button)
    void login(final View view) {
        Authentication.login(view.getContext(), usernameInput.getText().toString(),
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

    public LoginActivityPresenter(LoginActivity activity) {
        ButterKnife.bind(this, activity);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

       /* void requestPasswordReset() {
        if (NetworkUtil.isConnected(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Account Email:");

            final Activity act = this;
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

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
    }*/

}
