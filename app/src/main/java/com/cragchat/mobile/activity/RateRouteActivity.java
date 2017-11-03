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
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.database.models.RealmRating;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.networkapi.ErrorHandlingObserverable;
import com.cragchat.networkapi.NetworkApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class RateRouteActivity extends CragChatActivity {

    private String entityKey;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_rate_route);
        entityKey = getIntent().getStringExtra("entityKey");

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
        Spinner ydsSpinner = ((Spinner) findViewById(R.id.spinner_rate_yds));
        int stars = Integer.parseInt(((Spinner) findViewById(R.id.spinner_rate_stars)).getSelectedItem().toString());
        int yds = ydsSpinner.getSelectedItemPosition();

        if (NetworkApi.isConnected(this)) {
            NetworkApi.getInstance().postRating(
                    Authentication.getAuthenticatedUser(this).getToken(),
                    stars,
                    yds,
                    entityKey
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorHandlingObserverable<RealmRating>() {
                        @Override
                        public void onSuccess(final RealmRating object) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(object);
                                }
                            });
                            realm.close();
                        }
                    });
        } else {
            Toast.makeText(this, "Unable to post rating - will try again later", Toast.LENGTH_LONG).show();
        }
        Realm realm = Realm.getDefaultInstance();
        RealmRoute route = realm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, entityKey).findFirst();
        realm.close();
        launch(route);
    }


}

