package chat.crag.cragchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import chat.crag.cragchat.R;
import chat.crag.cragchat.sql.UpdateRecentActivityTask;

public class PreviewFragment extends Fragment {


    public static PreviewFragment newInstance(int displayableId) {
        PreviewFragment f = new PreviewFragment();
        Bundle b = new Bundle();
        b.putString("id", ""+displayableId);
        f.setArguments(b);
        return f;
    }

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        ListView lv = (ListView) view.findViewById(R.id.list_preview);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.progressBox);
        layout.setVisibility(View.VISIBLE);
        ProgressBar bar = (ProgressBar)view.findViewById(R.id.progressBar1);
        new UpdateRecentActivityTask(getActivity(), -1, lv, layout).execute();

        TextView cragName = (TextView) view.findViewById(R.id.text_preview_cragname);
        if (cragName != null) {
            String udata =  cragName.getText().toString();
            SpannableString content = new SpannableString(udata);
            content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
            cragName.setText(content);
        }


        return view;
    }


}