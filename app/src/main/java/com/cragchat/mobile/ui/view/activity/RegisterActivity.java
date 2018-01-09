package com.cragchat.mobile.ui.view.activity;


import android.os.Bundle;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.presenter.RegisterActivityPresenter;

public class RegisterActivity extends CragChatActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);
        RegisterActivityPresenter presenter = new RegisterActivityPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

