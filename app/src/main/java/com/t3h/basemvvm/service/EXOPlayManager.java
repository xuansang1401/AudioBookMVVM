package com.t3h.basemvvm.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class EXOPlayManager {

    private SimpleExoPlayer simpleExoPlayer;
    private Context context;
    private ExoCallBack exoCallBack;
    public boolean isPlaying = false;
//    public PlayerView playerView;

    public EXOPlayManager(Context context, ExoCallBack exoCallBack) {
        this.context = context;
        this.exoCallBack = exoCallBack;
    }

    public void initEXOMusic(String url) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }
        isPlaying = true;

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.e("sang","onLoadingChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                simpleExoPlayer.setPlayWhenReady(playWhenReady);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e("bvh", "error player: " + error.getMessage());
                Toast.makeText(context, "Can not play this video", Toast.LENGTH_SHORT).show();
                exoCallBack.onPlayerError();
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

        });

//        simpleExoPlayer.setVolume(2f);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        String userAgent = Util.getUserAgent(context, "applicationInfo.getAppName()");

        // Default parameters, except allowCrossProtocolRedirects is true
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent,
                null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true
        );

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                context,
                null,
                httpDataSourceFactory
        );



        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null);


//        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        simpleExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        simpleExoPlayer.prepare(mediaSource, false, false);
        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);

        simpleExoPlayer.setPlayWhenReady(true);
    }


    public void pauseEXO() {
        if (simpleExoPlayer != null) {
            isPlaying = false;
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    public void playEXO() {
        if (simpleExoPlayer != null) {
            isPlaying = true;
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public void seekToBegin() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.seekTo(0);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public void seekTo(long milisecond ) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.seekTo(milisecond);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public void rewindEXO(){
        if (simpleExoPlayer != null) {
            long time=simpleExoPlayer.getCurrentPosition();
            long t= 0;
            if (time>30001){
                t= time-30000;
            }

            simpleExoPlayer.seekTo(t);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }
    public void fastForwardEXO(){
        if (simpleExoPlayer != null) {
            long time=simpleExoPlayer.getCurrentPosition();
            long t= simpleExoPlayer.getDuration();
            long c= 0;
            if (time+30000<t){
                c= time+30000;
            }

            simpleExoPlayer.seekTo(c);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public long getCurrentPosition(){
        if(simpleExoPlayer != null){
            return simpleExoPlayer.getCurrentPosition();
        }else {
            return -1;
        }

    }

    public long getMilisOfVideo(){
        if(simpleExoPlayer != null){
            return simpleExoPlayer.getDuration();
        }else {
            return -1;
        }

    }

    //destroy exo
    public void stopEXO() {
        if (simpleExoPlayer != null) {
            isPlaying = false;
            simpleExoPlayer.stop();
        }
    }

    //destroy exo
    public void releaseEXO() {
        if (simpleExoPlayer != null) {
            isPlaying = false;
            simpleExoPlayer.release();
        }
    }

    public void playNewVideo(String urlPath){
        if (isPlaying){
            simpleExoPlayer.stop();
        }

        String userAgent = Util.getUserAgent(context, "applicationInfo.getAppName()");

        ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(urlPath),
                new DefaultDataSourceFactory(context, userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        isPlaying = true;

    }

    public void setPlaybackParameters(PlaybackParameters param) {
        simpleExoPlayer.setPlaybackParameters(param);
    }


    public interface ExoCallBack {
        void playNextWhenEnded();
        void onPlayerError();
    }

}
