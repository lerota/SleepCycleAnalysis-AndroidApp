package com.javacodegeeks.sleepcycledetection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.javacodegeeks.sleepcycledetection.SleepCycleDetection;

public class song extends Activity{
    MediaPlayer m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song);
        m=MediaPlayer.create(getApplicationContext(), R.raw.gaji);
        //m=MediaPlayer.create(this, R.raw.gaji);
        m.start();

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(song.this);
        dlgAlert.setTitle("Reminder !");
        dlgAlert.setMessage("Wake up !");
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                m.stop();
                dialog.cancel();
                //System.exit(0);
                Intent intent1 = new Intent(song.this, SleepCycleDetection.class);
                startActivity(intent1);
            }
        });
        dlgAlert.show();
    }

}
