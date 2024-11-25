package com.radio.hello;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    String url = "https://helloradio.co.in/helloradio.ogg";
    MediaPlayer mediaPlayer;

    int bindCount = 0;

    private void createMediaPlayer(){
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void destroyMediaPlayer(){
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        bindCount++;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        bindCount--; if(bindCount <= 0) stopSelf();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        destroyMediaPlayer();
        super.onDestroy();
    }

    private PlayerServiceComms.Stub binder = new PlayerServiceComms.Stub() {

        @Override
        public void startPlayer() {
            createMediaPlayer();
        }

        @Override
        public void pausePlayer() {
            destroyMediaPlayer();
        }

        @Override
        public void stopPlayerService() {
            stopSelf();
        }
    };
}
