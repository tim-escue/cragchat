package com.cragchat.mobile.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.EditImageActivity;
import com.cragchat.mobile.view.adapters.ImageAdapter;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.user.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageFragment extends Fragment implements View.OnClickListener {

    public static final int PICK_IMAGE = 873;
    private List<Image> images;
    private int id;
    private ImageAdapter adap;

    @BindView(R.id.list_empty)
    TextView empty;

    public static ImageFragment newInstance(int displayableId) {
        ImageFragment f = new ImageFragment();
        Bundle b = new Bundle();
        b.putString("id", ""+displayableId);
        f.setArguments(b);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        ButterKnife.bind(this, view);

        id = Integer.parseInt(getArguments().getString("id"));

        load (view);

        return view;
    }

    public void load(View v) {

        images = LocalDatabase.getInstance(getContext()).getImagesFor(id);

        if (images != null && images.size() > 0&& v != null) {
            GridView gridview = (GridView) v.findViewById(R.id.thumbnail_grid);
            adap  = new ImageAdapter(getContext(), images.toArray(new Image[images.size()]));
            gridview.setAdapter(adap);
        } else {
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (User.currentToken(getActivity()) != null) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            } else {
                //Log.d("RouteActivity", "Must be logged in to add an image");
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to add an image");
                df.show(getFragmentManager(), "dialog");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Log.d("RouteActivity", "No data");
                return;
            }
            try {
                //System.out.println("INTENT RECEIVED");
                if (((CragChatActivity)getActivity()).hasConnection()) {
                    Intent editImage = new Intent(getContext(), EditImageActivity.class);
                    editImage.putExtra("image_uri", data.getData().toString());
                    editImage.putExtra("displayable_id", id);
                    startActivity(editImage);
                    // new SendImageTask(this, User.currentToken(this), data.getData(), route.getId(), "nocap", imageFragment).execute();
                } else {
                    Toast.makeText(getContext(), "No data connection - storing comment to post it later.", Toast.LENGTH_LONG).show();
                    LocalDatabase.getInstance(getContext()).store(getActivity(), "IMAGE###" + RemoteDatabase.getPath(getContext(), data.getData()) + "###" + User.currentToken(getActivity()) + "###" + id + "###" + "nocap");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}