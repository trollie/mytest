package de.ahr.android.widgettest;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AHRBirthdayWidgetAlarm extends BroadcastReceiver {
	private final String REMINDER_BUNDLE = "MyReminderBundle";
	private static AlarmManager alarmMgr;
	private static PendingIntent pendingIntent;

	// this constructor is called by the alarm manager.
	public AHRBirthdayWidgetAlarm() {
	}

	// you can use this constructor to create the alarm.
	// Just pass in the main activity as the context,
	// any extras you'd like to get later when triggered
	// and the timeout
	public AHRBirthdayWidgetAlarm(Context context, Bundle extras, int timeoutInSeconds) {
		
		if (alarmMgr != null && pendingIntent != null)
			alarmMgr.cancel(pendingIntent);
		
		
		alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(context, AHRBirthdayWidgetAlarm.class);
		if (extras != null)
			intent.putExtra(REMINDER_BUNDLE, extras);
		
		pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, timeoutInSeconds);
		
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), timeoutInSeconds*1000, pendingIntent);
		
		Toast.makeText(context, "new Alarm", Toast.LENGTH_SHORT).show();
		Log.d("ALARM", "new Alarm");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// here you can get the extras you passed in when creating the alarm
		// intent.getBundleExtra(REMINDER_BUNDLE));

		Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
		Log.d("ALARM", "onReceive()");
	}
	
	public static void cancelAlarm() {
		if (alarmMgr != null && pendingIntent != null) {
			alarmMgr.cancel(pendingIntent);
			Log.d("ALARM", "Alarm cleared");

		}
	}

}
