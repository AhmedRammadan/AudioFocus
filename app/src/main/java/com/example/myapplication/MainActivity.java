package com.example.myapplication;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MediaPlayer media;
    AudioManager maudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maudioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        media=MediaPlayer.create(this,R.raw.phrase_what_is_your_name);
    }
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    media.setVolume(1.0f,1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    media.stop();
                    media.release();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    media.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    media.setVolume(0.2f,0.2f);
                    break;
            }
        }
    };
    public void play(View view) {
        int requset=maudioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
        switch (requset){
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                media.start();
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                Toast.makeText(this, "AUDIOFOCUS_REQUEST_FAILED", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void pause(View view) {
        media.pause();
        maudioManager.abandonAudioFocus(audioFocusChangeListener);
        media.release();
    }
}
