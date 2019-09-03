package com.armavi.medi.time;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.armavi.medi.time.ui.MainActivity;

public class Notifier extends Language {

    ImageView imgAlrt;
    TextView mednameAlrt, medTypeAlrt, medDialogAlrt, scheduleAlrt;
    public MediaPlayer medAlarm;
    Button alarmOff;
    boolean doubleBackToExitPressedOnce = false;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifier);

        loadLocale();

        imgAlrt = (ImageView) findViewById(R.id.imgAlrt);
        medDialogAlrt = (TextView) findViewById(R.id.dialogAlrt);
        mednameAlrt = (TextView) findViewById(R.id.medNameAlrt);
        medTypeAlrt = (TextView) findViewById(R.id.medTypeAlrt);
        scheduleAlrt = (TextView) findViewById(R.id.scheduleAlrt);
        alarmOff = (Button) findViewById(R.id.stopAlarm);

        mednameAlrt.setText(getIntent().getStringExtra("med_label_key"));
        medTypeAlrt.setText(getIntent().getStringExtra("med_type_key"));
        scheduleAlrt.setText(getIntent().getStringExtra("med_schedule_key"));

        medDialogAlrt.setText(getString(R.string.dialog)+" "+getIntent().getStringExtra("med_label_key")+" "+getIntent().getStringExtra("med_type_key")+" "+getIntent().getStringExtra("med_schedule_key"));

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000,1000,1000,1000,1000};

        medAlarm =  MediaPlayer.create(getApplicationContext(),R.raw.loud_msg);

        medAlarm.start();
        medAlarm.setLooping(true);

        vibrator.vibrate(pattern, 0);

        alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medAlarm.stop();
                vibrator.cancel();
            }
        });


        //For Medicine Types
        if(getIntent().getStringExtra("med_type_key").equals("Tablet")){

            imgAlrt.setImageResource(R.drawable.tablet);
            medTypeAlrt.setText(getResources().getString(R.string.tablet));

        }else if(getIntent().getStringExtra("med_type_key").equals("Capsule")){

            imgAlrt.setImageResource(R.drawable.capsule);
            medTypeAlrt.setText(getResources().getString(R.string.capsule));

        }else if(getIntent().getStringExtra("med_type_key").equals("Syrup")){

            imgAlrt.setImageResource(R.drawable.syrup);
            medTypeAlrt.setText(getResources().getString(R.string.syrup));

        }else if(getIntent().getStringExtra("med_type_key").equals("Injection")){

            imgAlrt.setImageResource(R.drawable.injection);
            medTypeAlrt.setText(getResources().getString(R.string.injection));

        }else if(getIntent().getStringExtra("med_type_key").equals("Saline")){

            imgAlrt.setImageResource(R.drawable.saline);
            medTypeAlrt.setText(getResources().getString(R.string.saline));

        }else if(getIntent().getStringExtra("med_type_key").equals("Ointment")){

            imgAlrt.setImageResource(R.drawable.oinment);
            medTypeAlrt.setText(getResources().getString(R.string.ointment));

        }else if(getIntent().getStringExtra("med_type_key").equals("Not Known")){

            imgAlrt.setImageResource(R.drawable.bot);
            medTypeAlrt.setText(getResources().getString(R.string.not_know));

        }

        //For Meal Count
        if(getIntent().getStringExtra("med_schedule_key").equals("After Meal")){
            scheduleAlrt.setText(getResources().getString(R.string.after_meal));
        }else if(getIntent().getStringExtra("med_schedule_key").equals("Before Meal")){
            scheduleAlrt.setText(getResources().getString(R.string.before_meal));
        }else if(getIntent().getStringExtra("med_schedule_key").equals("Anytime")){
            scheduleAlrt.setText(getResources().getString(R.string.anytime));
        }

        //Dialog
        String medTypeDialog = medTypeAlrt.getText().toString();
        String medScheduleDialog = scheduleAlrt.getText().toString();
        medDialogAlrt.setText(getString(R.string.dialog)+" "+getIntent().getStringExtra("med_label_key")+" "+medTypeDialog+" "+medScheduleDialog);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please Double Tap in Back to exit !", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                medAlarm.stop();
                vibrator.cancel();
                startActivity(new Intent(Notifier.this, MainActivity.class));
                finish();
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
