package com.example.rafaelanastacioalves.moby.entitydetailing;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rafaelanastacioalves.moby.R;
import com.example.rafaelanastacioalves.moby.common.MediaReferenceHelper;
import com.example.rafaelanastacioalves.moby.domain.entities.EntityDetails;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.MediaReference;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntityDetailsFragment extends DaggerFragment implements View.OnClickListener {

    public static String ARG_OBJECTS;

    private LiveDataEntityDetailsViewModel mLiveDataEntityDetailsViewModel;

    @Inject
    EntityDetailViewModelFactory entityDetailViewModelFactory;

    @BindView(R.id.detail_entity_detail_name)
    TextView tripPackageDetailValor;

    @BindView(R.id.trip_package_detail_imageview)
    ImageView tripPackageDetailImageview;

    @BindView(R.id.detail_entity_video)
    VideoView videoView;

    private MainEntity.Objects objects;
    private MediaController mediaController;

    @Override
    public void onAttach(Context context) {
        settupadagger();
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recoverVariables();
        subscribe();
        setupMediaController();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflateViews(inflater, container);
        return root;
    }

    private void setupMediaController() {
        mediaController = new MediaController(getActivity());
    }

    private void settupadagger() {
        AndroidSupportInjection.inject(this);
    }

    private void recoverVariables() {
        objects = (MainEntity.Objects) getArguments().getSerializable(ARG_OBJECTS);

    }

    private void subscribe() {
        mLiveDataEntityDetailsViewModel = ViewModelProviders.of(this, entityDetailViewModelFactory).get(LiveDataEntityDetailsViewModel.class);
        mLiveDataEntityDetailsViewModel.getEntityDetails(objects).observe(this, new Observer<Resource<MediaReference>>() {
            @Override
            public void onChanged(@Nullable Resource<MediaReference> entityDetails) {
                if (entityDetails != null && entityDetails.status == Resource.Status.SUCCESS){
                    MediaReference mediaReference = entityDetails.data;
                    playVideoFromUri(MediaReferenceHelper.getMediaUriFrom(mediaReference.getVideoFile(), getContext()));
                }
            }
        });
    }

    private void playVideoFromUrl(String bg) {
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(bg);
        videoView.requestFocus();
        videoView.start();
    }

    private void playVideoFromUri(Uri uriReferene) {
        videoView.setVideoURI(uriReferene);
        videoView.start();
    }

    private View inflateViews(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_detail_entity_detail_view, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void setupActionBarWithTitle(String title) {
        AppCompatActivity mActivity = (AppCompatActivity) getActivity();
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    private void setViewsWith(EntityDetails entityDetails) {
        tripPackageDetailValor.setText(entityDetails.getPrice());
        setupActionBarWithTitle(entityDetails.getTitle());
        Picasso.get()
                .load(entityDetails.getImage_url())
                .into(tripPackageDetailImageview, new Callback() {
                    @Override
                    public void onSuccess() {
                        getActivity().supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Comprado!", Toast.LENGTH_SHORT).show();
    }
}
