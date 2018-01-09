package com.cragchat.mobile.ui.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.presenter.RouteActivityPresenter;
import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.util.NavigationUtil;

public class RouteActivity extends SearchableActivity {

    private Route route;
    private RouteActivityPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        route = (Route) getIntent().getParcelableExtra(NavigationUtil.ENTITY);

        setContentView(R.layout.activity_route_new);

        final int initialTab = getIntent().getIntExtra("TAB", 0);

        presenter = new RouteActivityPresenter(this, route.getKey(), initialTab);

        Repository.getRoute(route.getKey(), new Callback<Route>() {
            @Override
            public void onSuccess(Route object) {
                presenter.present(object);
                route = object;
            }

            @Override
            public void onFailure() {

            }
        });

        presenter.present(route);
    }

    public int getTabForCommentTable(String table) {
        int tab = 0;
        if (table.equals(CommentSectionFragment.TABLE_BETA)) {
            tab = 0;
        } else if (table.equals(CommentSectionFragment.TABLE_LOCATION)) {
            tab = 3;
        }
        return tab;
    }

    public void switchTab(int tab) {
        presenter.switchTab(tab);
    }

    @Override
    int getToolbarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavigationUtil.launch(this, Repository.getArea(route.getParent(), null));
        }
        return super.onOptionsItemSelected(item);
    }

}
