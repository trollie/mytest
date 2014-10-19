/**
 * 
 */
package de.ahr.android.widgettest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author aruehl
 *
 */
public class NextEventListActivity extends Activity {
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        TextView textview = new TextView(this);
	        textview.setText("This is the next event tab");
	        setContentView(textview);
	    }

}
