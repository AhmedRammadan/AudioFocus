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
        //call function  getSystemService <get for my AUDIO_SERVICE>
        //and put it object maudioManager
        maudioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        //get music file from folder raw
        media=MediaPlayer.create(this,R.raw.phrase_what_is_your_name);
    }

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    //بعد الايقاف يعود هنه لتشغيل مره اخرى
                    //setVolume 100%
                    //start media
                    media.setVolume(1.0f,1.0f);
                    media.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //يسحب النظام الصوت منك لفتره غير معروف مدتها
                    //stop for media
                    media.stop();
                    media.release();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //يسحب النظام الصوت منك لفتره قصيرة
                    //pause for media
                    // ثم يعود مره اخر لل GAIN فى الاعلى للتشغيل مره اخره
                    media.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //يقول النظام اريدك ان تنحني لفتره فصيره
                    // بمعنى اخفض الصوت
                    //setVolume 20%
                    // ثم يعود مره اخر لل GAIN فى الاعلى للتشغيل مره اخره
                    media.setVolume(0.2f,0.2f);
                    break;
            }
        }
    };
    public void play(View view) {
        //Operating System (Android)
        int requset=maudioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
        switch (requset){
            //if accept Start media
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                media.start();
                break;
            //if don't accept Toast massage show
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                Toast.makeText(this, "AUDIOFOCUS_REQUEST_FAILED", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void pause(View view) {
        media.pause();
        //if the media finished return Audio focus for Operating System <abandon>
        maudioManager.abandonAudioFocus(audioFocusChangeListener);
        media.release();
    }
}
