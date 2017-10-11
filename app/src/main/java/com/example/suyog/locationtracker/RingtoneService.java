package com.example.suyog.locationtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;


public class RingtoneService extends Service {


    MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if( mMediaPlayer != null){
            Log.i("Fence","mMediaPlayer not Null");
            mMediaPlayer.stop();
            mMediaPlayer.seekTo(0);
            mMediaPlayer=null;
        }
        else {
            Log.i("Fence","mMediaPlayer Null");
            mMediaPlayer = MediaPlayer.create(this, R.raw.one);
            mMediaPlayer.start();

        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.i("RING","STOP");

    }







}
