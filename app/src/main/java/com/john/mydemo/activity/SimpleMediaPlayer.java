package com.john.mydemo.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.john.mydemo.R;

import java.io.IOException;

public class SimpleMediaPlayer extends Activity implements View.OnClickListener {

    private final static String TAG = "SimpleMediaPlayer";
    
    private EditText mEtPath;
    private EditText mEtPosition;
    private TextView mTvDuration;
    private TextView mTvPosition;
    private Button btnBack;
    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;
    private Button btnForward;
    private Button btnJump;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_media_player);

        mEtPath = (EditText) findViewById(R.id.et_play_path);
        mEtPosition = (EditText) findViewById(R.id.et_to_position);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);
        mTvPosition = (TextView) findViewById(R.id.tv_current_position);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnForward = (Button) findViewById(R.id.btn_forward);
        btnJump = (Button) findViewById(R.id.btn_to_position);

        btnBack.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnJump.setOnClickListener(this);

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                back();
                break;
            case R.id.btn_play:
                setPath();
                break;
            case R.id.btn_pause:
                pause();
                break;
            case R.id.btn_stop:
                stop();
                break;
            case R.id.btn_forward:
                forward();
                break;
            case R.id.btn_to_position:
                setPosition();
                break;
        }
    }

    private void back() {
        try {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                int position = mediaPlayer.getCurrentPosition();
                position -= 5 * 1000;
                if(position < 0) position = 0;
                mediaPlayer.seekTo(position);
            }
        } catch (Exception e) {
            Log.e(TAG, "快退失败");
        }
    }

    private void pause() {
        try {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            Log.e(TAG, "暂停失败");
        }
    }

    private void stop() {
        try {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        } catch (Exception e) {
            Log.e(TAG, "停止失败");
        }
    }

    private void forward() {
        try {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                int position = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                position += 5 * 1000;
                if(duration != -1 && position > duration) position = duration;
                mediaPlayer.seekTo(position);
            }
        } catch (Exception e) {
            Log.e(TAG, "快进失败");
        }
    }

    private void setPosition() {
        String s_position =  mEtPosition.getText().toString();
        try {
            int position = Integer.parseInt(s_position) * 1000;
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                int duration = mediaPlayer.getDuration();
                if(duration != -1 && position > duration) position = duration;
                if(position < 1) position = 0;
                mediaPlayer.seekTo(position);
            }
        } catch (Exception e) {
            Log.e(TAG, "快进失败");
        }
    }

    private void setPath() {
        String path = mEtPath.getText().toString();
        Uri uri = Uri.parse("file:///sdcard/" + path);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, uri);
        } catch (IOException e) {
            return;
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener((mp) -> {
            Log.i(TAG, "缓冲完成，开始播放");
            String duration = getTimeShow(mp.getDuration());
            mTvDuration.setText(duration);
            mp.start();
        });

        mediaPlayer.setOnCompletionListener((mp) -> {
            Log.i(TAG, "播放完成");
        });

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.i(TAG, "播放出错");
            return true;
        });

        mediaPlayer.setOnSeekCompleteListener((mp) -> {
            String position = getTimeShow(mp.getCurrentPosition());
            mTvPosition.setText(position);
        });

        mediaPlayer.setScreenOnWhilePlaying(true);

        new Thread(){
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    final String position = getTimeShow(mediaPlayer.getCurrentPosition());
                    mTvPosition.post(() -> {
                        mTvPosition.setText(position);
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

    private String getTimeShow(int time) {
        int minutes = time / (60 * 1000);
        time -= minutes * (60 * 1000);
        int seconds = time / 1000;
        time -= seconds * 1000;
        int millseconds = time;
        return minutes+"'"+seconds+"''"+millseconds+"'''";
    }

}
