package chat.crag.cragchat;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import chat.crag.cragchat.adapters.RouteActivityPagerAdapter;
import chat.crag.cragchat.android.SlidingTabLayout;
import chat.crag.cragchat.descriptor.Area;
import chat.crag.cragchat.descriptor.Displayable;
import chat.crag.cragchat.descriptor.Route;
import chat.crag.cragchat.fragments.ImageFragment;
import chat.crag.cragchat.fragments.NotificationDialog;
import chat.crag.cragchat.remote.RemoteDatabase;
import chat.crag.cragchat.search.SearchableActivity;
import chat.crag.cragchat.sql.LocalDatabase;
import chat.crag.cragchat.sql.SendImageTask;
import chat.crag.cragchat.user.User;
import org.json.JSONObject;

public class RouteActivity extends SearchableActivity {

    private Route route;
    private ImageFragment imageFragment;

    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        Intent intent = getIntent();
        route = null;
        try {
            route = Displayable.decodeRoute(new JSONObject(intent.getStringExtra(DATA_STRING)));
        }catch (Exception e) {
            e.printStackTrace();
        }

        int tabInd = intent.getIntExtra("TAB", 0);


        // Set text views
        TextView textView = (TextView) findViewById(R.id.route_display_name);
        if (route.getName().length() >= 16) {
            textView.setTextSize(28);
        } else if (route.getName().length() >= 19) {
            textView.setTextSize(10);
        }
        textView.setText(route.getName());
        //System.out.println(route.getName());

        Area[] hierarchy = LocalDatabase.getInstance(this).getHierarchy(route);
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
        textView = (TextView) findViewById(R.id.type);
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

        SlidingTabLayout slab = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slab.setViewPager(pager);
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
