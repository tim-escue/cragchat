package com.cragchat.mobile.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cragchat.mobile.R;
import com.cragchat.mobile.database.RelmTest;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.user.User;
import com.cragchat.mobile.view.adapters.pager.AreaActivityPagerAdapter;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;

import io.realm.ObjectServerError;
import io.realm.PermissionManager;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import io.realm.permissions.AccessLevel;
import io.realm.permissions.PermissionRequest;
import io.realm.permissions.UserCondition;

import static io.realm.ErrorCode.INVALID_CREDENTIALS;
import static io.realm.ErrorCode.UNKNOWN_ACCOUNT;

public class AreaActivity extends NavigableActivity {

    private Area area;

    private FloatingActionButton floatingActionButton;

    private SyncUser user;
    String authUrl = "http://ec2-52-34-138-217.us-west-2.compute.amazonaws.com:9080/auth";
    String realmUrl = "realm://ec2-52-34-138-217.us-west-2.compute.amazonaws.com:9080/crags";

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        addContent(R.layout.activity_displayable_new);

        floatingActionButton = findViewById(R.id.add_button);

        area = Displayable.decodeAreaString(getIntent().getStringExtra(CragChatActivity.DATA_STRING));
        area.loadStatistics(this);

        Realm.init(AreaActivity.this);

        new LoginTask().execute();


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setupToolbar();

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);

        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this,
                getSupportFragmentManager(), appBarLayout, area, area.getSubAreas(), area.getRoutes(), floatingActionButton);

        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(pageAdapter);
        pager.setCurrentItem(getInitialTabIndex());
        setAddButtonPagerAndAdapter(pager, pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);


    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class LoginTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            SyncCredentials myCredentials = SyncCredentials.usernamePassword("timsqdev@gmail.com", "88k88k88k", false);

            user = SyncUser.login(myCredentials, authUrl);

            return null;
        }


        protected void onPostExecute(String feed) {
            user.getPermissionManager().applyPermissions(new PermissionRequest(UserCondition.noExistingPermissions(), realmUrl, AccessLevel.WRITE), new PermissionManager.ApplyPermissionsCallback() {
                @Override
                public void onSuccess() {
                    Log.d("PERMISSION CHANGE", "updated");
                    new RealmTask().execute();
                }

                @Override
                public void onError(ObjectServerError objectServerError) {

                }
            });

        }

    }
    class RealmTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            SyncConfiguration config = new SyncConfiguration.Builder(user, realmUrl)
                    .build();
            Realm.setDefaultConfiguration(config);
            return null;
        }


        protected void onPostExecute(String feed) {
            Realm realm = Realm.getDefaultInstance();
           final RealmResults<RelmTest> results = realm.where(RelmTest.class).findAll();
            Log.d("resultsrealm", results.size()+ " IS SIZE");

            for (RelmTest i : results) {
                Log.d("name", i.getName());
            }

            realm.close();
        }

    }

    class RealmTaskTwo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }


        protected void onPostExecute(String feed) {

        }

    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(area.getName());
        getSupportActionBar().setSubtitle(area.getSubTitle(this));

    }

    public void setAddButtonPagerAndAdapter(final ViewPager pager, final TabPagerAdapter pageAdapter) {
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
    }

    public void onClick(View v) {
        openDisplayable(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Displayable parent = LocalDatabase.getInstance(this).getParent(area);
            if (parent != null) {
                launch(parent);
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}