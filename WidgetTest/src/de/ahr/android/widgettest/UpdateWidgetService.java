package de.ahr.android.widgettest;


import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	private static final String LOG = "UpdateWidgetService";

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(LOG, "onStart ");
		// Create some random data

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		int[] dateField = new int[] {R.id.date1, R.id.date2, R.id.date3, R.id.date4};
		int[] nameField = new int[] {R.id.name1, R.id.name2, R.id.name3, R.id.name4};
		
		String[] dow = getResources().getStringArray(R.array.day_of_week);
		String[] ldow = getResources().getStringArray(R.array.long_day_of_week);
		String[] month = getResources().getStringArray(R.array.month);
		
		Log.w(LOG, "Widgets: " + String.valueOf(allWidgetIds.length));
				
		RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_4_1);

		// Update Widget 
		Intent clickIntent = new Intent(this.getApplicationContext(), MyWidgetProvider.class);
		clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 
				0, 
				clickIntent,
				0);
		remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
		
		// Open Activity
		PendingIntent pI = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), AHRBirthdayWidgetActivity.class), 0);
		remoteViews.setOnClickPendingIntent(R.id.date1, pI);
		
		// Open Contact
		int contact_id = 2;
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contact_id); 
		Intent uriIntent = new Intent(Intent.ACTION_VIEW, contactUri);
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, uriIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.picture, pIntent);

		
		//remoteViews.setInt(R.id.layout, "setBackgroundResource", R.drawable.myshape);

		Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
           
        int i = 0;
        if (cursor.getCount() > 0) {
    	    while (cursor.moveToNext() && i < 4) {
    	    	int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
    	        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));	
		
				for (int widgetId : allWidgetIds) {
					// Open Widget Config
					Intent configIntent = new Intent(getApplicationContext(), AHRBirthdayWidgetConfigActivity.class);
			        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			        PendingIntent configPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, configIntent, 0);
			        remoteViews.setOnClickPendingIntent(R.id.name1, configPendingIntent);
					
					// Create some random data
					int days = id-1;
					int years = (new Random().nextInt(40));
					
					Calendar now = new GregorianCalendar();
					Calendar bd = (Calendar)now.clone();
					bd.add(Calendar.DAY_OF_YEAR, days);
					bd.add(Calendar.YEAR, -years);
					
					Calendar next = new GregorianCalendar(now.get(Calendar.YEAR), bd.get(Calendar.MONTH), bd.get(Calendar.DAY_OF_MONTH));
					if (next.before(now))
						next.add(Calendar.YEAR, 1);
					
					String dateString = null;
					switch (days) {
						case 0:
							dateString = getString(R.string.today);
							remoteViews.setTextColor(dateField[i], Color.WHITE);
							remoteViews.setTextColor(nameField[i], Color.WHITE);
							remoteViews.setFloat(dateField[i], "setTextSize", 13.0F);
							remoteViews.setFloat(nameField[i], "setTextSize", 13.0F);
							break;
						case 1:
							dateString = getString(R.string.tomorrow);
							remoteViews.setTextColor(dateField[i], Color.rgb(238, 238, 238));
							remoteViews.setTextColor(nameField[i], Color.rgb(238, 238, 238));
							remoteViews.setFloat(dateField[i], "setTextSize", 12.0F);
							remoteViews.setFloat(nameField[i], "setTextSize", 12.0F);
							break;
						case 2:
						case 3:
						case 4:
						case 5:
						case 6:
							dateString = ldow[days];
							remoteViews.setTextColor(dateField[i], Color.rgb(221, 221, 221));
							remoteViews.setTextColor(nameField[i], Color.rgb(221, 221, 221));
							remoteViews.setFloat(dateField[i], "setTextSize", 11.0F);
							remoteViews.setFloat(nameField[i], "setTextSize", 11.0F);
							break;
			
						default:
							dateString = dow[next.get(Calendar.DAY_OF_WEEK)] + ", " + next.get(Calendar.DAY_OF_MONTH) + "." + month[next.get(Calendar.MONTH)];
							remoteViews.setTextColor(dateField[i], Color.rgb(204, 204, 204));
							remoteViews.setTextColor(nameField[i], Color.rgb(204, 204, 204));
							remoteViews.setFloat(dateField[i], "setTextSize", 10.5F);
							remoteViews.setFloat(nameField[i], "setTextSize", 10.5F);
							break;
					}
					
					//Log.w("WidgetExample", widgetId + ": " + String.valueOf(number));
					// Set the text
					remoteViews.setTextViewText(
							dateField[i],
							dateString);
					remoteViews.setTextViewText(
							nameField[i],
							name + " (" + (next.get(Calendar.YEAR) - bd.get(Calendar.YEAR)) + ")" );
					
					
					Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
					
					//remoteViews.setImageViewBitmap(R.id.picture, buildUpdate("Ein TEST ist nicht genug!"));
					//remoteViews.setImageViewBitmap(R.id.picture, getRoundedCornerBitmap(buildUpdate("Ein TEST ist nicht genug!"), 10));
					remoteViews.setImageViewBitmap(R.id.picture, getRoundedCornerBitmap(bMap));
		
					//remoteViews.setImageViewBitmap(R.id.nextBirthday3, buildText("EIN TEST ist nicht genug"));
					appWidgetManager.updateAppWidget(widgetId, remoteViews);
				}
				i++;

            }
     	}
		stopSelf();

		super.onStart(intent, startId);
	}
	
	public Bitmap getPhoto(ContentResolver contentResolver, Long contactId) {
	    Uri contactPhotoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	    // contactPhotoUri --> content://com.android.contacts/contacts/1557

	    InputStream photoDataStream = Contacts.openContactPhotoInputStream(contentResolver,contactPhotoUri); // <-- always null
	    Bitmap photo = BitmapFactory.decodeStream(photoDataStream);
	    return photo;
	}
	
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff000000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = (bitmap.getWidth() + bitmap.getHeight()) / 2 / 10;

        paint.setAntiAlias(true);
        paint.setColor(color);
        
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
 		
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	public Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels , int w , int h , boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR  ) {

	    Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    final float densityMultiplier = context.getResources().getDisplayMetrics().density;

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, w, h);
	    final RectF rectF = new RectF(rect);

	    //make sure that our rounded corner is scaled appropriately
	    final float roundPx = pixels*densityMultiplier;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


	    //draw rectangles over the corners we want to be square
	    if (squareTL ){
	        canvas.drawRect(0, 0, w/2, h/2, paint);
	    }
	    if (squareTR ){
	        canvas.drawRect(w/2, 0, w, h/2, paint);
	    }
	    if (squareBL ){
	        canvas.drawRect(0, h/2, w/2, h, paint);
	    }
	    if (squareBR ){
	        canvas.drawRect(w/2, h/2, w, h, paint);
	    }

	    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	    canvas.drawBitmap(input, 0,0, paint);

	    return output;
	}


	
	public Bitmap buildUpdate(String time) {
		Bitmap myBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		Canvas myCanvas = new Canvas(myBitmap);
		Paint paint = new Paint();
		//Typeface clock = Typeface.createFromAsset(this.getAssets(), "Clockopia.ttf");
		//paint.setTypeface(clock);
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setTextSize(16);
		paint.setTextAlign(Align.CENTER);
		Paint aPaint = new Paint();
		aPaint.setColor(Color.rgb(64, 64, 64));
		Paint bPaint = new Paint();
		bPaint.setColor(Color.rgb(0, 0, 0));
		myCanvas.drawRect(0, 0, 100, 100, bPaint);
		//myCanvas.drawRoundRect(new RectF(0, 0, 100, 100), 12F, 12F, bPaint);
		//myCanvas.drawRoundRect(new RectF(2, 2, 98, 98), 10F, 10F, aPaint);
		myCanvas.drawText(time, 50, 50, paint);
		return myBitmap;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}

/**********************************************************
 *
 * $Log: $
 * 
 **********************************************************/