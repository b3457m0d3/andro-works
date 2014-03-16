package com.example.shoutoutloud;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GooglePlaces {
	
	private static final String LOG_TAG = "GooglePlaces";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch";
	private static final String RADIUS = "200";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyAeJrd0zKMhovJu4IRa8wB2hrG1qSq-8F8";
	
	private static ArrayList <String> PlacesList = null;
	private static String PlacesArray [] = null;
	private static String NewPlacesArray[] = null;
	
	static String [] ReturnPlaces (String Latitude, String Longitude){
		
		HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();

	    try{
	    	StringBuilder sb = new StringBuilder(PLACES_API_BASE + OUT_JSON);
	    	sb.append("?location=" + Latitude + ","+Longitude);
	    	sb.append("&radius="+RADIUS);
	        sb.append("&sensor=false");
	        sb.append("&key="+API_KEY);
	        
	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        
	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	       } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return PlacesArray;
	       } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return PlacesArray;
	       } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	    
	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("results");

	        // Extract the Place descriptions from the results
	        PlacesList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            PlacesList.add(predsJsonArray.getJSONObject(i).getString("name"));
	            PlacesList.add(predsJsonArray.getJSONObject(i).getString("vicinity"));
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }
	    
	    PlacesArray = new String[PlacesList.size()];
	    NewPlacesArray = new String[(PlacesList.size()/2)];
	    PlacesArray = PlacesList.toArray(PlacesArray);
	   /* 
	    for(int p=0; p<(PlacesList.size()-1); p=p+2){
	    	{NewPlacesArray[p] = PlacesArray[p]+"\n\n"+PlacesArray[p+1];
	    	Log.d(LOG_TAG, NewPlacesArray[p]);}
	    } */

		return PlacesArray;
	}
}
