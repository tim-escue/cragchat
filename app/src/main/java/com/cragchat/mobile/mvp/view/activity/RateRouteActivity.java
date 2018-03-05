package com.cragchat.mobile.mvp.view.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cragchat.mobile.R;
import com.cragchat.mobile.domain.model.Route;
import com.cragchat.mobile.util.ViewUtil;

public class RateRouteActivity extends CragChatActivity {

    private String entityKey;
    private Route entity;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_rate_route);
        entityKey = getIntent().getStringExtra("entityKey");
        entity = repository.getRoute(entityKey);

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
        Spinner ydsSpinner = findViewById(R.id.spinner_rate_yds);
        int stars = Integer.parseInt(((Spinner) findViewById(R.id.spinner_rate_stars)).getSelectedItem().toString());
        int yds = ydsSpinner.getSelectedItemPosition();

        repository.addRating(
                authentication.getAuthenticatedUser(this).getToken(),
                stars,
                yds,
                entityKey,
                entity.getName(),
                null
        );

        ViewUtil.launch(this, entity);
    }


}

