package com.feedthewolf.nhlpickem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by nmoccia on 2/3/2017.
 */
public class Notification_Reciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        PickReminderNotification pickReminderNotification = new PickReminderNotification();

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (    !sharedPreferences.getBoolean(context.getResources().getString(R.string.preference_has_pick_reminder_been_sent_today), false) &&
                sharedPreferences.getBoolean(context.getResources().getString(R.string.preference_are_there_games_today), false))
        {
            notificationManager.notify(101, pickReminderNotification.build(context, 1));
            editor.putBoolean(context.getResources().getString(R.string.preference_has_pick_reminder_been_sent_today), true);
        }
        editor.apply();
    }
}
