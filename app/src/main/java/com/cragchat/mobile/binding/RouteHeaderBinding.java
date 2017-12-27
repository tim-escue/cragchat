package com.cragchat.mobile.binding;

import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.util.FormatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by timde on 11/30/2017.
 */

public class RouteHeaderBinding {

    @BindView(R.id.header)
    RelativeLayout header;
    @BindView(R.id.yds)
    TextView yds;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.stars)
    RatingBar stars;
    @BindView(R.id.ratings)
    TextView ratings;
    @BindView(R.id.sends)
    TextView sends;
    @BindView(R.id.images)
    TextView images;

    public RouteHeaderBinding(View header) {
        ButterKnife.bind(this, header);
    }

    public void bind(Route route) {
        if (!route.getRatings().equalsIgnoreCase("0")) {
            yds.setText(FormatUtil.getYdsString(yds.getContext(), route.getYds()));
        } else {
            yds.setText("-");
        }
        type.setText(route.getRouteType().toString());
        stars.setRating((float) route.getStars());
        ratings.setText(String.valueOf(route.getRatings()));
        sends.setText(String.valueOf(route.getSends()));
        images.setText(String.valueOf(route.getImages().size()));
    }

}
