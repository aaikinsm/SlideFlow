package com.blackstar.slideflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.blackstar.slideflow.R;

public class DisplayLevels extends View{
	  Paint circles = new Paint();
	  Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.clear);
	  Bitmap localBitmap1;
	  boolean initial = true;
	  int level = 0, lvl, margin=5, max=24, imgWidth;	  
	  Typeface myTypeface;
	  Paint pBg = new Paint();
	  Paint rec1 = new Paint();
	  Paint rec2 = new Paint();
	  Paint rec3 = new Paint();
	  Paint rec4 = new Paint();
	  int rem = 0;
	  int rows = 0;
	  Paint txtp = new Paint();
	  int x;
	  int y;
	  double txtpY =0;
	  
	  public DisplayLevels(Context paramContext)
	  {
	    super(paramContext);
	  }
	  
	  public DisplayLevels(Context paramContext, AttributeSet paramAttributeSet)
	  {
	    super(paramContext, paramAttributeSet);
	  }
	  
	  public DisplayLevels(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	  {
	    super(paramContext, paramAttributeSet, paramInt);
	  }
	  
	  public void setLevel(int paramInt)
	  {
	    this.lvl = paramInt;
	  }
	  
	  public void setLevel(int paramInt, Typeface paramTypeface)
	  {
	    this.lvl = paramInt;
	    this.myTypeface = paramTypeface;
	  }
	  
	  @Override
	  protected void onDraw(Canvas paramCanvas){
		    super.onDraw(paramCanvas);		    
			if(initial){
				this.x = (paramCanvas.getWidth()-paramCanvas.getWidth()/12);
			    this.y = (paramCanvas.getHeight()-paramCanvas.getWidth()/12);			    
			    this.txtp.setTextSize(this.x / 10);
			    this.txtp.setTextAlign(Paint.Align.CENTER);			    
				lvl--; level--; //remove to start count at 0
			    img = Bitmap.createScaledBitmap(this.img, this.x / 6, this.x / 6, false);
			    txtpY= img.getHeight()/3;			    
			    imgWidth = img.getWidth();
			    initial=false;
			}
			
			this.rec1.setColor(Color.RED);
			this.rec2.setColor(Color.BLUE);
			this.rec3.setColor(Color.GREEN);
			this.rec4.setColor(Color.MAGENTA);
			
			if (this.myTypeface != null) {
			    this.txtp.setTypeface(this.myTypeface);
		    }
			
			if(level>49) max=49;
		    if(level>74) max=74;
		    if(level>99) max=99;

		    this.circles.setAlpha(98);
		    
		    if (this.level <= max){
		      this.rows = (this.level / 5);
		      this.rem = (this.level % 5);
		    }else{
		    	this.rows = ((-max-1 + this.level) / 5);
	  	        this.rem = ((-max-1 + this.level) % 5);
		    }
		    
		    if (this.rows == 0){
		    	this.rec1.setAlpha(10);
		    	this.rec2.setAlpha(10);
		    	this.rec3.setAlpha(10);
			    this.rec4.setAlpha(10);
		    }
		    else if (this.rows == 1){
		        this.rec2.setAlpha(10);
		        this.rec3.setAlpha(10);
		        this.rec4.setAlpha(10);
		    }
		    else if (this.rows == 2){
		        this.rec3.setAlpha(10);
		        this.rec4.setAlpha(10);
		    }
		    else if (this.rows == 3) {
		        this.rec4.setAlpha(10);
		    }
		    
		    paramCanvas.drawRect(this.margin / 2, imgWidth * 1 + 1 * this.margin, this.x - this.margin / 2, imgWidth * 2 + 1 * this.margin, this.rec1);
		    paramCanvas.drawRect(this.margin / 2, imgWidth * 2 + 2 * this.margin, this.x - this.margin / 2, imgWidth * 3 + 2 * this.margin, this.rec2);
		    paramCanvas.drawRect(this.margin / 2, imgWidth * 3 + 3 * this.margin, this.x - this.margin / 2, imgWidth * 4 + 3 * this.margin, this.rec3);
		    paramCanvas.drawRect(this.margin / 2, imgWidth * 4 + 4 * this.margin, this.x - this.margin / 2, imgWidth * 5 + 4 * this.margin, this.rec4);  
	    
	    	for (int j=0; j<=rows; j++){

		    	for (int m=0; m<5; m++){
		    		int rem2=rem;
	    			if(rows!=j)rem2 = 5;
		    		if (m <= rem2) {
		    			paramCanvas.drawBitmap(img, m * ((this.x - this.margin) / 5) + this.margin / 2,
		    					imgWidth * (1 + j) + this.margin * (1 + j), this.circles);
		    			if (this.level <= max) {
		  		  	  	      paramCanvas.drawText((m + 5 * j +1)+"", m * ((this.x - this.margin) / 5) + this.margin / 2 + imgWidth / 2,
		  	  	  	    		  (float) (25+ (imgWidth * (1 + j) + this.margin * (1 + j) + txtpY)), this.txtp);
		  		   	    }else{
		  		  	  	      paramCanvas.drawText( (max+2 + (m + 5 * j))+"", m * ((this.x - this.margin) / 5) + this.margin / 2 + imgWidth / 2,
		  		  	     		  (float) (25+(imgWidth * (1 +j) + this.margin * (1 +j) + txtpY)), this.txtp);
		  		  	    }
		    		}	    			
		    	}
	    	}

		    if (this.level < this.lvl) {
		       this.level = (1 + this.level);
		       invalidate();
		    }
	  }
	  
	  public int getSelectedLevel(float x, float y){
		  int row = 0, col=0;
		  if(y>(imgWidth * 1 + 1 * this.margin) && y<(imgWidth * 2 + 1 * this.margin)) row =1;
		  else if(y>(imgWidth * 2 + 2 * this.margin) && y<(imgWidth * 3 + 2 * this.margin)) row =2;
		  else if(y>(imgWidth * 3 + 3 * this.margin) && y<(imgWidth * 4 + 3 * this.margin)) row =3;
		  else if(y>(imgWidth * 4 + 4 * this.margin) && y<(imgWidth * 5 + 4 * this.margin)) row =4;
		  else if(y>(imgWidth * 5 + 5 * this.margin) && y<(imgWidth * 6 + 5 * this.margin)) row =5;
		  if(x> (0 * ((this.x - this.margin) / 5)) && x<(1 * ((this.x - this.margin) / 5))) col=1;
		  if(x> (1 * ((this.x - this.margin) / 5)) && x<(2 * ((this.x - this.margin) / 5))) col=2;
		  if(x> (2 * ((this.x - this.margin) / 5)) && x<(3 * ((this.x - this.margin) / 5))) col=3;
		  if(x> (3 * ((this.x - this.margin) / 5)) && x<(4 * ((this.x - this.margin) / 5))) col=4;
		  if(x> (4 * ((this.x - this.margin) / 5)) && x<(5 * ((this.x - this.margin) / 5))) col=5;
		  if(row*col==0)return-1;
		  else if(level<=24)return (row*5)+col-5;
		  else return (row*5)+col-5+max+1;
	  }
}