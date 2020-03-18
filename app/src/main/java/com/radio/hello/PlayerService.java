package com.radio.hello;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    String url = "http://148.72.210.73:8000/helloradio.ogg";
    MediaPlayer mediaPlayer;

    public interface MyPreparedListener {
        void playerPrepared();
    }

    MyPreparedListener myPreparedListener;

    public void setMyPreparedListener(MyPreparedListener myPreparedListener) {
        this.myPreparedListener = myPreparedListener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (myPreparedListener != null) myPreparedListener.playerPrepared();
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }
}
