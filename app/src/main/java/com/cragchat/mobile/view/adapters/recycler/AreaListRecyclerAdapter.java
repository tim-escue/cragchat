package com.cragchat.mobile.view.adapters.recycler;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.realm.RealmArea;
import com.cragchat.mobile.model.realm.RealmRoute;
import com.cragchat.mobile.util.NavigationUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

public class AreaListRecyclerAdapter extends RealmRecyclerViewAdapter<RealmArea,
        AreaListRecyclerAdapter.ViewHolder> implements LifecycleObserver {

    private Activity activity;
    private Realm mRealm;

    private AreaListRecyclerAdapter(@Nullable OrderedRealmCollection<RealmArea> data,
                                    boolean autoUpdate, Activity activity, Realm realm) {
        super(data, autoUpdate);
        this.activity = activity;
        this.mRealm = realm;
    }

    public static AreaListRecyclerAdapter create(Activity context, String[] entityKeys) {
        Realm realm = Realm.getDefaultInstance();
        return new AreaListRecyclerAdapter(
                realm.where(RealmArea.class).in(RealmRoute.FIELD_KEY, entityKeys).findAll(),
                false, context, realm);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disconnectListener() {
        mRealm.close();
    }

    public static View getItemView(ViewGroup root) {
        return LayoutInflater.
                from(root.getContext()).
                inflate(R.layout.displayable_recycler_row, root, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getItemView(parent));
    }

    public void sort(String field) {
        long start = System.currentTimeMillis();
        OrderedRealmCollection<RealmArea> data = getData();
        if (data != null) {
            updateData(data.sort(field, Sort.ASCENDING));
        }
        Log.d("SORT", String.valueOf(System.currentTimeMillis() - start));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RealmArea area = getItem(position);
        holder.bind(area, activity);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author)
        TextView text1;

        @BindView(R.id.list_row_two)
        TextView text2;

        @BindView(R.id.rectangle)
        LinearLayout rect;

        @BindView(R.id.icon)
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Area area, final Activity activity) {
            text1.setText(area.getName());
            List<String> routes = area.getRoutes();
            StringBuilder info = new StringBuilder();
            info.append(routes.size());
            info.append(" routes");
            List<String> subAreas = area.getSubAreas();
            if (subAreas.size() > 0) {
                info.append(" ");
                info.append(subAreas.size());
                info.append(" areas");
            }
            text2.setText(info.toString());
            icon.setImageResource(R.drawable.area_mountain);
            rect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtil.launch(activity, area);
                }
            });
        }
    }
}
