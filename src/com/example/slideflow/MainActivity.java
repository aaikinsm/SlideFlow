package com.example.slideflow;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{
	int level =1;
	boolean gameOver;
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
		Context cntx = this;
		mHandler = new Handler();
		gameOver = false;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)  level = extras.getInt("level");
		if (level>100) level=1;
		
		
		levelText.setText("Level "+level);
		canvas.loadData(level);
		canvas.setOnTouchListener(new OnSwipeTouchListener(cntx) {
		    public void onSwipeTop() {
		        canvas.setDirection("up");
		    }
		    public void onSwipeRight() {
		    	canvas.setDirection("right");
		    }
		    public void onSwipeLeft() {
		    	canvas.setDirection("left");
		    }
		    public void onSwipeBottom() {
		    	canvas.setDirection("down");
		    }

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(!gameOver) canvas.selectBlock(event.getX(), event.getY());
				}
				//Toast.makeText(MainActivity.this, event.getX(0)+":"+event.getY(0), Toast.LENGTH_SHORT).show();
			    return gestureDetector.onTouchEvent(event);
			}
		});
		
		go.setOnClickListener (new View.OnClickListener(){
        	@Override
			public void onClick (View v){
        		Intent i = new Intent(getApplicationContext(), MainActivity.class);
        		i.putExtra("level",level+1);
        		startActivity(i);
        		finish();
        	}
		});
		
		gameClock = new Runnable() {   
        	@Override
    		public void run() {
        		moves.setText("Moves: "+canvas.count+"/"+canvas.maxMoves);
        		if(canvas.gameOver){ 
        			go.setVisibility(View.VISIBLE);
        			gameOver=true;
        		}else{      			
        			mHandler.postDelayed(this, 500);       		
        		}
        	}
        };
        mHandler.postDelayed(gameClock, 500);
	}
}
