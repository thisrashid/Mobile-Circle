package com.ecafechat.mobilecircle;

//import com.ecafe.testproject.R;

import java.util.Vector;

import com.ecafechat.mobilecircle.model.MobileCircleDatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MobileCircleActivity extends Activity {
	MobileCircleDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MobileCircleDatabase(this);
        setContentView(R.layout.main);
        
        final EditText number = (EditText) findViewById(R.id.mobile_number);
        final TextView location = (TextView) findViewById(R.id.location);
        
        Button.OnClickListener listener = new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Vector<String> loc = db.getLocation(number.getText().toString().substring(0, 4));
				System.out.println("Output = " + loc);
				String output = "Number:" + number.getText().toString() + "\n";
				
				if(loc != null && loc.size() == 3) {
					output += "Circle: " + loc.get(0) + "\n";
					output += "Operator: " + loc.get(1) + "\n";
					output += "Company: " + loc.get(2) + "\n";
				} else {
					output += "No result found";
				}
				
				//location.setText(number.getText().toString().substring(0, 4) + " - " + loc.toString());
				location.setText(output);
				
			}
		};
		((Button) findViewById(R.id.button_ok)).setOnClickListener(listener);
    }
}