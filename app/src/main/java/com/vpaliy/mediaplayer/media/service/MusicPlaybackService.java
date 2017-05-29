package com.vpaliy.mediaplayer.media.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import java.util.List;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import com.vpaliy.mediaplayer.media.playback.PlaybackManager;
import com.vpaliy.mediaplayer.media.playback.QueueManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicPlaybackService extends MediaBrowserServiceCompat
            implements PlaybackManager.PlaybackManagerCallback,
        QueueManager.MetadataUpdateListener{

    private static final String LOG_TAG=MusicPlaybackService.class.getSimpleName();

    public static final String MEDIA_ID_ROOT="root";
    public static final String MEDIA_ID_EMPTY_ROOT="empty_root";

    private final  MediaSessionCompat mediaSession;
    private final PlaybackStateCompat.Builder stateBuilder;
    private final PlaybackManager playbackManager;

    @Inject
    public MusicPlaybackService(@NonNull MediaSessionCompat mediaSession,
                                @NonNull PlaybackManager manager){
        this.mediaSession=mediaSession;
        this.playbackManager=manager;
        stateBuilder=new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(playbackManager.getMediaSessionCallback());
        setSessionToken(mediaSession.getSessionToken());

    }

    @Override
    public void onPlaybackStart() {
        mediaSession.setActive(true);
        Intent intent=new Intent(this,MusicPlaybackService.class);
        startService(intent);
    }

    @Override
    public void onPlaybackStateUpdated(PlaybackStateCompat newState) {

    }

    @Override
    public void onNotificationRequired() {

    }

    @Override
    public void onPlaybackStop() {
        mediaSession.setActive(false);
        stopSelf();
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {

    }

    @Override
    public void onCurrentQueueIndexUpdated(int queueIndex) {

    }

    @Override
    public void onMetadataRetrieveError() {

    }

    @Override
    public void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue) {

    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        Log.d(LOG_TAG,"onGetRoot()");
        if(!clientPackageName.equals(getPackageName())){
            return new BrowserRoot(MEDIA_ID_ROOT,null);
        }
        return new BrowserRoot(MEDIA_ID_EMPTY_ROOT,null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        Log.d(LOG_TAG,"onLoadChildren()");
    }


}