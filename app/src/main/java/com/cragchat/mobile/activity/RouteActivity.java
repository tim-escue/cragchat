package com.cragchat.mobile.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.RouteActivityPagerAdapter;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.search.SearchableActivity;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.user.User;
import org.json.JSONObject;

public class RouteActivity extends SearchableActivity {

    private Route route;
    private ImageFragment imageFragment;

    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);



        Intent intent = getIntent();
        route = null;
        try {
            route = Displayable.decodeRoute(new JSONObject(intent.getStringExtra(CragChatActivity.DATA_STRING)));
        }catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(route.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int tabInd = intent.getIntExtra("TAB", 0);

        Area[] hierarchy = LocalDatabase.getInstance(this).getHierarchy(route);
        StringBuilder subtitle = new StringBuilder();
        for (int i = 0 ; i < hierarchy.length; i++) {
            Area area = hierarchy[i];
            if (i != 0) {
                subtitle.append(" -> ");
            }
            subtitle.append(area.getName());
        }

        getSupportActionBar().setSubtitle(subtitle);

        /*
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
        }*/

        TextView textView = (TextView) findViewById(R.id.type);
        textView.setText(route.getType());

        textView = (TextView) findViewById(R.id.yds_scale);
        String yds = route.getYdsString(this, route.getYds(this));
        textView.setText(yds);

        textView = (TextView) findViewById(R.id.stars);
        String sters = route.getStarsString(this);
        textView.setText(sters);

        imageFragment = ImageFragment.newInstance(route.getId());
        RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(getSupportFragmentManager(), imageFragment, route.getId());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(tabInd);

        TabLayout slab = (TabLayout) findViewById(R.id.sliding_tabs);
        slab.setupWithViewPager(pager);
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

    private static final int PICK_IMAGE = 873;

    public void addImage(View v) {
        if (User.currentToken(this)!= null){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        } else {
            //Log.d("RouteActivity", "Must be logged in to add an image");
            DialogFragment df = NotificationDialog.newInstance("Must be logged in to add an image");
            df.show(getFragmentManager(), "dialog");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Log.d("RouteActivity", "No data");
                return;
            }
            try {
                //System.out.println("INTENT RECEIVED");
                if (hasConnection()) {
                    Intent editImage = new Intent(this, EditImageActivity.class);
                    editImage.putExtra("image_uri", data.getData().toString());
                    editImage.putExtra("route_json", Route.encodeRoute(route).toString());
                    startActivity(editImage);
                   // new SendImageTask(this, User.currentToken(this), data.getData(), route.getId(), "nocap", imageFragment).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No data connection - storing comment to post it later.", Toast.LENGTH_LONG).show();
                    LocalDatabase.getInstance(this).store(this, "IMAGE###" + RemoteDatabase.getPath(this, data.getData()) + "###" + User.currentToken(this) + "###" + route.getId() + "###" + "nocap");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}
