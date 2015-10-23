package com.javacodegeeks.sleepcycledetection;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

//MediaPlayer m=MediaPlayer.create(AlarmReceiver.this, R.raw.audio);

    @Override
    public void onReceive(Context context, Intent intent) {


        //m.start();
        Toast.makeText(context, "Time is up!!!!!", Toast.LENGTH_LONG).show();
        Vibrator vib=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        vib.vibrate(2000);

        Intent i=new Intent(context,song.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

}
