package com.example.location_based_crime_alert;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class uploadToServer {
	
	private static final String TAG_SUCCESS = "success";
	private static final int official = 0;
	
    private static String url_create_crime = "http://www.healthmonitoringanalytics.com/android_connect/create_crime.php";
    
	
	private JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	private Context programContext;
	
	@SuppressWarnings("unchecked")
	public uploadToServer(String title, double geo_lat, double geo_long, String description,Context programContext){
		this.programContext = programContext;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
        String A,B,C,D,E;
        A = title;
        B = Double.toString(geo_lat);
        C = Double.toString(geo_long);
        D = description;
        E = Integer.toString(official);
        
        params.add(new BasicNameValuePair("title",A));
        params.add(new BasicNameValuePair("geo_lat",B));
        params.add(new BasicNameValuePair("geo_long",C));
        params.add(new BasicNameValuePair("description",D));
        params.add(new BasicNameValuePair("official",E));
      
		new CreateNewCrime().execute(params);
		

	}
	
    /**
     * Background Async Task to Create new Crime
     * */
    class CreateNewCrime extends AsyncTask<List<NameValuePair>, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(programContext);
            pDialog.setMessage("Uploading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating crime
         * */
        protected String doInBackground(List<NameValuePair>... args) {

            // Building Parameters
            
        	// AsyncTask inherently passes an array of objects.
 
            // getting JSON Object
            // Note that create crime url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_crime,
                    "POST", args[0]);
 
            // check log cat for response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
}

	
    



