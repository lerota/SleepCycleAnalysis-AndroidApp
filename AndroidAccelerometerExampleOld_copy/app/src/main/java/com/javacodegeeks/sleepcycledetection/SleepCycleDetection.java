package com.javacodegeeks.sleepcycledetection;


//import org.rosuda.REngine.*;
//import org.rosuda.REngine.Rserve.*;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import java.io.*;

import android.view.View;
import android.view.View.OnClickListener;

//Alarm
import android.app.Activity;
import android.os.Bundle;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SleepCycleDetection extends Activity implements SensorEventListener, OnClickListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private FileWriter writer;
    private Button btnStart, btnStop;
    private boolean started = false;

    File root, dir, sensorFile;

    private float X = 0;
    private float Y = 0;
    private float Z = 0;

    private TextView currentX, currentY, currentZ;

    int label = 0;

//Alarm instance variables
    TimePicker TimePicker;
    DatePicker DatePicker;
    Button Setalarm;
    Button Stopalarm;

    TimePickerDialog timePickerDialog;

    final static int RQS_1 = 1;
//end here

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        root = android.os.Environment.getExternalStorageDirectory();
        dir = new File(root.getAbsolutePath() + "/myapp");
        //dir.mkdirs();

        //File oldFile = new File(dir, "data.txt");

        //boolean deleted = oldFile.delete();
        //System.out.println("Delete status = " + deleted);
        //Calendar curr = Calendar.getInstance();
        String time = String.valueOf(System.currentTimeMillis());
        sensorFile = new File(dir, time +".txt");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            //	vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // fail! we don't have an accelerometer!
        }

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Alarm from here
        DatePicker =(DatePicker)findViewById(R.id.datePicker1);
        TimePicker=(TimePicker)findViewById(R.id.timePicker1);
        Calendar now = Calendar.getInstance();

        DatePicker.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);

        TimePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        TimePicker.setCurrentMinute(now.get(Calendar.MINUTE));

        Setalarm = (Button) findViewById(R.id.btnStart);
        Setalarm.setOnClickListener(this);

        Stopalarm = (Button) findViewById(R.id.btnStop);
        Stopalarm.setOnClickListener(this);
        //Setalarm.setOnClickListener(new OnClickListener() {

            //public void onClick(View arg0) {
            //    Calendar current = Calendar.getInstance();
            //
            //    Calendar cal = Calendar.getInstance();
            //    cal.set(DatePicker.getYear(),
            //            DatePicker.getMonth(),
            //            DatePicker.getDayOfMonth(),
            //            TimePicker.getCurrentHour(),
            //            TimePicker.getCurrentMinute(),
            //            00);
            //
            //    if(cal.compareTo(current) <= 0){
            //        //The set Date/Time already passed
            //        Toast.makeText(getApplicationContext(),
            //                "Invalid Date/Time",
            //                Toast.LENGTH_LONG).show();
            //    }else{
            //        setAlarm(cal);
            //    }

            //}


    }

    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        try {
            writer = new FileWriter(sensorFile, true);
            writer.write("X"+","+"Y"+","+"Z"+","+"timestamp"+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(started){
            // clean current values
            displayCleanValues();
            // display the current x,y,z accelerometer values
            displayCurrentValues();

            // get the x,y,z values of the accelerometer
            X = event.values[0];
            Y = event.values[1];
            Z = event.values[2];
            long timestamp=System.currentTimeMillis();
            try {
                writer.write(X+","+Y+","+Z+","+timestamp+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(Float.toString(X));
        currentY.setText(Float.toString(Y));
        currentZ.setText(Float.toString(Z));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnStart:

                Calendar current = Calendar.getInstance();

                Calendar cal = Calendar.getInstance();
                cal.set(DatePicker.getYear(),
                        DatePicker.getMonth(),
                        DatePicker.getDayOfMonth(),
                        TimePicker.getCurrentHour(),
                        TimePicker.getCurrentMinute(),
                        00);

                if(cal.compareTo(current) <= 0){
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(),
                            "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                }else{
                    label = 1;
                    setAlarm(cal);
                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);
                    started = true;

                    sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
                }

                break;

            case R.id.btnStop:
                Toast.makeText(getApplicationContext(),
                        "Stop Tracking Sleep",
                        Toast.LENGTH_LONG).show();
                stopAlarm();
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                started = false;

                sensorManager.unregisterListener(this);
                break;

            default:
                break;
        }


    }

    //Alarm function from here
    private void setAlarm(Calendar targetCal) {

        //
        Toast.makeText(SleepCycleDetection.this, "Alarm is set at " + targetCal.getTime(),
                Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        //Intent intent = new Intent(this, AlarmReceiver.class);
        Intent intent = new Intent(SleepCycleDetection.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                SleepCycleDetection.this, RQS_1, intent, 0);
        //getBaseContext().startActivity(intent);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(
        //        this, RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

    }

    private void stopAlarm() {

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SleepCycleDetection.this, RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }

    @Override
    public void onBackPressed() {
        if(label==0){
            sensorFile.delete();
        }
        stopAlarm();
        started = false;
        sensorManager.unregisterListener(this);
        //Intent i= new Intent("package.homescreenactivity");//homescreen of your app.
        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        //startActivity(i);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

}