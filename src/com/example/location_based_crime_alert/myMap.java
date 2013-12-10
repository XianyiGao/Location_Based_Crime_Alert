package com.example.location_based_crime_alert;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.PendingIntent;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class myMap extends FragmentActivity implements LocationListener,OnMyLocationButtonClickListener,OnGesturePerformedListener{

	private GoogleMap mMap;
	  private Marker m1,m2,m3,m4,m5;
	  LatLng p;
	  DecimalFormat df;
	  Double CurrentLat=0.0,CurrentLng=0.0;
	  String[] Query_lat=new String[100];
	  int lat_length=0;
	  String[] Query_long=new String[100];
	  int long_length=0;
	  String addres="Waiting for Location";
	  GestureLibrary gestureLib;
	  uploadToServer u;
	  downloadFromServer d1;

		class MyInfoWindowAdapter implements InfoWindowAdapter{

	        private final View myContentsView;
	  
	  MyInfoWindowAdapter(){
	   myContentsView = getLayoutInflater().inflate(R.layout.infowindow, null);
	   
	  }
	  
	  @Override
	  public View getInfoContents(Marker marker) {
	   
	   TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
	            tvTitle.setText(marker.getTitle());
	   TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
	            tvSnippet.setText(marker.getSnippet());
	           
	            return myContentsView;
	  }
		
	  @Override
	  public View getInfoWindow(Marker marker) {
	   // TODO Auto-generated method stub
	   return null;
	  }
		}
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
	    gestureOverlayView.addOnGesturePerformedListener(myMap.this);
	    gestureLib = GestureLibraries.fromRawResource(getApplicationContext(), R.raw.gestures);
	    if (!gestureLib.load()) {
	        finish();
	      }
	    CurrentLat=MainActivity.current_lat;
	    CurrentLng=MainActivity.current_long;
	    Bundle extras = getIntent().getExtras();
	    Query_lat = extras.getStringArray("Query_lat");
        lat_length = extras.getInt("lat_length");
        Query_long = extras.getStringArray("Query_long");
        long_length = extras.getInt("long_length");
         df = new DecimalFormat("###.##");
	    setUpMapIfNeeded(); 
	    updateMap();

	   
	    
	    
	  }
 
	  private void updateMap(){
		  
		  for (int i=0; i<downloadFromServer.crimesList.size(); i++){
		    	p=new LatLng(Double.parseDouble(downloadFromServer.crimesList.get(i).get("geo_lat")),Double.parseDouble(downloadFromServer.crimesList.get(i).get("geo_long")));
		     m2 =mMap.addMarker(new MarkerOptions().position(p).title(downloadFromServer.crimesList.get(i).get("title")).snippet("Occurrence Time: "+downloadFromServer.crimesList.get(i).get("updated_at")+"\n"+downloadFromServer.crimesList.get(i).get("description")));
		     	mMap.addCircle(new CircleOptions().center(p).radius(100.0));
		    }
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
		  mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
	      mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(CurrentLat, CurrentLng)));  
		  mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
	      //m1 =mMap.addMarker(new MarkerOptions().position(new LatLng(CurrentLat, CurrentLng)).title("Me").snippet("Current Position"));
	      //m1.showInfoWindow();
	    }  
	  
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		CurrentLat=arg0.getLatitude();
		CurrentLng=arg0.getLongitude();
	    try {

            Geocoder geo = new Geocoder(myMap.this, Locale.getDefault());
           
            List<Address> addresses = geo.getFromLocation(CurrentLat, CurrentLng, 1);
            if (addresses.isEmpty()) {
                addres="Waiting for Location";
            	
            }
            else {
                if (addresses.size() > 0) {
                    addres=addresses.get(0).getFeatureName() + "\n" + addresses.get(0).getLocality() +"\n" + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                 
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); 
        }
	    

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
	@Override
	  public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
	    ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
	    boolean flag=true;
	    String name="Unknown";
	    
	    for (Prediction prediction : predictions) {
	      if ((prediction.score > 1.0)&&(flag)) {
	        //Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
	        //    .show();
	        name=prediction.name;
	        flag=false;
	      }
	    }
	   
	    if (name.equals("c shape")){
	    	
	    	Intent intent = new Intent(Intent.ACTION_DIAL);
	    	startActivity(intent);
	    	//Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			//startActivityForResult(intent, PICK_CONTACT);
			  
	    }else if (name.equals("m shape")){
	        Intent ii = new Intent(myMap.this, myMap.class);
	        PendingIntent pi = PendingIntent.getBroadcast(myMap.this, 0, ii, 0);
	    	SmsManager sms = SmsManager.getDefault();
	    	String message="";
	    	if ((MainActivity.Phone_Number.length()>=10)&&(MainActivity.message_selection>0)){
	    		
	    		if (MainActivity.messages.get(MainActivity.message_selection).equals("Help me! I'm in ADDRESS.")){
	    			
	    			if (addres.equals("Waiting for Location")) {
	    				message="Help me! I'm in latitude "+String.format("%7.3f", mMap.getMyLocation().getLatitude())+" and longitude "+String.format("%7.3f", mMap.getMyLocation().getLongitude());
	    			}else message = "Help me! I'm in " + addres;
	    			sms.sendTextMessage(MainActivity.Phone_Number, null, message, pi, null);
	    		}else{
	    	sms.sendTextMessage(MainActivity.Phone_Number, null, MainActivity.messages.get(MainActivity.message_selection), pi, null);
	    		}
	    	Toast.makeText(this, message, Toast.LENGTH_SHORT)
	        .show();
	    	}
	    	else {
	    		Toast.makeText(this, "Please set the emergency message!", Toast.LENGTH_SHORT)
	                .show();
	    	}
	    }else if (name.equals("s shape")){
	    	u=new uploadToServer("Crime Reported by somone", mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude(), "User reported crime location", this);
	    	d1=new downloadFromServer(this);
	    	
	    	mMap.clear();
	    	 setUpMapIfNeeded(); 
	 	    updateMap();
	    }
	  }
	
}
