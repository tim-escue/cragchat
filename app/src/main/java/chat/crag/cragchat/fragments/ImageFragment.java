package chat.crag.cragchat.fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import chat.crag.cragchat.R;
import chat.crag.cragchat.adapters.ImageAdapter;
import chat.crag.cragchat.descriptor.Image;
import chat.crag.cragchat.sql.LocalDatabase;

import java.util.List;


public class ImageFragment extends Fragment {


    public static ImageFragment newInstance(int displayableId) {
        ImageFragment f = new ImageFragment();
        Bundle b = new Bundle();
        b.putString("id", ""+displayableId);
        f.setArguments(b);
        return f;
    }

    private List<Image> images;
    private int id;
    private ImageAdapter adap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_images, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        load (view);

        return view;
    }

    public void load(View v) {

        images = LocalDatabase.getInstance(getContext()).getImagesFor(id);

        if (images != null && v != null) {
            GridView gridview = (GridView) v.findViewById(R.id.thumbnail_grid);
            adap  = new ImageAdapter(getContext(), images.toArray(new Image[images.size()]));
            gridview.setAdapter(adap);

        }
    }

}
