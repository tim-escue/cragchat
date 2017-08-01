package chat.crag.cragchat;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import chat.crag.cragchat.adapters.AreaActivityPagerAdapter;
import chat.crag.cragchat.android.SlidingTabLayout;
import chat.crag.cragchat.descriptor.Area;
import chat.crag.cragchat.descriptor.Displayable;
import chat.crag.cragchat.search.SearchableActivity;
import chat.crag.cragchat.sql.LocalDatabase;

public class AreaActivity extends SearchableActivity {
    private Area area;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_area);

        area = Displayable.decodeAreaString(getIntent().getStringExtra(CragChatActivity.DATA_STRING));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(area.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Area[] hierarchy = LocalDatabase.getInstance(this).getHierarchy(area);

        LinearLayout navLayout = (LinearLayout) findViewById(R.id.nav_layout);
        for (int i = 0 ; i < hierarchy.length; i++) {
            LinearLayout button = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_nav_button, null);
            if (i == 0) {
                TextView arrow = (TextView) button.findViewById(R.id.text_arrow);
                arrow.setVisibility(View.GONE);
            }
            TextView location = (TextView) button.findViewById(R.id.text_location);
            location.setText(hierarchy[i].getName());
            navLayout.addView(button);
        }


        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this, getSupportFragmentManager(), area);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

        SlidingTabLayout slab = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slab.setViewPager(pager);
    }

    public void onClick(View v) {
        openDisplayable(v);
    }
}