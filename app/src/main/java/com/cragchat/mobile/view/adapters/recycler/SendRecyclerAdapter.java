package com.cragchat.mobile.view.adapters.recycler;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Send;
import com.cragchat.mobile.model.realm.RealmSend;
import com.cragchat.mobile.util.FormatUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class SendRecyclerAdapter extends RealmRecyclerViewAdapter<RealmSend, SendRecyclerAdapter.ViewHolder> implements LifecycleObserver {

    private Realm mRealm;

    public static SendRecyclerAdapter create(String entityKey) {
        Realm realm = Realm.getDefaultInstance();
        return new SendRecyclerAdapter(
                realm.where(RealmSend.class).equalTo(RealmSend.FIELD_ENTITY_KEY, entityKey).findAll(),
                true, realm);
    }

    private SendRecyclerAdapter(@Nullable OrderedRealmCollection<RealmSend> data, boolean autoUpdate, Realm realm) {
        super(data, autoUpdate);
        this.mRealm = realm;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disconnectListener() {
        System.out.println("Lifecycle: Actually destroyed");
        mRealm.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getItemView(parent));
    }

    public static View getItemView(ViewGroup parent) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_list_send, parent, false);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_send_username)
        TextView text1;
        @BindView(R.id.item_send_date)
        TextView text2;
        @BindView(R.id.item_send_pitches)
        TextView text3;
        @BindView(R.id.item_send_attempts)
        TextView text4;
        @BindView(R.id.item_send_style)
        TextView text5;
        @BindView(R.id.item_send_send_type)
        TextView text6;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Send send) {
            String title = send.getUsername();
            text1.setText(title);
        /*holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RetroFitRestApi.isConnected(activity)) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", send.getUserName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
            String date = send.getDate();
            Date dateObject = null;
            try {
                dateObject = FormatUtil.RAW_FORMAT.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

            text2.setText(dateObject != null ? FormatUtil.elapsed(dateObject, Calendar.getInstance().getTime()) : date);
            text2.setText(FormatUtil.getFormattedDate(send.getDate()));
            text3.setText("Pitches: " + send.getPitches());
            text4.setText("Attempts: " + send.getAttempts());
            text5.setText("Style: " + send.getClimbingStyle());
            text6.setText("Send Type: " + send.getSendType());
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RealmSend send = getItem(position);
        holder.bind(send);
    }
}
