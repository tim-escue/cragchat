package com.cragchat.mobile.mvp.view.adapters.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Area> areas;
    private List<CardView> views;
    private List<String> names;

    public MainPagerAdapter(Context context) {
        mContext = context;
        this.areas = new ArrayList<>();
        this.views = new ArrayList<>();
        this.names = new ArrayList<>();
    }

    public void setData(List<Area> newAreas) {
        for (Area i : newAreas) {
            if (!names.contains(i.getName())) {
                areas.add(i);
                views.add(null);
                names.add(i.getName());
            }
        }
        notifyDataSetChanged();
    }

    public CardView getCardAt(int position) {
        return views.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Area crag = areas.get(position);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.crag_item, null, false);

        CardView card = itemView.findViewById(R.id.card);
        TextView name = itemView.findViewById(R.id.crag_name);
        TextView areaNumber = itemView.findViewById(R.id.number_areas);
        TextView routeNumber = itemView.findViewById(R.id.number_routes);
        TextView imagesNumber = itemView.findViewById(R.id.number_comments);

        name.setText(crag.getName());
//        routeNumber.setText(String.valueOf(crag.getRoutes().size()));
       // areaNumber.setText(String.valueOf(crag.getSubAreas().size()));
       // imagesNumber.setText(String.valueOf(crag.getImages().size()));
        itemView.setOnClickListener(view -> ViewUtil.launch(mContext, crag.getKey()));

        card.setMaxCardElevation(16.0f);
        collection.addView(itemView);
        views.set(position, card);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
        views.set(position, null);
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}