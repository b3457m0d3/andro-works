package com.example.xxxx;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class MapView extends Activity {
	
    private GoogleMap mMap;
    private MapFragment mMapFragment;
    
    private double latitude = 0;
    private double longitude = 0;
    
    GPSTracker gps;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		
		gps = new GPSTracker(MapView.this);
		if(gps.canGetLocation())
		{
			latitude = gps.getLatitude(); // returns latitude
			longitude = gps.getLongitude(); // returns longitude
		}
		else
		{
			gps.showSettingsAlert();
		}

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		// Set the map type to normal
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		 Circle circle = mMap.addCircle(new CircleOptions()
	     .center(new LatLng(latitude, longitude))
	     .radius(2000));

		Marker marker = mMap.addMarker(new MarkerOptions()
                         .position(new LatLng(53.558, 9.927))
                         .title("Hello world")
                         .snippet("The fuck is this"));
		
		// Move the camera instantly to hamburg with a zoom of 15.
	    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

	    // Zoom in, animating the camera.
	    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

	}
	
	/*private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	                            .getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.

	        }
	    }
	}*/

}
