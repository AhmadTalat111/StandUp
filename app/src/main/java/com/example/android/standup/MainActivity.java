package com.example.android.standup;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String toastMessage;
                if (isChecked) {
                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
                    if (alarmManager != null) {
                        alarmManager
                                .setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime,
                                        repeatInterval, notifyPendingIntent);
                    }
                    toastMessage = "Stand Up Alarm On!";

                } else {
                    mNotificationManager.cancelAll();
                    if(alarmManager != null){
                        alarmManager.cancel(notifyPendingIntent);
                    }
                    toastMessage = "Stand Up Alarm On!";
                }
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
        boolean alarmUp = (PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE)!= null);
        alarmToggle.setChecked(alarmUp);
        createNotificationChannel();
    }

    public void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, "Stand Up Notification!",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            mNotificationManager.createNotificationChannel(notificationChannel);

        }
    }

}
