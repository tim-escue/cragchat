package chat.crag.cragchat;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
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

        area = Displayable.decodeAreaString(getIntent().getStringExtra(CragChatActivity.DATA_STRING));

        setContentView(R.layout.activity_area);

        TextView textView = (TextView) findViewById(R.id.wall_display_name);
        if (area.getName().length() >= 16) {
            textView.setTextSize(28);
        } else if (area.getName().length() >= 19) {
            textView.setTextSize(10);
        }
        textView.setText(area.getName());

        Area[] hierarchy = LocalDatabase.getInstance(this).getHierarchy(area);
        if (hierarchy.length != 0) {
            textView = (TextView) findViewById(R.id.crag_name1);
            SpannableString content = new SpannableString(hierarchy[0].getName());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);
            textView.setVisibility(View.VISIBLE);

            for (int i = 1 ; i < hierarchy.length; i++) {
                textView = (TextView) findViewById(getResources().getIdentifier("crag_name" + (i+1), "id", getPackageName()));

                content = new SpannableString("->"+hierarchy[i].getName());
                content.setSpan(new UnderlineSpan(), 2, content.length(), 0);
                textView.setText(content);

                textView.setVisibility(View.VISIBLE);

            }
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