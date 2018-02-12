package com.cragchat.mobile.ui.view.adapters.recycler;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.realm.RealmArea;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;
import com.cragchat.mobile.util.NavigationUtil;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class CragsFragmentRecyclerAdapter extends RealmRecyclerViewAdapter<RealmArea, CragsFragmentRecyclerAdapter.ViewHolder> implements LifecycleObserver {

    private CragChatActivity activity;
    private Realm realm;
    private int lastPosition;

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    private CragsFragmentRecyclerAdapter(@Nullable OrderedRealmCollection<RealmArea> data, boolean autoUpdate, CragChatActivity activity, Realm realm) {
        super(data, autoUpdate);
        this.activity = activity;
        this.realm = realm;
        lastPosition = -1;
    }

    public static CragsFragmentRecyclerAdapter create(String cragName, Activity activity) {
        Realm realm = Realm.getDefaultInstance();
        return new CragsFragmentRecyclerAdapter(
                realm.where(RealmArea.class).equalTo(RealmArea.FIELD_NAME, cragName).findAll(),
                true,
                (CragChatActivity) activity,
                realm);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.crag_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Area crag = getItem(position);
        Log.d("Binding", crag.getKey());
        holder.name.setText(crag.getName());
        holder.routeNumber.setText(String.valueOf(crag.getRoutes().size()));
        holder.areaNumber.setText(String.valueOf(crag.getSubAreas().size()));
        holder.imagesNumber.setText(String.valueOf(crag.getImages().size()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtil.launch(activity, crag);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView routeNumber;
        TextView areaNumber;
        TextView imagesNumber;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.crag_name);
            icon = itemView.findViewById(R.id.crag_icon);
            areaNumber = itemView.findViewById(R.id.number_areas);
            routeNumber = itemView.findViewById(R.id.number_routes);
            imagesNumber = itemView.findViewById(R.id.number_comments);
        }
    }

    private void setAnimation(RecyclerView.ViewHolder viewHolder, int position) {
        if ( position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
            viewHolder.itemView.startAnimation(animation);
        }
        lastPosition = position;

    }

    @Override
    public void onViewDetachedFromWindow(final CragsFragmentRecyclerAdapter.ViewHolder holder)
    {
        holder.itemView.clearAnimation();
    }
}
