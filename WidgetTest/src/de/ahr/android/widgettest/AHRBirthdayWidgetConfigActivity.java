package de.ahr.android.widgettest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


public class AHRBirthdayWidgetConfigActivity extends Activity {

    private static final String PREFS_NAME = AHRBirthdayWidgetConfigActivity.class.getName();
    NotificationManager notMan;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	public AHRBirthdayWidgetConfigActivity() {
		super();
	}

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        notMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("Config", "onCreate()" + PREFS_NAME);
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);
        
        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d("Config", "ID: " + mAppWidgetId);
        }
        
        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // Set the view layout resource to use.
        setContentView(R.layout.config);

        // Set the CheckBox state
        SharedPreferences prefs = AHRBirthdayWidgetConfigActivity.this.getSharedPreferences(PREFS_NAME, 0);
        ((CheckBox)findViewById(R.id.cb_graded_fontsize)).setChecked(prefs.getBoolean(mAppWidgetId + "_cb_graded_fontsize", true));        
        ((CheckBox)findViewById(R.id.cb_relative_date)).setChecked(prefs.getBoolean(mAppWidgetId + "_cb_relative_date", true));
        ((CheckBox)findViewById(R.id.cb_show_age)).setChecked(prefs.getBoolean(mAppWidgetId + "_cb_show_age", true));
        ((CheckBox)findViewById(R.id.cb_show_background)).setChecked(prefs.getBoolean(mAppWidgetId + "_cb_show_background", true));
        
        
//      //group membership info
//        String[] tempFields = new String[] {
//        		GroupMembership.GROUP_ROW_ID, GroupMembership.GROUP_SOURCE_ID, ContactsContract.Groups.TITLE};
//        Cursor cursor = managedQuery(ContactsContract.Data.CONTENT_URI, tempFields,
//                 ContactsContract.Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "'",
//                 null, null);
////         "+ ContactsContract.Groups.SUMMARY_COUNT + " > 0 AND
        
        final String[] GROUP_PROJECTION = new String[] { ContactsContract.Groups.SYSTEM_ID, ContactsContract.Groups.TITLE, ContactsContract.Groups.GROUP_VISIBLE, ContactsContract.Groups.ACCOUNT_NAME, ContactsContract.Groups.ACCOUNT_TYPE };
//       // final String GROUP_WHERE = ContactsContract.Groups.GROUP_VISIBLE + " = 1 AND (" + ContactsContract.Groups.ACCOUNT_TYPE + " = ? OR " + ContactsContract.Groups.ACCOUNT_TYPE + " = ?)";
//       // final String[] SEL_ARGS = new String[] {"com.google", "vnd.sec.contact.phone"};
//        final String GROUP_WHERE = ContactsContract.Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE + "'"; //ContactsContract.Groups.GROUP_VISIBLE + " = 1";
//        final String[] SEL_ARGS = new String[] {};
//        Cursor cursor = managedQuery(ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION, GROUP_WHERE, SEL_ARGS, ContactsContract.Groups.TITLE + " ASC");
        //   Cursor cursor = managedQuery(ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION, null, null, ContactsContract.Groups.TITLE + " ASC");

        Cursor cursor = managedQuery(
        		ContactsContract.Groups.CONTENT_URI,
        		GROUP_PROJECTION,
        		ContactsContract.Groups.GROUP_VISIBLE + " = 1",
        		null, 
        		null
        		);
        
        
        String groups = "";
        if (cursor.getCount() > 0) {
    	    while (cursor.moveToNext()) {
    	    	int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Groups.SYSTEM_ID));
    	        String group = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));	
    	        String act = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME));	
    	        String type = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_TYPE));
    	        if (group.startsWith("System Group:")) {
    	        	if (group.endsWith("Coworkers"))
    	        		group = getResources().getString(R.string.coworkers);
    	        	else if (group.endsWith("Family"))
    	        		group = getResources().getString(R.string.family);
    	        	else if (group.endsWith("Friends"))
    	        		group = getResources().getString(R.string.friends);
    	        	else if (group.endsWith("My Contacts"))
    	        		group = getResources().getString(R.string.my_contacts);
    	        }
    	        		
    	        groups += group + "\n";
    	        Log.d("Config", "Group: " + group + "("+id+")" + "("+act+")" + "["+type+"]");
    	    }
    	}
        
//        String where = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
//                + "="
//                + id
//                + " AND "
//                + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE
//                + "='"
//                + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE
//                + "'";
        
        ((TextView)findViewById(R.id.textView1)).setText(groups);
        

        // Bind the action for the save button.
        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = AHRBirthdayWidgetConfigActivity.this;

            SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
            prefs.putBoolean(mAppWidgetId + "_cb_show_age", ((CheckBox)findViewById(R.id.cb_show_age)).isChecked());
            prefs.putBoolean(mAppWidgetId + "_cb_graded_fontsize", ((CheckBox)findViewById(R.id.cb_graded_fontsize)).isChecked());
            prefs.putBoolean(mAppWidgetId + "_cb_relative_date", ((CheckBox)findViewById(R.id.cb_relative_date)).isChecked());
            prefs.putBoolean(mAppWidgetId + "_cb_show_background", ((CheckBox)findViewById(R.id.cb_show_background)).isChecked());
            prefs.commit();
            
            showNotification("Ich bin ein Notification", true, 1);

            // Push widget update to surface with newly set prefix
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            MyWidgetProvider.updateAppWidget(context, appWidgetManager,
//                    mAppWidgetId, titlePrefix);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

//
//    static void deleteTitlePref(Context context, int appWidgetId) {
//    }
//
//    static void loadAllTitlePrefs(Context context, ArrayList<Integer> appWidgetIds,
//            ArrayList<String> texts) {
//    }

	private void showNotification(String text, boolean ongoing, int id) {
		Notification notification = new Notification(android.R.drawable.stat_notify_sync, text,System.currentTimeMillis());
		
		if (ongoing) {
			notification.flags = Notification.FLAG_ONGOING_EVENT;
		} else {
			notification.flags = Notification.FLAG_AUTO_CANCEL;
		}
		
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 
				0,
				new Intent(getApplicationContext(),AHRBirthdayWidgetConfigActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT
		);
		
		notification.setLatestEventInfo(this, "NotificationActivity", text, contentIntent);
				
		notMan.notify(id, notification);
	}
		


}


/**********************************************************
 *
 * $Log: $
 * 
 **********************************************************/