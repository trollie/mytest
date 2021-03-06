package de.ahr.android.widgettest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class AHRBirthdayWidgetActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        ArrayList<SearchResults> searchResults = GetSearchResults();
       
        final ListView lv1 = (ListView) findViewById(R.id.ListView01);
        lv1.setAdapter(new MyCustomBaseAdapter(this, searchResults));
       
	        lv1.setOnItemClickListener(new OnItemClickListener() {
	         @Override
	         public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	          Object o = lv1.getItemAtPosition(position);
	          SearchResults fullObject = (SearchResults)o;
	          Toast.makeText(AHRBirthdayWidgetActivity.this, "You have chosen: " + " " + fullObject.getName(), Toast.LENGTH_LONG).show();
	         } 
	        });
    }
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.config:
            	Intent intent = new Intent(AHRBirthdayWidgetActivity.this, AHRBirthdayWidgetConfigActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private ArrayList<SearchResults> GetSearchResults(){
     ArrayList<SearchResults> results = new ArrayList<SearchResults>();
     
     SearchResults sr1 = new SearchResults();
     sr1.setName("John Smith");
     sr1.setCityState("Dallas, TX");
     sr1.setPhone("214-555-1234");
     results.add(sr1);
     
     sr1 = new SearchResults();
     sr1.setName("Jane Doe");
     sr1.setCityState("Atlanta, GA");
     sr1.setPhone("469-555-2587");
     results.add(sr1);
     
     sr1 = new SearchResults();
     sr1.setName("Steve Young");
     sr1.setCityState("Miami, FL");
     sr1.setPhone("305-555-7895");
     results.add(sr1);
     
     sr1 = new SearchResults();
     sr1.setName("Fred Jones");
     sr1.setCityState("Las Vegas, NV");
     sr1.setPhone("612-555-8214");
     results.add(sr1);
     
     return results;
    }

}