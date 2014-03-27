package com.example.xxxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.EventLog;
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
public class HomeFragment extends ListFragment implements OnScrollListener{
	
	/* Variables to restore the listView position */
	private int index;
	private View v;
	private int top;
	
	/* Variables to manage paging */
	private int currentPage  = 0;                   //  current page of the data          
	private int jsonAfter = 10;                      // Page after loading the json data
	
	JSONParser jsonParser = new JSONParser();
	
	ArrayList<HashMap<String, String>> newsUpdates;
	//ArrayAdapter<String> adapter;
	
	private ListView lv;
	private NewsAdapter ladapter;
	
	private String url_show_data = "http://www.xxxx.me/android/show_updates.php";
	
	    // JSON Node names
	    static final String TAG_SUCCESS = "success";
	    static final String TAG_DETAILS = "details";
		static final String TAG_USERID = "user_id";
		static final String TAG_POSTID = "post_id";
		static final String GEO_LOCATION = "geo_location";
		static final String TIME_PASSED = "time_passed";
		static final String NEWS_UPDATE = "news_update";
		static final String TICK_MARKS = "tick_marks";
		static final String RE_SHOUTS = "re_shouts";
		static final String CITY = "city";
		static final String COUNTRY = "country";
		static final String IMAGE1_URL = "image1_url";
		static final String SPAM_COUNT = "spam_count";
		
		private String user_id, post_id, geo_location, time_passed, news_update, tick_marks, re_shouts;
		private String city, country, spam_count, image1_url;
		
		private String email = null;
		
		// people JSONArray
		JSONArray details = null;
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ImageView map = (ImageView) rootView.findViewById(R.id.imageView1);
        
        lv = (ListView) rootView.findViewById(android.R.id.list);
        
        // Creating a button - Refresh
        Button refresh = new Button(getActivity());
        refresh.setText("Refresh");
      
        // Adding button to listview at footer
        lv.addHeaderView(refresh);
        
        refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LoadAllUpdates().execute();
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
		
		lv.setOnScrollListener(new OnScrollListener() {
		    @Override
		    public void onScroll(AbsListView view, int firstVisibleItem, 
		        int visibleItemCount, int totalItemCount) {
		        
		       
		    }

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

		    	if(listIsAtTop())
		    		Toast.makeText(getActivity(), "At top", Toast.LENGTH_LONG).show();
				
			}
		});
		
		lv.setOnScrollListener(new EndlessScroll(){
			 @Override
		        public void onLoadMore(int page, int totalItemsCount) {
		            // TODO Auto-generated method stub
		            new LoadAllUpdates().execute();
		        }
		});
		super.onActivityCreated(savedInstanceState);
    }
    
    class LoadAllUpdates extends AsyncTask<String, String, String> 
	{
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			
			/* Getting the top value and child value for restoring the listView position */
			index = lv.getFirstVisiblePosition();
			v = lv.getChildAt(0);
			top = (v == null) ? 0 : v.getTop();
			super.onPreExecute();
		}
		
		/**
		 * getting All people from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", Integer.toString(currentPage)));
			params.add(new BasicNameValuePair("end", Integer.toString(jsonAfter)));
			Log.d("URL", url_show_data);
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
						city = c.getString(CITY);
						country = c.getString(COUNTRY);
						image1_url = c.getString(IMAGE1_URL);
						spam_count = c.getString(SPAM_COUNT);

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
						map.put(CITY, city);
						map.put(COUNTRY, country);
						map.put(IMAGE1_URL, image1_url);
						map.put(SPAM_COUNT, spam_count);

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
		

		protected void onPostExecute(String file_url) {
                  //updating list view with the parsed items
                    
                  /*  adapter = new SimpleAdapter(getActivity().getApplicationContext(), newsUpdates,
                            R.layout.landing_list,
                            new String[] {GEO_LOCATION, TIME_PASSED, NEWS_UPDATE, TICK_MARKS, RE_SHOUTS}, 
                            new int[] {R.id.GeoLocation,R.id.TimePassed, R.id.NewsUpdate, R.id.TickMarks, R.id.ReShouts});
                     
                    setListAdapter(adapter); */
                    
                    ladapter = new NewsAdapter(getActivity().getApplicationContext(), newsUpdates);
                
                    setListAdapter(ladapter);
                    
                    (ladapter).notifyDataSetChanged();
                    
                    /* Restoring the position of list view */
                    lv.setSelectionFromTop(index, top);
                    
                    /* Changing values of current and json after for pagin */
                    currentPage = currentPage + 11;
                    jsonAfter = jsonAfter + 10;
                    
		}
  }
    
    private boolean listIsAtTop()   {   
        if(lv.getChildCount() == 0) return true;
        return lv.getChildAt(0).getTop() == 0;
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
