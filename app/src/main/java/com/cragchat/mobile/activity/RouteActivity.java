package com.cragchat.mobile.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.user.User;

import org.json.JSONObject;

public class RouteActivity extends DisplayableActivity {

    private Route route;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_route);

        Intent intent = getIntent();
        route = null;
        try {
            route = Displayable.decodeRoute(new JSONObject(intent.getStringExtra(CragChatActivity.DATA_STRING)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportActionBar().setTitle(route.getName());
        getSupportActionBar().setSubtitle(route.getSubTitle(this));

        TextView textView = (TextView) findViewById(R.id.type);
        textView.setText(route.getType());

        textView = (TextView) findViewById(R.id.yds_scale);
        String yds = route.getYdsString(this, route.getYds(this));
        textView.setText(yds);

        textView = (TextView) findViewById(R.id.stars);
        String sters = route.getStarsString(this);
        textView.setText(sters);

        int tabInd = intent.getIntExtra("TAB", 0);
        RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(this, getSupportFragmentManager(), route.getId());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(tabInd);

        TabLayout slab = (TabLayout) findViewById(R.id.sliding_tabs);
        slab.setupWithViewPager(pager);
    }

    @Override
    public Displayable getDisplayable() {
        return route;
    }

    public void rate(View v) {
        if (User.currentToken(this) != null) {
            Intent next = new Intent(this, RateRouteActivity.class);
            next.putExtra("id", route.getId());
            startActivity(next);
        } else {
            Toast.makeText(this, "Must be logged in to rate climb", Toast.LENGTH_SHORT).show();
        }
    }

    public void submitSend(View v) {
        if (User.currentToken(this) != null) {
            Intent next = new Intent(this, SubmitSendActivity.class);
            next.putExtra("id", route.getId());
            startActivity(next);
        } else {
            Toast.makeText(this, "Must be logged in to submit send", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View v) {
        openDisplayable(v);
    }

}
