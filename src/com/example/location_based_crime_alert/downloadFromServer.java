package com.example.location_based_crime_alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;



public class downloadFromServer {
	
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_CRIME = "crime";
	private static final String TAG_CRIMES = "crimes";
	private static final String TAG_PID = "pid";
	private static final String TAG_TITLE = "title";
	private static final String TAG_GEO_LAT = "geo_lat";
	private static final String TAG_GEO_LONG = "geo_long";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_OFFICIAL = "official";
	private static final int official = 0;
	
    private JSONParser jParser = new JSONParser();
    private JSONArray crimes = null;
	private ProgressDialog pDialog;
	private Context programContext;
    public static ArrayList<HashMap<String, String>> crimesList = new ArrayList<HashMap<String, String>>();

    
    public static boolean wait = true;

	
    private static String url_all_crimes = "http://www.healthmonitoringanalytics.com/android_connect/get_all_crimes.php";

	
	
	public downloadFromServer(Context con){
		
		this.programContext = con;
		new LoadAllProducts().execute();		
		
	}
	
    class LoadAllProducts extends AsyncTask<String, String, String> {
    	 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(programContext);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_crimes, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Crimes: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    crimes = json.getJSONArray(TAG_CRIMES);
                    if(crimes != null){
                    	Log.d("Here","Not null");
                    }
                    // looping through All Products
                    for (int i = 0; i < crimes.length(); i++) {
                        JSONObject c = crimes.getJSONObject(i);
                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String title = c.getString(TAG_TITLE);
                        String description = c.getString(TAG_DESCRIPTION);
                        String geo_lat = c.getString(TAG_GEO_LAT);
                        String geo_long = c.getString(TAG_GEO_LONG);
                        String official = c.getString(TAG_OFFICIAL);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_TITLE, title);
                        map.put(TAG_DESCRIPTION, description);
                        map.put(TAG_GEO_LAT, geo_lat);
                        map.put(TAG_GEO_LONG, geo_long);
                        map.put(TAG_OFFICIAL, official);
 
                        // adding HashList to ArrayList
                        crimesList.add(map);
                    }
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
            // dismiss the dialog after getting all products
        	
            pDialog.dismiss();
            // updating UI from Background Thread
            
             
 
        }
 
    }
}


