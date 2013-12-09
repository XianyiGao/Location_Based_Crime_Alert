package com.example.location_based_crime_alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGesturePerformedListener {
  private GestureLibrary gestureLib;
  public static List<String> messages = new ArrayList<String>(); 
  public static int message_selection=0;
  public static String Phone_Number=" ";
  public static final int PICK_CONTACT=1;
  LocationManager locationManager=null;
  LocationListener locationListener=null;
  Button GPS_service, GPS_disable;
  static double lat=0, lon=0;
  TextView text, addres;
  public static List<String> Query_lat, Query_long;
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
	  final Context context;
		context=this;
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);
    gestureOverlayView.addOnGesturePerformedListener(MainActivity.this);
    text = (TextView) findViewById(R.id.textView1);
    addres = (TextView) findViewById(R.id.textView2);
    gestureLib = GestureLibraries.fromRawResource(MainActivity.this, R.raw.gestures);
    if (!gestureLib.load()) {
      finish();
    }
    messages.add("----SELECT----");
    messages.add("Help me! I'm in ADDRESS.");
    GPS_service = (Button) findViewById(R.id.button1);
    GPS_service.setOnClickListener(GPS_serviceClick);
    GPS_disable = (Button) findViewById(R.id.button2);
    GPS_disable.setOnClickListener(GPS_disableClick);
    locationListener = new LocationListener() {
    	
        public void onLocationChanged(Location location) {
          

        	
        	if (location != null)
        	{
        		String print="";

        		String stra=String.format("%7.3f", location.getLatitude());
        		String strb=String.format("%7.3f", location.getLongitude());
        		
        		print="Latitude: "+stra+"\nLongitude: "+strb;
        		//+location.getProvider()
        		lat=location.getLatitude();
        		lon=location.getLongitude();
        		text.setText(print);
        		LatLng myLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        		try {

                    Geocoder geo = new Geocoder(context, Locale.getDefault());
                   
                    List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.isEmpty()) {
                        addres.setText("Waiting for Location");
                    	
                    }
                    else {
                        if (addresses.size() > 0) {
                            addres.setText(addresses.get(0).getFeatureName() + "\n" + addresses.get(0).getLocality() +"\n" + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                         
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace(); 
                }
        	}
        }
    	

        public void onStatusChanged(String provider, int status, Bundle extras) {
  		// could do something here

  	  }
    	
        public void onProviderEnabled(String provider) {
        	// could do something here
        	
        }
    
        public void onProviderDisabled(String provider) {
        	// could do something here
        	
        }
      };
      locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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
    if (name.equals("horizontal swipe")) {
    	Intent intent = new Intent(MainActivity.this,
				messageSetting.class);
		startActivity(intent);
    }else if (name.equals("circle")){
    	Intent intent = new Intent(MainActivity.this,
				myMap.class);
		startActivity(intent);
    }else if (name.equals("c shape")){
    	Intent intent = new Intent(Intent.ACTION_DIAL);
    	startActivity(intent);
    	//Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		//startActivityForResult(intent, PICK_CONTACT);
		  
    }else if (name.equals("m shape")){
        Intent ii = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, ii, 0);
    	SmsManager sms = SmsManager.getDefault();
    	String message="";
    	if ((Phone_Number.length()>=10)&&(message_selection>0)){
    		
    		if (messages.get(message_selection).equals("Help me! I'm in ADDRESS.")){
    			
    			if (addres.getText().toString().equals("Waiting for Location")||(addres.getText().toString().equals("Address"))) {
    				message="Help me! I'm in latitude "+String.format("%7.3f", lat)+" and longitude "+String.format("%7.3f", lon);
    			}else message = "Help me! I'm in " + addres.getText().toString();
    			sms.sendTextMessage(Phone_Number, null, message, pi, null);
    		}else{
    	sms.sendTextMessage(Phone_Number, null, messages.get(message_selection), pi, null);
    		}
    	Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show();
    	}
    	else {
    		Toast.makeText(this, "Please set the emergency message!", Toast.LENGTH_SHORT)
                .show();
    	}
    }
  }
  @Override 
  protected void onDestroy() { 
	  locationManager.removeUpdates(locationListener);
   super.onDestroy(); 
  } 
  private OnClickListener GPS_serviceClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, LocListener.class);
			String[] latitude = new String[100];
			int lat_length = 0;
			lat_length = Query_lat.size();
			String[] longitude = new String[100];
			int long_length = 0;
			long_length = Query_long.size();
			intent.putExtra("Query_lat", latitude);
			intent.putExtra("lat_length", lat_length);
			intent.putExtra("Query_long", longitude);
			intent.putExtra("long_length", long_length);
			
			startService(intent);
		}
  };
  private OnClickListener GPS_disableClick = new OnClickListener() {
		public void onClick(View v) {
			 
		}
};
} 