package com.cragchat.mobile.binding;

import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Area;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by timde on 11/30/2017.
 */

public class AreaHeaderBinding {

    @BindView(R.id.areas)
    TextView areas;
    @BindView(R.id.routes)
    TextView routes;
    @BindView(R.id.images)
    TextView images;

    public AreaHeaderBinding(View header) {
        ButterKnife.bind(this, header);
    }

    public void bind(Area area) {
        images.setText(String.valueOf(area.getImages().size()));
        routes.setText(String.valueOf(area.getRoutes().size()));
        areas.setText(String.valueOf(area.getSubAreas().size()));
    }

}
