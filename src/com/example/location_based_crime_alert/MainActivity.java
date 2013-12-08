package com.example.location_based_crime_alert;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGesturePerformedListener {
  private GestureLibrary gestureLib;
  public static List<String> messages = new ArrayList<String>(); 
  public static int message_selection=0;
  public static String Phone_Number=null;
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);
    gestureOverlayView.addOnGesturePerformedListener(MainActivity.this);
    gestureLib = GestureLibraries.fromRawResource(MainActivity.this, R.raw.gestures);
    if (!gestureLib.load()) {
      finish();
    }
    messages.add("----SELECT----");
    messages.add("Help me! I'm in ADDRESS.\n(ADDRESS will be replaced by the current location)");
    
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
    }
  }
} 