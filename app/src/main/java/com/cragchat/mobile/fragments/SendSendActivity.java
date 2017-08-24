package com.cragchat.mobile.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cragchat.mobile.R;

/**
 * Created by tim on 7/31/17.
 */

public class SendSendActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle intent) {
        super.onCreate(intent);

        setContentView(R.layout.activity_rate_route);
/*
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_select_send_typee);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_select_send_type, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner = (Spinner) findViewById(R.id.spinner_select_style);
        adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.spinner_options_climbstyle, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);*

        EditText edit = (EditText) findViewById(R.id.edittext_attempts);
        edit.setFilters(new InputFilter[] {digitFilter});
        edit = (EditText) findViewById(R.id.edittext_pitches);
        edit.setFilters(new InputFilter[] {digitFilter});*/
    }
}
