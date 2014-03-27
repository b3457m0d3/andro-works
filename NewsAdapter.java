package com.example.xxxx;

import java.util.ArrayList;
import java.util.HashMap;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

@SuppressLint("NewApi")
public class NewsAdapter extends BaseAdapter {

	private static LayoutInflater inflater=null;
	private Context context;
	ArrayList<HashMap<String, String>>data;
    public ImageLoader imageLoader; 
    
    public static HashMap<String, String> news_updates;
    
    public NewsAdapter(Context a,  ArrayList<HashMap<String, String>> d) {
        context = a;
        data = d;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(context);
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View vi = convertView;
		
		if(convertView == null)
			vi = inflater.inflate(R.layout.landing_list, null);
		
		TextView geoLocation = (TextView)vi.findViewById(R.id.GeoLocation);
		TextView timePassed = (TextView)vi.findViewById(R.id.TimePassed);
		TextView newsUpdate = (TextView)vi.findViewById(R.id.NewsUpdate);
		TextView tickMarks = (TextView)vi.findViewById(R.id.TickMarks);
		TextView reShouts = (TextView)vi.findViewById(R.id.ReShouts);
        ImageView image1 = (ImageView)vi.findViewById(R.id.image1);
		
        news_updates = new HashMap<String, String>();
        news_updates = data.get(position);
        
        geoLocation.setText(news_updates.get(HomeFragment.GEO_LOCATION));
        timePassed.setText(news_updates.get(HomeFragment.TIME_PASSED));
        newsUpdate.setText(news_updates.get(HomeFragment.NEWS_UPDATE));
        tickMarks.setText(news_updates.get(HomeFragment.TICK_MARKS));
        reShouts.setText(news_updates.get(HomeFragment.RE_SHOUTS));
        if(!(news_updates.get(HomeFragment.IMAGE1_URL).equals(null)))
        {
        	UrlImageViewHelper.setUrlDrawable(image1, news_updates.
        			get(HomeFragment.IMAGE1_URL), R.drawable.stub);
        }
		return vi;
	}
   
}
