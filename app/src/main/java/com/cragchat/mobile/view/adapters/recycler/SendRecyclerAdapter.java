package com.cragchat.mobile.view.adapters.recycler;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.realm.RealmSend;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.FormatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class SendRecyclerAdapter extends RealmRecyclerViewAdapter<RealmSend, SendRecyclerAdapter.ViewHolder> {

    public SendRecyclerAdapter(String entityKey) {
        this(Repository.getRealm().where(RealmSend.class).equalTo(RealmSend.FIELD_ENTITY_KEY, entityKey).findAll(), true);
    }

    public SendRecyclerAdapter(@Nullable OrderedRealmCollection<RealmSend> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_list_send, parent, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RealmSend send = getItem(position);
        String title = send.getUsername();

        holder.text1.setText(title);
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
        holder.text2.setText(FormatUtil.getFormattedDate(send.getDate()));
        holder.text3.setText("Pitches: " + send.getPitches());
        holder.text4.setText("Attempts: " + send.getAttempts());
        holder.text5.setText("Style: " + send.getClimbingStyle());
        holder.text6.setText("Send Type: " + send.getSendType());

    }
}
