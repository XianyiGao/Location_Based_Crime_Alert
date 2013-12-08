package com.example.location_based_crime_alert;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.TimeZone;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class myMap extends FragmentActivity implements LocationListener,OnMyLocationButtonClickListener{

	private GoogleMap mMap;
	  private Marker m1,m2,m3,m4,m5;
	  LatLng p; 
	  DecimalFormat df;
	  Double CurrentLat=0.0,CurrentLng=0.0;
	  
	  
	  
	 
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	     df = new DecimalFormat("###.##");
	    setUpMapIfNeeded(); 
	    
	    	p=new LatLng(40.517828 , -74.465292);
	     m2 =mMap.addMarker(new MarkerOptions().position(p).title("Mickey"));
	     	mMap.addCircle(new CircleOptions().center(p).radius(100.0));
	     	
	     p=new LatLng(40.513179 , -74.433839);
	     	mMap.addCircle(new CircleOptions().center(p).radius(100.0));
	     m3 =mMap.addMarker(new MarkerOptions().position(p).title("Donald"));
	     
	     p=new LatLng(40.495526 , -74.467141);
	     m4 =mMap.addMarker(new MarkerOptions().position(p).title("Goofy"));
	     mMap.addCircle(new CircleOptions().center(p).radius(100.0));  
	     
	     p= new LatLng(40.497125 , -74.417059);
	     mMap.addCircle(new CircleOptions().center(p).radius(100.0));
	    m5 =mMap.addMarker(new MarkerOptions().position(p).title("Garfield"));
	    

	    mMap.setOnMarkerClickListener(new OnMarkerClickListener(){


	    	@Override
			public boolean onMarkerClick(Marker marker) {
				
				// TODO Auto-generated method stub
				
				DateFormat d = DateFormat.getDateTimeInstance();
				d.setTimeZone(TimeZone.getTimeZone("EST"));
				float []results = new float[3];
				
				if(marker.getTitle().equalsIgnoreCase("Mickey"))
				{
		    	   
		     	   Location.distanceBetween(m1.getPosition().latitude,m1.getPosition().longitude, marker.getPosition().latitude, marker.getPosition().longitude,results);
		     	   marker.setSnippet("Distance:"+df.format(results[0]*0.000621371)+"miles ; updated "+ d.format(System.currentTimeMillis()) );
		     	   
				}
				else if(marker.getTitle().equalsIgnoreCase("donald"))
					{
					
					Location.distanceBetween(m1.getPosition().latitude,m1.getPosition().longitude, marker.getPosition().latitude, marker.getPosition().longitude,results);
			     	   marker.setSnippet("Distance:"+df.format(results[0]*0.000621371)+"miles ; updated "+ d.format(System.currentTimeMillis()) );
					
					}
		       else if(marker.getTitle().equalsIgnoreCase("goofy"))
					{ 
		    	   Location.distanceBetween(m1.getPosition().latitude,m1.getPosition().longitude, marker.getPosition().latitude, marker.getPosition().longitude,results);
		     	   marker.setSnippet("Distance:"+df.format(results[0]*0.000621371)+"miles ; updated "+ d.format(System.currentTimeMillis()) );
		     	   }
		       else if(marker.getTitle().equalsIgnoreCase("garfield"))
					
		    	   {
		    	         
		    	   Location.distanceBetween(m1.getPosition().latitude,m1.getPosition().longitude, marker.getPosition().latitude, marker.getPosition().longitude,results);
		     	   marker.setSnippet("Distance:"+df.format(results[0]*0.000621371)+"miles ; updated "+ d.format(System.currentTimeMillis()) );
		     	   }
		       else marker.showInfoWindow();
				return false;
			}
	    	
	    });
	    
	    
	  }
 
	  
	  private void setUpMapIfNeeded() {
	        // Do a null check to confirm that we have not already instantiated the map.
	        if (mMap == null) {
	            // Try to obtain the map from the SupportMapFragment.
	            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	            // Check if we were successful in obtaining the map.
	            if (mMap != null) {
	                setUpMap();
	                mMap.setMyLocationEnabled(true);
	                mMap.setOnMyLocationButtonClickListener(this);
	            }
	        }
	    }

	
	
	  @Override
	    protected void onResume() {
	        super.onResume();
	        setUpMapIfNeeded();
	    	  
	    }
	  
	  
	  private void setUpMap() {
	      mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(CurrentLat, CurrentLng)));  
		  mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
	      m1 =mMap.addMarker(new MarkerOptions().position(new LatLng(CurrentLat, CurrentLng)).title("Me"));
	      m1.showInfoWindow();
	    }  
	  
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		CurrentLat =  arg0.getLatitude();
	    CurrentLng =  arg0.getLongitude();
	    

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMyLocationButtonClick() {
		// TODO Auto-generated method stub
		return false;
	}

	
} 
