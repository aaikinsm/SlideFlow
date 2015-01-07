package com.blackstar.slideflow;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackstar.slideflow.R;
import com.flurry.android.FlurryAgent;

public class SlideActivity extends Activity{
	int level =1, highestLevel=1, count;
	boolean gameOver, isTimed;
	Handler mHandler;
	Runnable gameClock;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide);
		final SlideView canvas = (SlideView)  findViewById(R.id.slideView1);
		final TextView levelText = (TextView)  findViewById(R.id.textViewLevel);
		final TextView moves = (TextView)  findViewById(R.id.textViewMoves);
		final Button go = (Button)  findViewById(R.id.buttonGo);
		final Button menu = (Button)  findViewById(R.id.buttonMenu);
		final Button retry = (Button)  findViewById(R.id.buttonRetry);
		final ImageView swipeImage = (ImageView) findViewById(R.id.imageSwipe);
		Context cntx = this;
		mHandler = new Handler();
		gameOver = false;
		isTimed=false;
		
		//read file
		Bundle extras = getIntent().getExtras();
		if (extras != null){  
			level = extras.getInt("level");
			highestLevel = extras.getInt("highestLevel");
			count = extras.getInt("time",-1);
			if (count!=-1){
				isTimed=true;
				level = (int) (level+(Math.random() * 5));
			}
		}
		else{
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
		}
		
		if(level==1) swipeImage.setVisibility(View.VISIBLE);
		
		//Start Time Data for Flurry
		final Map<String, String> flurryParam = new HashMap<String, String>();
	    flurryParam.put("Level", level+""); 
		FlurryAgent.logEvent("Level_Time", flurryParam, true);
		
		if (level>96) level=1;
		
				
		levelText.setText("Level "+level);
		canvas.loadData(level);
		canvas.setOnTouchListener(new OnSwipeTouchListener(cntx) {
		    @Override
			public void onSwipeTop() {
		        canvas.setDirection("up");
		    }
		    @Override
			public void onSwipeRight() {
		    	canvas.setDirection("right");
		    }
		    @Override
			public void onSwipeLeft() {
		    	canvas.setDirection("left");
		    }
		    @Override
			public void onSwipeBottom() {
		    	canvas.setDirection("down");
		    }

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(!gameOver) canvas.selectBlock(event.getX(), event.getY());
				}
				//Toast.makeText(SlideActivity.this, event.getX(0)+":"+event.getY(0), Toast.LENGTH_SHORT).show();
			    return gestureDetector.onTouchEvent(event);
			}
		});
		
		go.setOnClickListener (new View.OnClickListener(){
        	@Override
			public void onClick (View v){
        		Intent i = new Intent(getApplicationContext(), SlideActivity.class);
        		i.putExtra("level",level+1); i.putExtra("highestLevel",highestLevel);
        		if(isTimed) i.putExtra("time",count);
        		startActivity(i);
        		finish();
        	}
		});
		
		retry.setOnClickListener (new View.OnClickListener(){
        	@Override
			public void onClick (View v){
        		Intent i = new Intent(getApplicationContext(), SlideActivity.class);
        		i.putExtra("level",level); i.putExtra("highestLevel",highestLevel);
        		if(isTimed) i.putExtra("time",count);
        		startActivity(i);
        		finish();
        	}
		});
		
		menu.setOnClickListener (new View.OnClickListener(){
        	@Override
			public void onClick (View v){
        		//Intent i = new Intent(getApplicationContext(), Menu.class);
        		//startActivity(i);
        		finish();
        	}
		});
		
		gameClock = new Runnable() {   
        	@Override
    		public void run() {
        		int maxM = (canvas.maxMoves+((level-1)/24*5)-((level-1)/4));
        		moves.setText("Moves: "+canvas.count+"/"+maxM);
        		if (isTimed && count<0){
        			gameOver = true;
        			moves.setText("SCORE:"+level/2*10);  
        			levelText.setText("Time is up");
        		}
        		else if(canvas.gameOver){ 
        			moves.setText("LEVEL COMPLETE");
        			if(!isTimed){
	        			try{
	    					OutputStreamWriter out = new OutputStreamWriter(openFileOutput("user_file",0)); 
	    					String data;
	    					if (level+1 > highestLevel) data = (level+1)+" "+(level+1);
	    					else data = (level+1)+" "+highestLevel;
	    					out.write(data);
	    					out.close(); 
	    				} catch (IOException z) {
	    		    		z.printStackTrace(); 
	    		    	}
	        			
	        			//user data to report to flurry analytics
	        	        final Map<String, String> userParams = new HashMap<String, String>();
	        	        userParams.put("Level", level+""); 
	        	        userParams.put("Moves", canvas.count+""); 
	        	        userParams.put("Max", maxM+"");
	        			FlurryAgent.logEvent("Level_Data", userParams);
        			}
        			go.setVisibility(View.VISIBLE);
        			if(level==1) swipeImage.setVisibility(View.GONE);
        			gameOver=true;
        		}
        		else if(canvas.count == maxM){
        			gameOver = true;
        			moves.setText("LEVEL FAILED (Too Many moves)");
        		}
        		else{  
        			if(isTimed){
        				levelText.setText("Time: "+count);
        				count--;
        			}
        			mHandler.postDelayed(this, 500);          			
        		}       		
        	}
        };
        mHandler.postDelayed(gameClock, 500);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();	
		FlurryAgent.endTimedEvent("Level_Time");
	}
}
