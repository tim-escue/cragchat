package com.cragchat.mobile.ui.view.adapters.recycler;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.model.realm.RealmRoute;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.mobile.util.NavigationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

public class RouteListRecyclerAdapter extends RealmRecyclerViewAdapter<RealmRoute, RouteListRecyclerAdapter.ViewHolder> implements LifecycleObserver {

    private CragChatActivity activity;
    private Realm mRealm;
    private String[] routeIds;

    private RouteListRecyclerAdapter(@Nullable OrderedRealmCollection<RealmRoute> data, boolean autoUpdate,
                                     String[] routeIds, CragChatActivity activity, Realm realm) {
        super(data, autoUpdate);
        this.activity = activity;
        this.mRealm = realm;
        this.routeIds = routeIds;
    }

    public static RouteListRecyclerAdapter create(String[] routeIds, CragChatActivity activity) {
        Realm realm = Realm.getDefaultInstance();
        return new RouteListRecyclerAdapter(
                realm.where(RealmRoute.class).in(RealmRoute.FIELD_KEY, routeIds).findAll(),
                true,
                routeIds,
                activity,
                realm
        );
    }

    public static View getItemView(ViewGroup root) {
        return LayoutInflater.
                from(root.getContext()).
                inflate(R.layout.route_list_item, root, false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disconnectListener() {
        mRealm.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getItemView(parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RealmRoute r = getItem(position);
        holder.bind(r, activity);
    }

    public void filter(boolean showSport, boolean showTrad, boolean showMixed) {
        RealmQuery<RealmRoute> routeQuery = mRealm.where(RealmRoute.class)
                .in(RealmRoute.FIELD_KEY, routeIds);
        if (!showSport) {
            routeQuery.notEqualTo(RealmRoute.FIELD_TYPE, "Sport");
        }
        if (!showTrad) {
            routeQuery.notEqualTo(RealmRoute.FIELD_TYPE, "Trad");
        }
        if (!showMixed) {
            routeQuery.notEqualTo(RealmRoute.FIELD_TYPE, "Sport,Trad");
        }
        updateData(routeQuery.findAll());
    }

    public void sort(String field) {
        OrderedRealmCollection<RealmRoute> data = getData();
        if (data != null) {
            updateData(data.sort(field, Sort.ASCENDING));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author)
        TextView text1;

        @BindView(R.id.type)
        TextView type;

        @BindView(R.id.not_rated)
        TextView notRated;

        @BindView(R.id.rectangle)
        RelativeLayout rect;

        @BindView(R.id.icon)
        ImageView icon;

        @BindView(R.id.yds)
        TextView yds;

        @BindView(R.id.stars)
        RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Route r, final CragChatActivity activity) {
            text1.setText(r.getName());
            if (r.getRatings().equals("0")) {
                yds.setText("-");
                rating.setVisibility(View.GONE);
                notRated.setVisibility(View.VISIBLE);
            } else {
                yds.setText(FormatUtil.getYdsString(yds.getContext(), r.getYds()));
                rating.setVisibility(View.VISIBLE);
                rating.setRating((float) r.getStars());
                notRated.setVisibility(View.GONE);
            }
            type.setText(r.getRouteType().toString());
            icon.setImageResource(r.getType().equalsIgnoreCase("sport") ? R.drawable.bolt_img : R.drawable.nuts);
            rect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtil.launch(activity, r);
                }
            });
        }
    }
}
