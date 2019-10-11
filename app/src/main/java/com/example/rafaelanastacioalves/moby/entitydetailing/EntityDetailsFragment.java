package com.example.rafaelanastacioalves.moby.entitydetailing;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.example.rafaelanastacioalves.moby.BuildConfig;
import com.example.rafaelanastacioalves.moby.R;
import com.example.rafaelanastacioalves.moby.common.MediaReferenceHelper;
import com.example.rafaelanastacioalves.moby.domain.entities.EntityDetails;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.MediaReference;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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
    PlayerView playerView;

    private MainEntity.Objects objects;
    private MediaController mediaController;
    private SimpleExoPlayer player;
    private long playbackPosition = 0;
    private int currentWindow;
    private boolean playWhenReady;

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflateViews(inflater, container);
        return root;
    }


    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
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
                if (entityDetails != null && entityDetails.status == Resource.Status.SUCCESS) {
                    MediaReference mediaReference = entityDetails.data;
                    playVideoFromUri(MediaReferenceHelper.getMediaUriFrom(mediaReference.getVideoFile(), getContext()));
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    private void playVideoFromUri(Uri uriReferene) {
        MediaSource mediaSource = buildMediaSource(uriReferene);
        player.prepare(mediaSource);
    }

    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    private MediaSource buildMediaSource(Uri uriReference) {
        DataSource.Factory dataSourceFactory = new FileDataSourceFactory();
        return new ExtractorMediaSource(uriReference, dataSourceFactory,
                new DefaultExtractorsFactory(), null, null);
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
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Comprado!", Toast.LENGTH_SHORT).show();
    }
}
