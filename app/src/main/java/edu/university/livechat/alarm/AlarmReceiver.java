package edu.university.livechat.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import edu.university.livechat.R;
import edu.university.livechat.ui.login.LoginActivity;

@SuppressWarnings("deprecation")
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // set up notification manager
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // set up redirect intent
        Intent redirectIntent = new Intent(context, LoginActivity.class);
        //set up pending intent for the notification
        Notification notify = new Notification.Builder(context.getApplicationContext())
                .setContentText("Your friends are waiting for you! Click here to chat!")
                .setContentTitle("Reminder")
                .setContentIntent(PendingIntent.getActivity(context, 0, redirectIntent, PendingIntent.FLAG_IMMUTABLE))
                .setSmallIcon(R.drawable.chat_main_logo)
                .setChannelId("mainChannel")
                .build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notify);
    }
}
