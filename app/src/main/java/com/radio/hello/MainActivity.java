package com.radio.hello;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton playPauseBtn;
    Boolean isPlaying = true;

    private class PlayerServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseBtn = findViewById(R.id.playPauseBtn);

        final Intent intent = new Intent(MainActivity.this, PlayerService.class);
        startService(intent);
        bindService(intent, new PlayerServiceConnection(), 0);
        showToast();

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    playPauseBtn.setImageResource(R.drawable.play);
                    isPlaying = false;
//                   pauseMusic();
                    stopService(intent);

                } else {
                    playPauseBtn.setImageResource(R.drawable.pause);
                    isPlaying = true;
//                   playMusic();
                    startService(intent);
                    showToast();

                }

            }
        });
    }

    private void showToast() {
        Toast.makeText(this, "Buffering...", Toast.LENGTH_SHORT).show();
    }

}
