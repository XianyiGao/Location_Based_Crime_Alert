package com.example.location_based_crime_alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class google_Map extends Activity {

	
	ImageView _imgView;
	static Bitmap Bmap=null;
	ProgressDialog _busyDialog = null;
	Button download=null, checkin, clear,update;
	LocationManager locationManager=null;
	LocationListener locationListener=null;
	TextView text=null, addres;
	GoogleMap map;
	RadioGroup GPS_NET;
	ListView list;
	Marker marker_handle=null;
	private ArrayAdapter<String> listAdapter;
	static List<String> check_in_list = new ArrayList<String>();
	static double lat=0, lon=0;
	private String fileName="Assignment2_CHECKIN.txt";
	private File path = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS
					+ "/Assignment2/");
	private File file =  new File(path, fileName);
	static String passedTime=null;
	static int counter=0;

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
            _imgView = (ImageView) myContentsView.findViewById(R.id.imgDownload1);
            Bitmap Bmap2=null;
            Bmap2=getResizedBitmap(Bmap,50);
            _imgView.setImageBitmap(Bmap2);
   TextView tvtime = ((TextView)myContentsView.findViewById(R.id.timePass));
            tvtime.setText(passedTime);
   
            
            return myContentsView;
  }
  public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
      int width = image.getWidth();
      int height = image.getHeight();
      
      float bitmapRatio; 
      bitmapRatio = (float) 1.0 * width / height;
      
      if (bitmapRatio > 0) {
          width = maxSize;
          height = (int) (width / bitmapRatio);
          
      } else {
          height = maxSize;
          width = (int) (height * bitmapRatio);
          
      }
      
      return Bitmap.createScaledBitmap(image, width, height, true);
}
	
	
  @Override
  public View getInfoWindow(Marker marker) {
   // TODO Auto-generated method stub
   return null;
  }
	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.map_view);
		final Context context;
		context=this;
		check_in_list.clear();
		
		try{
			path.mkdirs();
			if (file.exists()){
			//Toast.makeText(getApplicationContext(), "exist", Toast.LENGTH_LONG).show();
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    
		        String line = br.readLine();

		        while ((line != null)&&(line != "\n")) {
		            check_in_list.add(line);
		            line = br.readLine();
		        
		    } 
		        
		        br.close();}
		    
	           
		} catch(Exception e){}
		
		list = (ListView) findViewById(R.id.listView1);
		listAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, check_in_list);
		list.setAdapter(listAdapter);

		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setOnMarkerClickListener(
    		    new OnMarkerClickListener() {
    		        boolean doNotMoveCameraToCenterMarker = true;
    		        public boolean onMarkerClick(Marker marker) {
    		            //Do whatever you need to do here ....
    		        	String str=null;
    		        	if (marker.getTitle().compareTo("mickey")==0)
    		        		str="http://www.winlab.rutgers.edu/~shubhamj/mickey.png";
    		        	if (marker.getTitle().compareTo("donald")==0)
    		        		str="http://winlab.rutgers.edu/~sugangli/donald.jpg";
    		        	if (marker.getTitle().compareTo("goofy")==0)
    		        		str="http://winlab.rutgers.edu/~sugangli/goofy.png";
    		        	if (marker.getTitle().compareTo("garfield")==0)
    		        		str="http://winlab.rutgers.edu/~sugangli/garfield.jpg";
    		        	marker_handle=marker;
    		        	
    		        	downloadImage(str);
    		        
    		            return doNotMoveCameraToCenterMarker;
    		        }
    		    });
		map.setMyLocationEnabled(true);

   
        
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
      
        
        map.setTrafficEnabled(true);
        
       
        
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());

		GPS_NET = (RadioGroup) findViewById(R.id.radioGroup1);
		  GPS_NET.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			   public void onCheckedChanged(RadioGroup arg0, int arg1) {
			    
			    int id=arg0.getCheckedRadioButtonId();
			    
			    RadioButton selectedRedioButton = (RadioButton) findViewById(id);
			    
			    if (selectedRedioButton.getText().toString().equalsIgnoreCase("gps location")){
			    	try
			    	{
			    		locationManager.removeUpdates(locationListener);
			    		

			    	}catch (Exception ex){}
			    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			    }
			    if (selectedRedioButton.getText().toString().equalsIgnoreCase("network location")){
			    try
			    	{
			    		locationManager.removeUpdates(locationListener);

			    	}catch (Exception ex){}
			    try {
			    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			    }catch (Exception ex)
			    {
			    	Toast.makeText(getApplicationContext(), "Network location provider is not available in emulater", Toast.LENGTH_LONG).show();
			    }
			    }
			   }
			  });
	
		checkin = (Button) findViewById(R.id.button1);
		clear = (Button) findViewById(R.id.button2);
		checkin.setOnClickListener(clickCheckin);
		clear.setOnClickListener(clickClear);
		update = (Button) findViewById(R.id.button3);
		update.setOnClickListener(clickUpdate);
		text = (TextView) findViewById(R.id.textView1);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        addres = (TextView) findViewById(R.id.textView2);
        
        
        locationListener = new LocationListener() {
        
            public void onLocationChanged(Location location) {
              
      
            	
            	if (location != null)
            	{
            		String print="";
 
            		String stra=String.format("%7.3f", location.getLatitude());
            		String strb=String.format("%7.3f", location.getLongitude());
            		String strc=String.format("%7.2f", location.getAccuracy());
            		print="Latitude: "+stra+"\nLongitude: "+strb+"\nAccuracy(meters): "+strc;
            		//+location.getProvider()
            		lat=location.getLatitude();
            		lon=location.getLongitude();
            		text.setText(print);
            		map.clear();
            		LatLng myLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            		
            		Location mickey=new Location("A");
            		Location donald=new Location("B");
            		Location goofy=new Location("C");
            		Location garfield=new Location("D");
            		mickey.setLatitude(40.517828);
            		mickey.setLongitude(-74.465292);
            		donald.setLatitude(40.513179);
            		donald.setLongitude(-74.465292);
            		goofy.setLatitude(40.495526);
            		goofy.setLongitude(-74.467141);
            		garfield.setLatitude(40.497125);
            		garfield.setLongitude(-74.417059);
            		
            		map.addMarker(new MarkerOptions().position(new LatLng(40.517828, -74.465292)).title("mickey").snippet("Distance: "+String.valueOf(location.distanceTo(mickey))));
            		map.addMarker(new MarkerOptions().position(new LatLng(40.513179, -74.433839)).title("donald").snippet("Distance: "+String.valueOf(location.distanceTo(donald))));
            		map.addMarker(new MarkerOptions().position(new LatLng(40.495526, -74.467141)).title("goofy").snippet("Distance: "+String.valueOf(location.distanceTo(goofy))));
            		map.addMarker(new MarkerOptions().position(new LatLng(40.497125, -74.417059)).title("garfield").snippet("Distance: "+String.valueOf(location.distanceTo(garfield))));
            	
            	map.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
            	
            	

            		
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

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
      
        //Declare the timer
        Timer t = new Timer();
        
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
            	String str11=null;
            	counter++;
            	if (counter>=60) counter=counter % 60;
            	if (counter<10) str11="Time passed: 00:0"+String.valueOf(counter);
            	else str11="Time passed: 00:"+String.valueOf(counter);
            	passedTime=str11;
                //Called each time when 1000 milliseconds (1 second) (the period parameter)
            }

        },
        //Set how long before to start calling the TimerTask (in milliseconds)
        0,
        //Set the amount of time between each execution (in milliseconds)
        1000);
    }

    private OnClickListener clickUpdate = new OnClickListener() {
		public void onClick(View v) {
			counter=0;
			
			
		}
    };

    private OnClickListener clickCheckin = new OnClickListener() {
		public void onClick(View v) {
			String str;
			str=String.format("Latitude: %7.2f Longitude: %7.2f",lat, lon);
			
			//String str="2";
			listAdapter.insert(str,0);
			
			
		}
    };
    private OnClickListener clickClear = new OnClickListener() {
		public void onClick(View v) {
			//check_in_list.clear();

			listAdapter.clear();
			
		}
    };

	public void downloadImage(String Http) {
	    //_imgView.setImageBitmap(null);
		showBusyDialog();
		
		new DownloadImageTask().execute(Http);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
		@Override
		protected Bitmap doInBackground(String...url) {
			return loadImageFromNetwork(url[0]);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			
			Bmap=result;
			if (result==null) 
            	Toast.makeText(getApplicationContext(), "Wrong!!", Toast.LENGTH_LONG).show();
			dismissBusyDialog();
			marker_handle.showInfoWindow();
			
			
			
		}
	}

	private Bitmap loadImageFromNetwork(String url) {
		Bitmap bitmap = null;

		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(url)
					.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public void showBusyDialog() {
		_busyDialog = ProgressDialog.show(this, "", "Downloading Image...",
				true);
	}

	public void dismissBusyDialog() {
		if (_busyDialog != null) {
			_busyDialog.dismiss();
		}
	}
	public void onDestroy(){
		if (check_in_list.size()>0){
		
			try{
				path.mkdirs();
				file.setWritable(true);
				
				FileWriter output = new FileWriter(file);
				for (int i=0; i<check_in_list.size();i++){
					output.write(check_in_list.get(i)+"\n");
				}
				output.close();
				
			} catch(Exception e){
				Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
			}
		}else if (file.exists()) {
			file.delete();
		}
		
		
		super.onDestroy();
	}
}
