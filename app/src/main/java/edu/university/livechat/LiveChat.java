package edu.university.livechat;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import edu.university.livechat.alarm.AlarmReceiver;

public class LiveChat extends android.app.Application {
    // by default when the app is started, notifications are disabled
    private static final String PREFS_NAME = "MyPrefs";
    // possible values: open, closed
    private static final String APP_STATE = "appState";

    @Override
    public void onCreate() {
        super.onCreate();
        // set the notification checks and start repeating alarm, and create notification channel
        // if notifications are enabled
        setAppState(getApplicationContext(), "open");
        startAlarmWithNotificationCheck();
    }

    @Override
    public void onTerminate() {
        // when the app is terminated, set the notification checks to disabled
        setAppState(getApplicationContext(), "closed");
        super.onTerminate();

    }

    private void createNotificationChannel() {
        CharSequence name = "Main Channel";
        String description = "This channel is used for notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("mainChannel", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    @SuppressWarnings("deprecation")
    private void startAlarm() {
        // get the context manager
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // get the list of running services & alarms
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        // if the list is null, return
        if (runningServices == null) return;
        // if the list is empty, create alarm
        if (runningServices.isEmpty()) {
            // create alarm
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // set the intent to be sent to the AlarmReceiver
            Intent intent = new Intent(this, AlarmReceiver.class);

            // set the intent to be immutable, since it will be reused
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            // every 1 minute, since lower than 1 minute is not allowed
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000L, pendingIntent);
        }

    }
    // check if notifications are enabled
    private void startAlarmWithNotificationCheck() {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            startAlarm();
            createNotificationChannel();
        }
    }
    public void setAppState(Context context, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(APP_STATE, value);
        editor.apply();
        System.out.println("appState has been set");
    }
    @SuppressWarnings("unused")
    public String getAppState() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(APP_STATE, null);
    }
}