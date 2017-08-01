package chat.crag.cragchat;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import chat.crag.cragchat.adapters.ProfilePagerAdapter;
import chat.crag.cragchat.android.SlidingTabLayout;
import chat.crag.cragchat.search.SearchableActivity;
import chat.crag.cragchat.user.User;

public class ProfileActivity extends SearchableActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String username = getIntent().getStringExtra("username");

        TextView name = (TextView) findViewById(R.id.profile_username);
        name.setText(username);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        ProfilePagerAdapter pageAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), username);
        pager.setAdapter(pageAdapter);

        SlidingTabLayout slab = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slab.setViewPager(pager);
    }



}


