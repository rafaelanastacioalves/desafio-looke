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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafaelanastacioalves.moby.R;
import com.example.rafaelanastacioalves.moby.common.MediaReferenceHelper;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.MediaReference;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntityDetailsFragment extends DaggerFragment {

    public static final String ARG_POSITION = "position";
    public static final String MAIN_ENTITY = "objects";

    private LiveDataEntityDetailsViewModel mLiveDataEntityDetailsViewModel;

    @Inject
    EntityDetailViewModelFactory entityDetailViewModelFactory;

    @BindView(R.id.detail_entity_detail_name)
    TextView tripPackageDetailValor;

    @BindView(R.id.main_view)
    View mainView;

    @BindView(R.id.detail_entity_video)
    PlayerView videoPlayerView;

    @BindView(R.id.detail_entity_audio)
    PlayerView audioPlayerView;

    @BindView(R.id.error_view)
    TextView errorView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private MainEntity.Objects objects;
    private SimpleExoPlayer videoPlayer;
    private SimpleExoPlayer audioPlayer;
    private long videoPlaybackPosition = 0;
    private int videoCurrentWindow =0;
    private boolean videoPlayWhenReady = true;

    private long audioPlaybackPosition = 0;
    private int audioCurrentWindow =0;
    private boolean audioPlayWhenReady = true;



    @Override
    public void onAttach(Context context) {
        settupadagger();
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recoverVariables();
        loadMedia();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflateViews(inflater, container);

        setViewsWith(objects);
        return root;
    }


    private void initializePlayers() {
        videoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        audioPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl()
        );
        audioPlayer.addListener(getAudioMediaListener());

        videoPlayerView.setPlayer(videoPlayer);
        videoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        videoPlayer.setPlayWhenReady(videoPlayWhenReady);
        videoPlayer.seekTo(videoCurrentWindow, videoPlaybackPosition);

        audioPlayerView.setPlayer(audioPlayer);
        audioPlayer.setPlayWhenReady(videoPlayWhenReady);
        audioPlayer.seekTo(videoCurrentWindow, videoPlaybackPosition);





    }


    private void settupadagger() {
        AndroidSupportInjection.inject(this);
    }

    private void recoverVariables() {
        objects = (MainEntity.Objects) getArguments().getSerializable(MAIN_ENTITY);

    }

    private void loadMedia() {
        mLiveDataEntityDetailsViewModel = ViewModelProviders.of(this, entityDetailViewModelFactory).get(LiveDataEntityDetailsViewModel.class);
        mLiveDataEntityDetailsViewModel.getMedia(objects).observe(this, new Observer<Resource<MediaReference>>() {
            @Override
            public void onChanged(@Nullable Resource<MediaReference> entityDetails) {
                if (entityDetails != null && entityDetails.status != null) {

                    switch (entityDetails.status){
                        case SUCCESS:
                            hideProgressBar();
                            showMainView();
                            MediaReference mediaReference = entityDetails.data;
                            playVideoFromUri(MediaReferenceHelper.getMediaUriFrom(mediaReference.getVideoFile(), getContext()));
                            playAudioFromUri(MediaReferenceHelper.getMediaUriFrom(mediaReference.getAudioFile(), getContext()));
                            break;
                        case LOADING:
                            showProgressBar();
                            hideMainView();
                            break;
                        case GENERIC_ERROR:
                            hideProgressBar();
                            showErrorView();
                            break;
                        case CONNECTIVITY_ERROR:
                            hideProgressBar();
                            showErrorView();
                            break;
                        case INTERNAL_SERVER_ERROR:
                            hideProgressBar();
                            showErrorView();
                            break;
                    }

                }
            }
        });
    }


    private void askVideoToStop() {
        videoPlayer.stop();
        videoPlayer.seekTo(0);

        Toast.makeText(getActivity(), "Video stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayers();
        }
    }

    private void playAudioFromUri(Uri mediaUriFrom) {
        MediaSource mediaSource = buildMediaSource(mediaUriFrom);
        audioPlayer.prepare(mediaSource);
    }

    private void playVideoFromUri(Uri uriReferene) {
        MediaSource mediaSource = buildMediaSource(uriReferene);
        videoPlayer.prepare(mediaSource);
    }

    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
        if ((Util.SDK_INT <= 23 || videoPlayer == null)) {
            initializePlayers();
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

    private void setViewsWith(MainEntity.Objects objects) {
        tripPackageDetailValor.setText(objects.getName());
        setupActionBarWithTitle(objects.getName());
    }

    private void showErrorView(){
        errorView.setVisibility(View.VISIBLE);
    }

    private void hideErrorView(){
        errorView.setVisibility(View.GONE);
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

    private void showMainView(){
        mainView.setVisibility(View.VISIBLE);
    }

    private void hideMainView(){
        mainView.setVisibility(View.GONE);
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
        if (videoPlayer != null) {
            videoPlaybackPosition = videoPlayer.getCurrentPosition();
            videoCurrentWindow = videoPlayer.getCurrentWindowIndex();
            videoPlayWhenReady = videoPlayer.getPlayWhenReady();
            videoPlayer.release();
            videoPlayer = null;
        }

        if (audioPlayer != null) {
            audioPlaybackPosition = audioPlayer.getCurrentPosition();
            audioCurrentWindow = audioPlayer.getCurrentWindowIndex();
            audioPlayWhenReady = audioPlayer.getPlayWhenReady();
            audioPlayer.release();
            audioPlayer = null;
        }
    }



    private Player.EventListener getAudioMediaListener() {
        return new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState){
                    case Player.STATE_ENDED:
                        askVideoToStop();
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        };
    }
}
