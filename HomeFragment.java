package com.example.xxxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
 
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class HomeFragment extends ListFragment{
	
	private int currentPage  = 0;                   //  current page of the data          
	private int jsonAfter = 10;                      // Page after loading the json data
	
	JSONParser jsonParser = new JSONParser();
	
	ArrayList<HashMap<String, String>> newsUpdates;
	//ArrayAdapter<String> adapter;
	
	private ListView lv;
	private ListAdapter adapter;
	
	private String url_show_data = "xxxxxxxxxxx" + "?start=" + currentPage + "&end=" + jsonAfter;
	
	    // JSON Node names
	    private static final String TAG_SUCCESS = "success";
	    private static final String TAG_DETAILS = "details";
		private static final String TAG_USERID = "user_id";
		private static final String TAG_POSTID = "post_id";
		private static final String GEO_LOCATION = "geo_location";
		private static final String TIME_PASSED = "time_passed";
		private static final String NEWS_UPDATE = "news_update";
		private static final String TICK_MARKS = "tick_marks";
		private static final String RE_SHOUTS = "re_shouts";
		
		String user_id, post_id, geo_location, time_passed, news_update, tick_marks, re_shouts;
		
		private String email = null;
		
		// people JSONArray
		JSONArray details = null;
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        
        ImageView map = (ImageView) rootView.findViewById(R.id.imageView1);
        
        lv = (ListView) rootView.findViewById(android.R.id.list);
        
        
        // Creating a button - Load More
        Button btnLoadMore = new Button(getActivity());
        btnLoadMore.setText("Load More");
        
        // Creating a button - Refresh
        Button refresh = new Button(getActivity());
        refresh.setText("Refresh");
      
        // Adding button to listview at footer
        lv.addFooterView(btnLoadMore);
        lv.addHeaderView(refresh);
        
        refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LoadAllUpdates().execute();
			}
		});
        
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
                Toast.makeText(getActivity(), currentPage + " - " + jsonAfter + url_show_data, Toast.LENGTH_SHORT).show();

                new LoadAllUpdates().execute();
                ((BaseAdapter) adapter).notifyDataSetChanged();
                
                lv.smoothScrollByOffset(currentPage);
			}
		});
        
        map.bringToFront();
        
        email = LandingPage.email_id;
        
        map.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), MapView.class);
				intent.putExtra("email_id", email);
				startActivity(intent);
				
			}
		});

        // Hashmap for ListView
		newsUpdates = new ArrayList<HashMap<String, String>>();
		
        return rootView;
    }
    
    public void onActivityCreated (Bundle savedInstanceState){
    	
        // Loading people in Background Thread
		new LoadAllUpdates().execute();
		super.onActivityCreated(savedInstanceState);
    }
    
    class LoadAllUpdates extends AsyncTask<String, String, String> 
	{
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		/**
		 * getting All people from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			url_show_data = url_show_data + "?start=" + currentPage + "&end=" + jsonAfter;
			
			currentPage = currentPage + 11;
            jsonAfter = jsonAfter + 10;
			
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(url_show_data, "GET", params);
			// Check your log cat for JSON reponse
			Log.d("The List is: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// people found
					// Getting Array of people
					details = json.getJSONArray(TAG_DETAILS);
					Log.d("Debug", "working well");
                    
					// looping through All people
					for (int i = 0; i < details.length(); i++) {
						JSONObject c = details.getJSONObject(i);

						// Storing each json item in variable
						user_id = c.getString(TAG_USERID);
						post_id = c.getString(TAG_POSTID);
						geo_location = c.getString(GEO_LOCATION);
					    time_passed = c.getString(TIME_PASSED);
						news_update = c.getString(NEWS_UPDATE);
						tick_marks = c.getString(TICK_MARKS);
						re_shouts = c.getString(RE_SHOUTS);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_USERID, user_id);
						map.put(TAG_POSTID, post_id);
						map.put(GEO_LOCATION, geo_location);
						map.put(TIME_PASSED, time_passed);
						map.put(NEWS_UPDATE, news_update);
						map.put(TICK_MARKS, tick_marks);
						map.put(RE_SHOUTS, re_shouts);

						// adding HashList to ArrayList
						newsUpdates.add(map);
					}
				}
				else
				{
					Log.d("Debug", "Cannot fetch data");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} 
			return null;
	}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/

		protected void onPostExecute(String file_url) {
                  //updating list view with the parsed items

                    adapter = new SimpleAdapter(getActivity().getApplicationContext(), newsUpdates,
                            R.layout.landing_list,
                            new String[] {GEO_LOCATION, TIME_PASSED, NEWS_UPDATE, TICK_MARKS, RE_SHOUTS}, new int[] 

                         {R.id.GeoLocation,R.id.TimePassed, R.id.NewsUpdate, R.id.TickMarks, R.id.ReShouts});
                    setListAdapter(adapter);
                
		}
  }
}
