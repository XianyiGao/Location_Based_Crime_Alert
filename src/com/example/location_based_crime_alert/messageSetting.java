package com.example.location_based_crime_alert;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class messageSetting extends Activity {
private Spinner spinner_handle, spinner_handle2;
//private String[] totalText = new String[100];

private Button add, delete;
private EditText new_message, phone_number;
//private int message_leng;
private ArrayAdapter<String> adapter, adapter2;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.message_setting);
        
        
        
        //message_leng=messages.size();

        
        spinner_handle=(Spinner) findViewById(R.id.spinner1);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MainActivity.messages);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_handle.setAdapter(adapter);
		//selection = 0;
		spinner_handle.setSelection(MainActivity.message_selection);
		add = (Button) findViewById(R.id.button1);
		add.setOnClickListener(addClick);
		
		delete = (Button) findViewById(R.id.button2);
		delete.setOnClickListener(deleteClick);
		
		new_message = (EditText) findViewById(R.id.editText1);
		new_message.setFocusable(false);
		new_message.setOnClickListener(touchMessage);
		
		
		spinner_handle2=(Spinner) findViewById(R.id.spinner2);
		adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MainActivity.messages);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_handle2.setAdapter(adapter2);
		
		phone_number = (EditText) findViewById(R.id.editText2);
		
		
    };
    
    private OnClickListener addClick = new OnClickListener() {
		public void onClick(View v) {
			String newText;
			newText = new_message.getText().toString();
			if ((newText!=null)&&(newText!="")&&(newText.length()!=0))
			{
				
				
				adapter.insert(newText, 1);
				//adapter.notifyDataSetChanged();

				//adapter2.insert(newText, 1);
				//adapter2.notifyDataSetChanged();

				//spinner_handle.setAdapter(adapter);
				spinner_handle.setSelection(1);
				//spinner_handle2.setAdapter(adapter2);
				new_message.setText("");
			}
			
		}
    };
    
    private OnClickListener deleteClick = new OnClickListener() {
  		public void onClick(View v) {
  			int select;
  			select=spinner_handle2.getSelectedItemPosition();
			if (select!=0)
			{
				
				
				adapter.remove(MainActivity.messages.get(select));
				//adapter.notifyDataSetChanged();

				//adapter2.insert(newText, 1);
				//adapter2.notifyDataSetChanged();

				//spinner_handle.setAdapter(adapter);
				spinner_handle.setSelection(0);
				//spinner_handle2.setAdapter(adapter2);
				spinner_handle2.setSelection(0);
			}
  			
  		}
      };
      
    private OnClickListener touchMessage = new OnClickListener() {
  		public void onClick(View v) {
  			
  			new_message.setFocusableInTouchMode(true);
  		}
      };
	
 	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void onBackPressed() {
    	if ((spinner_handle.getSelectedItemPosition()!=0)&&(phone_number.getText().toString().length()>=10))
    	{
    	  MainActivity.message_selection=spinner_handle.getSelectedItemPosition();
    	  MainActivity.Phone_Number=phone_number.getText().toString();
    	  super.onBackPressed();
    	}
    	else {
    		Toast.makeText(this,"Please select a message and input a valid phone number.", Toast.LENGTH_LONG).show();
    	}
    	
       
    }

}