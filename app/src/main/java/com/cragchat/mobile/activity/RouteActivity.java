package com.cragchat.mobile.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.android.CircleImageView;
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
            route = Displayable.decodeRoute(
                    new JSONObject(intent.getStringExtra(CragChatActivity.DATA_STRING)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_button);

        ImageView routeImage = findViewById(R.id.route_image);
        routeImage.setImageResource(route.getType().equalsIgnoreCase("sport") ?
                R.drawable.bolt_img : R.drawable.nuts);

        final RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(this,
                getSupportFragmentManager(), appBarLayout, floatingActionButton, route.getId());
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = pageAdapter.getItem(pager.getCurrentItem());
                if (fragment instanceof View.OnClickListener) {
                    View.OnClickListener clickListener = (View.OnClickListener) fragment;
                    clickListener.onClick(view);
                }
            }
        });

        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(tabInd);
        pager.addOnPageChangeListener(pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);
    }

    @Override
    public Displayable getDisplayable() {
        return route;
    }

    public void onClick(View v) {
        openDisplayable(v);
    }

}
