package com.cragchat.mobile.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.NavigationUtil;

public class SubmitSendActivity extends CragChatActivity {

    private String entityKey;
    private String entityName;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_submit_send);
        entityKey = getIntent().getStringExtra("entityKey");
        entityName = Repository.getRoute(entityKey, null).getName();

        Spinner spinner = (Spinner) findViewById(R.id.spinner_select_style);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.spinner_options_climbstyle, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner = (Spinner) findViewById(R.id.spinner_select_send_typee);
        adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.spinner_select_send_type, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        InputFilter digitFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source instanceof SpannableStringBuilder) {
                    SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder) source;
                    for (int i = end - 1; i >= start; i--) {
                        char currentChar = source.charAt(i);
                        if (!Character.isLetterOrDigit(currentChar) && !Character.isSpaceChar(currentChar)) {
                            sourceAsSpannableBuilder.delete(i, i + 1);
                        }
                    }
                    return source;
                } else {
                    StringBuilder filteredStringBuilder = new StringBuilder();
                    for (int i = start; i < end; i++) {
                        char currentChar = source.charAt(i);
                        if (Character.isLetterOrDigit(currentChar) || Character.isSpaceChar(currentChar)) {
                            filteredStringBuilder.append(currentChar);
                        }
                    }
                    return filteredStringBuilder.toString();
                }
            }
        };
        EditText edit = (EditText) findViewById(R.id.edittext_attempts);
        edit.setFilters(new InputFilter[]{digitFilter});
        edit = (EditText) findViewById(R.id.edittext_pitches);
        edit.setFilters(new InputFilter[]{digitFilter});

    }

    public void submit(View v) {
        String climbStyle = ((Spinner) findViewById(R.id.spinner_select_style)).getSelectedItem().toString();
        String sendStyle = ((Spinner) findViewById(R.id.spinner_select_send_typee)).getSelectedItem().toString();
        int pitches;
        int attempts;
        try {
            pitches = Integer.parseInt(((EditText) findViewById(R.id.edittext_pitches)).getText().toString());
            attempts = Integer.parseInt(((EditText) findViewById(R.id.edittext_attempts)).getText().toString());
        } catch (NumberFormatException exception) {
            Toast.makeText(this, "Pitches or attempts too large", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pitches < 1 || attempts < 1) {
            Toast.makeText(this, "Pitches and attempts must both be greater than 1", Toast.LENGTH_SHORT).show();
            return;
        }

        Repository.addSend(Authentication.getAuthenticatedUser(this).getToken(),
                entityKey,
                pitches,
                attempts,
                sendStyle,
                climbStyle,
                entityName,
                null);


        NavigationUtil.launch(this, Repository.getRoute(entityKey, null));
    }


}


