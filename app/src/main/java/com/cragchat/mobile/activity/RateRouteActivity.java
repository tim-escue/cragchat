package com.cragchat.mobile.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.sql.SendRatingTask;
import com.cragchat.mobile.user.User;

public class RateRouteActivity extends SearchActivity {

    private int id;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_rate_route);
        id = getIntent().getIntExtra("id", -1);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_rate_yds);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.yds_options, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner = (Spinner) findViewById(R.id.spinner_rate_stars);
        adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.star_options, android.R.layout.simple_spinner_item);
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

    }

    public void rate(View v) {
        Spinner yds = ((Spinner) findViewById(R.id.spinner_rate_yds));
        int stars = Integer.parseInt(((Spinner) findViewById(R.id.spinner_rate_stars)).getSelectedItem().toString());
//        String climbStyle = ((Spinner) findViewById(R.id.spinner_select_style)).getSelectedItem().toString();
        //      String sendStyle = ((Spinner) findViewById(R.id.spinner_select_send_typee)).getSelectedItem().toString();
        //     int pitches = Integer.parseInt(((EditText)findViewById(R.id.edittext_pitches)).getText().toString());
        //    int attempts = Integer.parseInt(((EditText)findViewById(R.id.edittext_attempts)).getText().toString());

        //String timeString = ((EditText)findViewById(R.id.edittext_time)).getText().toString();
        // String[] args = timeString.split(":");
        int timeSeconds = 0;
        /*if (args.length== 0 || args.length > 3) {
            Toast.makeText(this, "Time to climb is not formatted correctly", Toast.LENGTH_LONG).show();
            return;
            } else {
                timeSeconds = Integer.parseInt(args[0]) * 3600 + Integer.parseInt(args[1]) * 60 + Integer.parseInt(args[2]);
            }*/


        if (hasConnection()) {
            new SendRatingTask(this, id, yds.getSelectedItemPosition(), stars, User.currentToken(this), true).execute();
        } else {
            Toast.makeText(this, "Unable to post rating - will try again later", Toast.LENGTH_LONG).show();
           /* String store = "RATING###" + id + "###" + yds.getSelectedItemPosition() + "###" + stars + "###" + climbStyle + "###" + pitches + "###" + timeSeconds + "###" + User.currentToken(this)
                    + "###" +sendStyle + "###" +attempts;
            LocalDatabase.getInstance(this).store(this, store);*/
        }

    }


}

