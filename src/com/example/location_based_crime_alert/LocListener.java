package com.example.location_based_crime_alert;
import android.app.NotificationManager;
import android.app.Service;

import android.content.Context;

import android.content.Intent;

import android.location.Location;

import android.location.LocationListener;

import android.location.LocationManager;

import android.os.Bundle;

import android.os.IBinder;


import android.widget.Toast;




public class LocListener extends Service implements LocationListener {

	

	public static double CurLat=0.0,CurLng=0.0;

	public LocationManager lm;

	public LocationListener l;

	public static double QuerryLat=0.0,QuerryLng=0.0;
	String[] Query_lat=new String[100];
	  int lat_length=0;
	  String[] Query_long=new String[100];
	  int long_length=0;
	  
	  NotificationManager mBuilder;
	@Override

	public void onCreate(){

		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();  
		
		mBuilder =

	            new NotificationCompat.Builder(this)

	            .setSmallIcon(R.drawable.common_signin_btn_icon_focus_light)

	            .setContentTitle("My notification")

	            .setContentText("Hello World!");



        //---use the LocationManager class to obtain locations data---

        




		

	}

	@Override

    public int onStartCommand(Intent intent, int flags, int startId) {

      //TODO do something useful
		Bundle extras = intent.getExtras();
	    Query_lat = extras.getStringArray("Query_lat");
        lat_length = extras.getInt("lat_length");
        Query_long = extras.getStringArray("Query_long");
        long_length = extras.getInt("long_length");
        lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
     //---request for location updates using GPS---

        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                6000,
                10,
                this);     


      return START_STICKY;

    }

	@Override

	public IBinder onBind(Intent arg0) {

		// TODO Auto-generated method stub

		return null;

	}




	@Override

	public void onLocationChanged(Location arg0) {

		// TODO Auto-generated method stub

		CurLat= arg0.getLatitude();

		CurLng= arg0.getLongitude();

		float [] distance= new float[2];

		boolean flag=true;
		
		for (int i=0; i<lat_length; i++ ){
		Location.distanceBetween( CurLat, CurLng,

		    QuerryLat, QuerryLng, distance);


		if( distance[0] <= 100.0  ){

			

		} else {

		    Toast.makeText(getBaseContext(), "Inside", Toast.LENGTH_LONG).show();

		}

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




}