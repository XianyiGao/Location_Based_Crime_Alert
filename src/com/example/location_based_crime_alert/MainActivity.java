package com.example.location_based_crime_alert;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGesturePerformedListener {
  private GestureLibrary gestureLib;
  public static List<String> messages = new ArrayList<String>(); 
  public static int message_selection=0;
  public static String Phone_Number=" ";
  public static final int PICK_CONTACT=1;
  
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
    messages.add("Help me! I'm in ADDRESS. (ADDRESS will be replaced by the current location)");
    
  }
  @Override
	 public void onActivityResult(int reqCode, int resultCode, Intent data) {
	 super.onActivityResult(reqCode, resultCode, data);

	 switch (reqCode) {
	 case (PICK_CONTACT):
	   if (resultCode == Activity.RESULT_OK) {

	     Uri contactData = data.getData();
	     Cursor c =  managedQuery(contactData, null, null, null, null);
	     if (c.moveToFirst()) {


	         String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

	         String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

	           if (hasPhone.equalsIgnoreCase("1")) {
	          Cursor phones = getContentResolver().query( 
	                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
	                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
	                       null, null);
	             phones.moveToFirst();
	              String cNumber = phones.getString(phones.getColumnIndex("data1"));
	              Toast.makeText(this, "Number " + cNumber,Toast.LENGTH_SHORT).show();
	           }
	         String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


	     }
	   }
	   break;
	 }
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
    	if ((Phone_Number.length()>=10)&&(message_selection>0)){
    	sms.sendTextMessage(Phone_Number, null, messages.get(message_selection), pi, null);
    	Toast.makeText(this, "Done!", Toast.LENGTH_SHORT)
        .show();
    	}
    	else {
    		Toast.makeText(this, "Please set the emergency message!", Toast.LENGTH_SHORT)
                .show();
    	}
    }
  }
} 