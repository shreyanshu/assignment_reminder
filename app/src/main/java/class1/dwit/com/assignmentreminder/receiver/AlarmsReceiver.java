package class1.dwit.com.assignmentreminder.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import class1.dwit.com.assignmentreminder.MainActivity;
import class1.dwit.com.assignmentreminder.utils.AlarmUtils;
import class1.dwit.com.assignmentreminder.R;

/**
 * Created by sghatuwa on 3/27/17.
 */

public class AlarmsReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        System.out.println("I am running");
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        displayNotification(context);
    }

    protected void displayNotification(Context context ) {


        String title = context.getResources().getString(R.string.app_name);
        String body = "There is an assignment";
   /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        Intent nintent = new Intent();
        nintent.setClass(context, MainActivity.class);
        PendingIntent pin = PendingIntent.getActivity(context, 0, nintent, 0);

        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.setTicker(context.getResources().getString(R.string.app_name));
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setLights(Color.argb(0, 0, 0, 255), 1000, 750);
        mBuilder.setContentIntent(pin);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
       /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(1234563, mBuilder.build());
//        SharedPreferences prefs = context.getSharedPreferences("Alarm", context.MODE_PRIVATE);
//        int interval = prefs.getInt("interval", 15);

        AlarmUtils.setAlarm(context);

        Intent i = new Intent(context, MainActivity.class);


//        AlarmUtils.setAlarm();
    }
}
