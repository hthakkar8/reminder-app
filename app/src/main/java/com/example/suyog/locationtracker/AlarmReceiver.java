package com.example.suyog.locationtracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by abc on 10/3/2017.
 */

public class AlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        createNotification(context,"Reminding You","Your Reminder","Alert");
        Log.i("HEY","IN ALARM");
    }

    private void createNotification(Context context, String msg, String msgText, String alert)
    {
        PendingIntent pi = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(msg).setContentText(msgText).setTicker(alert).setSmallIcon(R.drawable.icon);

        mBuilder.setContentIntent(pi);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(1,mBuilder.build());
    }


}
