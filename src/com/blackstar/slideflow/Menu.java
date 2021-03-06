package com.blackstar.slideflow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.blackstar.slideflow.R;
import com.flurry.android.FlurryAgent;

public class Menu extends Activity{
	int level, highestLevel;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		final Button challenge = (Button)  findViewById(R.id.buttonChallenge);
		final Button timed = (Button)  findViewById(R.id.buttonTimed);
		final Button multiplayer = (Button)  findViewById(R.id.buttonMultiplayer);
		
		// configure Flurry
		FlurryAgent.setLogEnabled(false);
		// init Flurry
		FlurryAgent.init(this, "FHXP5J7H49G6NHF4Q72M");
		
		challenge.setOnClickListener (new View.OnClickListener(){
        	@Override
			public void onClick (View v){
        		setContentView(R.layout.levels);
        		final DisplayLevels canvas = (DisplayLevels)  findViewById(R.id.displayLevels1);
        		final Button start = (Button)  findViewById(R.id.start);		
        		level=1; highestLevel=1;
        		try {
        			FileInputStream fi = openFileInput("user_file");
        			Scanner in = new Scanner(fi);
        			level=in.nextInt();
        			highestLevel=in.nextInt();
        			in.close();
        		} catch (FileNotFoundException e) {
        			e.printStackTrace();
        			try{
        				OutputStreamWriter out = new OutputStreamWriter(openFileOutput("user_file",0)); 
        				out.write("1 1");
        				out.close(); 
        			} catch (IOException z) {
        	    		z.printStackTrace(); 
        	    	}
        		}
        		
        		canvas.setLevel(highestLevel);
        		
        		canvas.setOnTouchListener(new View.OnTouchListener(){
        			@Override
        			public boolean onTouch(View arg0, MotionEvent event) {
        				// TODO Auto-generated method stub
        				if(event.getAction() == MotionEvent.ACTION_DOWN){
        					int out = (canvas.getSelectedLevel(event.getX(),event.getY()));
        					if(out!=-1 && out<=highestLevel){
        						Intent i = new Intent(getApplicationContext(), SlideActivity.class);
        		        		i.putExtra("level",out); i.putExtra("highestLevel",highestLevel);
        		        		startActivity(i);
        					}
        				}
        				return false;
        			}
        		});
        		start.setOnClickListener (new View.OnClickListener(){
                	@Override
        			public void onClick (View v){
                		Intent i = new Intent(getApplicationContext(), SlideActivity.class);
                		i.putExtra("level",level); i.putExtra("highestLevel",highestLevel);
                		startActivity(i);
                	}
        		});
        		FlurryAgent.logEvent("Challenge");
        	}
		});
		
		timed.setOnClickListener (new View.OnClickListener(){
        	@Override
			public void onClick (View v){
        		level=1; highestLevel=1;
        		Intent i = new Intent(getApplicationContext(), SlideActivity.class);
        		i.putExtra("level",level); i.putExtra("highestLevel",highestLevel);
        		i.putExtra("time",200);
        		startActivity(i);
        		FlurryAgent.logEvent("Timed");
        	}
		});
		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "FHXP5J7H49G6NHF4Q72M");
	}
	 
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
}
