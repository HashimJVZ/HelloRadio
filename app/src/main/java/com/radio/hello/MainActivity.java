package com.radio.hello;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton playPauseBtn;
    Boolean isPlaying = true;

    PlayerServiceComms comms;
    PlayerServiceConnection connection;

    private class PlayerServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            comms = PlayerServiceComms.Stub.asInterface(service);
            try {
                comms.startPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            comms = null;
            Log.d("MainActivity","PlayerService has been disconnected.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent playerServiceIntent = new Intent(MainActivity.this, PlayerService.class);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        connection = new PlayerServiceConnection();

        bindService(playerServiceIntent, connection, BIND_AUTO_CREATE);
        showToast();

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    playPauseBtn.setImageResource(R.drawable.play);
                    isPlaying = false;
//                   pauseMusic();
                    try {
                        comms.pausePlayer();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                } else {
                    playPauseBtn.setImageResource(R.drawable.pause);
                    isPlaying = true;
//                   playMusic();
                    try {
                        comms.startPlayer();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    showToast();

                }

            }
        });
    }

    private void showToast() {
        Toast.makeText(this, "Buffering...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
