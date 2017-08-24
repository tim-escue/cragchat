package com.cragchat.mobile.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.*;

import com.cragchat.mobile.R;
import com.cragchat.mobile.search.SearchableActivity;
import com.cragchat.mobile.sql.SendSendTask;
import com.cragchat.mobile.user.User;

public class SubmitSendActivity extends SearchableActivity {

    private int id;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_submit_send);
        id = getIntent().getIntExtra("id", -1);

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
                    SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
                    for (int i = end - 1; i >= start; i--) {
                        char currentChar = source.charAt(i);
                        if (!Character.isLetterOrDigit(currentChar) && !Character.isSpaceChar(currentChar)) {
                            sourceAsSpannableBuilder.delete(i, i+1);
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
        edit.setFilters(new InputFilter[] {digitFilter});
        edit = (EditText) findViewById(R.id.edittext_pitches);
        edit.setFilters(new InputFilter[] {digitFilter});

    }

    public void submit(View v) {
        String climbStyle = ((Spinner) findViewById(R.id.spinner_select_style)).getSelectedItem().toString();
        String sendStyle = ((Spinner) findViewById(R.id.spinner_select_send_typee)).getSelectedItem().toString();
        int pitches = Integer.parseInt(((EditText)findViewById(R.id.edittext_pitches)).getText().toString());
        int attempts = Integer.parseInt(((EditText)findViewById(R.id.edittext_attempts)).getText().toString());

        if (hasConnection()) {
            new SendSendTask(this, id, attempts, pitches, climbStyle, sendStyle, User.currentToken(this), true).execute();
        } else {
           /* Toast.makeText(this, "Unable to post rating - will try again later", Toast.LENGTH_LONG).show();
            String store = "RATING###" + id + "###" + yds.getSelectedItemPosition() + "###" + stars + "###" + climbStyle + "###" + pitches + "###" + timeSeconds + "###" + User.currentToken(this)
                    + "###" +sendStyle + "###" +attempts;
            LocalDatabase.getInstance(this).store(this, store);*/
        }

    }


}


