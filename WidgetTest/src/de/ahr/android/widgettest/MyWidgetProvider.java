package de.ahr.android.widgettest;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String LOG = "MyWidgetProvider";
	public static final String OPEN_CONTACT = "OPEN_CONTACT";
	public static final String ACTION_WIDGET_CONFIGURE = "ACTION_WIDGET_CONFIGURE";
	public static final String WIDGET_UPDATE = "WIDGET_UPDATE";
	
//    static AlarmManager alarmManager;
//    static PendingIntent pendingIntent;


	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		Log.i(LOG, "onReceive method called");
		if (intent.getAction().equals(OPEN_CONTACT)) {
//			Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, 2);
//			Intent uriIntent = new Intent(Intent.ACTION_VIEW, contactUri);
			Toast.makeText(context, "ID: " + intent.getExtras().getInt("ID", 0), Toast.LENGTH_LONG).show();
		}
		
		if (intent.getAction().equals(WIDGET_UPDATE)) {
			Toast.makeText(context, "AlarmManager Update!!!!", Toast.LENGTH_LONG).show();
			Log.i(LOG, "AlarmManager Update!!!!");
		}

	    if (intent.getExtras() != null) {
	    	Log.i(LOG, "intent Extras: " + intent.getExtras().keySet().toString());
	    }
	    
	    if (intent.getAction()==null) {
	    	Log.i(LOG, "intent has no Action");
	    }
	    else {
	    	Log.i(LOG, "intent Action: " + intent.getAction());
	    }
//	    else if (intent.getExtras() != null) {
//	        Bundle extras = intent.getExtras();
//	            int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//	            // do something for the widget that has appWidgetId = widgetId
//	    		Log.i(LOG, "WidgetId: " + widgetId);
//	    }
//	    else {
//	        super.onReceive(context, intent);
//	    }
////		// Build the intent to call the service
////		Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
////		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
////		// Update the widgets via the service
//		Log.i(LOG, "calling the service from onReceive");
//		context.startService(intent);
	}
	
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Toast.makeText(context, "AlarmManager Enabled!!!!", Toast.LENGTH_LONG).show();
		Log.i(LOG, "AlarmManager Enabled!!!!");
//		//prepare Alarm Service to trigger Widget
//		Intent intent = new Intent(MyWidgetProvider.WIDGET_UPDATE);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		Calendar calendar = Calendar.getInstance();
////		calendar.set(Calendar.HOUR, 0);
//		calendar.add(Calendar.MINUTE, 1);
////		calendar.set(Calendar.MINUTE, 0);
////		calendar.set(Calendar.SECOND, 10);
//		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
//				1000*60, pendingIntent);
		Bundle b = new Bundle(1);
		b.putString("alarm", "myAlarm");
		new AHRBirthdayWidgetAlarm(context, b, 90);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i(LOG, "onUpdate method called!");

		for (int i : appWidgetIds)
			Log.d(LOG, "ID: " + i);
		
		
		
//		if (alarmManager == null) {
//		//prepare Alarm Service to trigger Widget
//		   Intent intent = new Intent(MyWidgetProvider.WIDGET_UPDATE);
//		   intent.setAction(MyWidgetProvider.WIDGET_UPDATE);
//		   pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//		   alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//		   Calendar calendar = Calendar.getInstance();
//		   calendar.set(Calendar.HOUR, 0);
//		   calendar.add(Calendar.HOUR, 24);
//		   calendar.set(Calendar.MINUTE, 0);
//		   calendar.set(Calendar.SECOND, 10);
//		   alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//		}
		
		
//		// Get all ids
//		ComponentName thisWidget = new ComponentName(context,
//				MyWidgetProvider.class);
//		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

		// Update the widgets via the service
		Log.i(LOG, "calling the service");
		context.startService(intent);
	}
	
//	@Override
//	public void onDisabled(Context context) {
//		super.onDisabled(context);
//
//		alarmManager.cancel(pendingIntent);
//	}

	
	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, String titlePrefix) {
		
	}

	@Override
	public void onDisabled (Context context) {
		AHRBirthdayWidgetAlarm.cancelAlarm();
	}
	
}

/**********************************************************
 *
 * $Log: $
 * 
 **********************************************************/