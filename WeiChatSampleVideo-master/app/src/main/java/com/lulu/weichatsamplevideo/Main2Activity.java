package com.lulu.weichatsamplevideo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/22 16:25:16:25
 * @Purpose :
 */

/**
 * @author chenzheng_java
 * @description 通过SurfaceView/SurfaceHolder实现自己的播放器
 * @description 这里进行一下补充说明，我们可以通过mediaplayer添加OnPreparedListener
 * 以及OnCompletionListener等事件对准备好播放以及播放完成后的操作进行控制。
 * 使用SurfaceView以及SurfaceHolder进行视频播放时，结构是这样的：
 * 1、首先，我们从布局文件中获取一个surfaceView
 * 2、通过surfaceView.getHolder()方法获取与该容器想对应的surfaceHolder
 * 3、对srufaceHolder进行一些默认的设置，如addCallback()和setType()
 * 4、通过mediaPlayer.setDisplay()方法将视频播放与播放容器链接起来
 * @since 2011/03/23
 */
public class Main2Activity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private SurfaceView mSurfaceView1;
    private ImageButton mButtonPlay;
    private ImageButton mButtonPause;
    private ImageButton mButtonStop;
    private ImageButton mButtonReset;
    private RelativeLayout mActivityMain2;
    private SurfaceHolder mSurfaceView1Holder;

    public static final String TAG = "通知";
    private MediaPlayer mediaPlayer;
    private String path;
    boolean isPause; // 是否已经暂停了
    private MediaPlayer player;
    private SurfaceHolder holder;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
//        initData();
        initDatas();
    }

    private void initDatas() {
        player = new MediaPlayer();
        try {
            player.setDataSource(path);
            holder = mSurfaceView1.getHolder();
            holder.addCallback(this);
            player.prepare();

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    player.start();
                    player.setLooping(true);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        mSurfaceView1Holder = mSurfaceView1.getHolder();
        /**
         * 注册surefaceView创建。改变和销毁时调用的执行方法
         */
        mSurfaceView1Holder.addCallback(this);
        /**
         *  这里必须设置为SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS哦，意思
         *  是创建一个push的'surface'，主要的特点就是不进行缓冲
         */
        mSurfaceView1Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    private void initView() {
        mSurfaceView1 = (SurfaceView) findViewById(R.id.surfaceView1);
        mButtonPlay = (ImageButton) findViewById(R.id.button_play);
        mButtonPause = (ImageButton) findViewById(R.id.button_pause);
        mButtonStop = (ImageButton) findViewById(R.id.button_stop);
        mButtonReset = (ImageButton) findViewById(R.id.button_reset);

        mButtonPlay.setOnClickListener(this);
        mButtonPause.setOnClickListener(this);
        mButtonStop.setOnClickListener(this);
        mButtonReset.setOnClickListener(this);
        //获取与当前的surfaceViewg关连的那surefaceHolder
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                play();
                break;
            case R.id.button_pause:
                pause();
                break;
            case R.id.button_stop:
                stop();
                break;
            case R.id.button_reset:
                reset();
                break;
        }
    }

    private void reset() {
        if (mediaPlayer.isPlaying()) {
            Log.e(TAG, "reset:点击重置 ");
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            mediaPlayer.start();
        }

    }

    private void stop() {

        if (mediaPlayer.isPlaying()) {
            Log.e(TAG, "stop: 点击暂停");
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


    private void pause() {
        Log.e(TAG, "pause: 点击暂停按钮");
        if (mediaPlayer.isPlaying()) {
            if (isPause == false) {
                mediaPlayer.pause();
                isPause = true;
            } else {
                mediaPlayer.start();
                isPause = false;
            }
        }


    }

    private void play() {
        mediaPlayer = new MediaPlayer();
        // 设置多媒体流类型
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置用于展示mediaPlayer的容器
        mediaPlayer.setDisplay(mSurfaceView1Holder);
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPause = false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "play: 播放过程中出现错误哦");
            }
        } else {
            mediaPlayer.isLooping();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceCreated: 创建");
        player.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.e(TAG, "surfaceChanged:尺寸改变 ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceDestroyed: 销毁");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

    }
}
