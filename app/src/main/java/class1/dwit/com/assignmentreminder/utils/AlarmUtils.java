package class1.dwit.com.assignmentreminder.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import class1.dwit.com.assignmentreminder.App;
import class1.dwit.com.assignmentreminder.MainActivity;
import class1.dwit.com.assignmentreminder.R;
import class1.dwit.com.assignmentreminder.database.DatabaseHelper;
import class1.dwit.com.assignmentreminder.domain.Assignment;
import class1.dwit.com.assignmentreminder.receiver.AlarmsReceiver;

/**
 * Created by User on 5/3/2017.
 */

public class AlarmUtils
{

    public static void setAlarm(Context c)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(c);
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(c.ALARM_SERVICE);

//        Context applicationContext = MainActivity.getContextOfApplication();
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences sharedPref = c.getSharedPreferences("this_batch",Context.MODE_PRIVATE);
        String defaultValue = "2018";
        String batch = sharedPref.getString(c.getString(R.string.classes), defaultValue);
        final String bat = batch.substring(batch.length()-4);
//        String defaultValue = "2018";
//        String batch = sharedPref.getString(get(R.string.classes), defaultValue);

//        int batch = R.string.classes;
//        String batchs = Integer.toString(batch);
//        String defaultValue = "2018";
//        String batch = sharedPref.getString(App.getContext().getResources().getString(R.string.classes), defaultValue);

        Assignment assignment = databaseHelper.getNextAlert(bat);
        Intent alarmIntent = new Intent(c, AlarmsReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, alarmIntent, 0);
        Calendar calendar = Calendar.getInstance();
        long epoch = Long.parseLong(assignment.getDeadline())*1000;
//        Date date = new Date(epoch); // 'epoch' in long

        epoch -= 10800000;

        calendar.setTimeInMillis(epoch);

//        String alarm =

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(c, "Alarm Started" + epoch + " " + bat , Toast.LENGTH_SHORT).show();
    }
}
