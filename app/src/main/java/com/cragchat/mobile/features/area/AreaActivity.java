package com.cragchat.mobile.features.area;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.presenter.AreaActivityPresenter;
import com.cragchat.mobile.ui.view.activity.MainActivity;
import com.cragchat.mobile.ui.view.activity.SearchableActivity;
import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.util.NavigationUtil;

import javax.inject.Inject;

public class AreaActivity extends SearchableActivity {

    @Inject
    Area area;

    private AreaActivityPresenter presenter;

    @Inject
    AreaListFragment areaListFragment;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_displayable_new);
        presenter = new AreaActivityPresenter(this, areaListFragment, area);
        presenter.present(area, repository);

        repository.getArea(area.getKey(), new Callback<Area>() {
            @Override
            public void onSuccess(Area object) {
                presenter.present(object, repository);
                area = object;
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public int getTabForCommentTable(String commentTable) {
        int tab = 0;
        if (area.getSubAreas().size() == 0) {
            if (commentTable.equals(CommentSectionFragment.TABLE_DISCUSSION)) {
                tab = 2;
            } else if (commentTable.equals(CommentSectionFragment.TABLE_LOCATION)) {
                tab = 3;
            }
        } else {
            if (commentTable.equals(CommentSectionFragment.TABLE_DISCUSSION)) {
                tab = 3;
            } else if (commentTable.equals(CommentSectionFragment.TABLE_LOCATION)) {
                tab = 4;
            }
        }
        return tab;
    }

    public void switchTab(int tab) {
        presenter.switchTab(tab);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    @Override
    public int getToolbarColor() {
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
            Area parent = repository.getArea(area.getParent(), null);
            if (parent != null) {
                NavigationUtil.launch(this, parent);
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }


}