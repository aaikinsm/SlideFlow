package com.example.slideflow;


import java.io.InputStream;
import java.util.Scanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SlideView extends View{
	Bitmap [] pic = new Bitmap[16], explosion = new Bitmap[13], electric = new Bitmap[13];
	boolean moved = false, initial=true, gameOver, animate=false;
	int x=0,y=0, wDir=0, hDir=0, speed=15, count=0, block=0, MAX=100, numBlocks=0, base=7, maxMoves=5,
			 width, height, gridSize=5, tileSize, padding=1;
	int[][] blockData = new int[MAX][3], animData;
	Matrix matrix = new Matrix();
	GameLogic compute;
	public SlideView (Context context){
		super (context);		
	}
    
    public SlideView(Context context, AttributeSet attrs) { 
		super( context, attrs );
	} 
	
	public SlideView(Context context, AttributeSet attrs, int defStyle) { 
		super( context, attrs, defStyle );
	}
	
	@Override
    public void onMeasure(int x, int y){
    	super.onMeasure(x,y);
    	int newSize = x-padding;
    	setMeasuredDimension(newSize,newSize);
    }
	
	Paint paint = new Paint(), paint2 = new Paint(), paint3 = new Paint();
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		width = canvas.getWidth();
		tileSize = width/gridSize;
		
		// move selected block in swipe direction		
		if (moved && !initial){
			blockData[block][0]+=speed*wDir;
			blockData[block][1]+=speed*hDir;
			if((blockData[block][0]%(tileSize)<speed)&&(blockData[block][1]%(tileSize)<speed)){
				blockData[block][0]-=blockData[block][0]%(tileSize);
				blockData[block][1]-=blockData[block][1]%(tileSize);			
				hDir=0; wDir=0;
				if(compute.check(blockData).equals("cleared")) gameOver();
				moved=false;
				if(compute.check(blockData).equals("five")){ 
					remove(5); //count-=2;
				}
				else if(compute.check(blockData).equals("four")){ 
					remove(4); //count-=1;
				}
				else if(compute.check(blockData).equals("three")){ 
					remove(3);
				}
				else count++;
			}
		}
		
		//set block type, gridSize, and coordinates
		if (initial){
			pic[1] = BitmapFactory.decodeResource(getResources(), R.drawable.green);
			pic[2] = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
			pic[3] = BitmapFactory.decodeResource(getResources(), R.drawable.red);
			pic[4] = BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
			pic[5] = BitmapFactory.decodeResource(getResources(), R.drawable.torquise);
			pic[6] = BitmapFactory.decodeResource(getResources(), R.drawable.block);
			pic[7] = BitmapFactory.decodeResource(getResources(), R.drawable.block);
			pic[8] = BitmapFactory.decodeResource(getResources(), R.drawable.left_down);
			pic[9] = BitmapFactory.decodeResource(getResources(), R.drawable.left_right);
			pic[10] = BitmapFactory.decodeResource(getResources(), R.drawable.right_down);
			pic[11] = BitmapFactory.decodeResource(getResources(), R.drawable.up_down);
			pic[12] = BitmapFactory.decodeResource(getResources(), R.drawable.up_left);
			pic[13] = BitmapFactory.decodeResource(getResources(), R.drawable.up_right);
			pic[14] = BitmapFactory.decodeResource(getResources(), R.drawable.start);
			pic[15] = BitmapFactory.decodeResource(getResources(), R.drawable.end);
			for(int i=1; i<16; i++){
				try{
					pic[i] = Bitmap.createScaledBitmap(pic[i], tileSize, tileSize, false);
				}catch(NullPointerException e){}
			}
			explosion[1] = BitmapFactory.decodeResource(getResources(), R.drawable.e12);
			explosion[2] = BitmapFactory.decodeResource(getResources(), R.drawable.e11);
			explosion[3] = BitmapFactory.decodeResource(getResources(), R.drawable.e10);
			explosion[4] = BitmapFactory.decodeResource(getResources(), R.drawable.e9);
			explosion[5] = BitmapFactory.decodeResource(getResources(), R.drawable.e8);
			explosion[6] = BitmapFactory.decodeResource(getResources(), R.drawable.e7);
			explosion[7] = BitmapFactory.decodeResource(getResources(), R.drawable.e6);
			explosion[8] = BitmapFactory.decodeResource(getResources(), R.drawable.e5);
			explosion[9] = BitmapFactory.decodeResource(getResources(), R.drawable.e4);
			explosion[10] = BitmapFactory.decodeResource(getResources(), R.drawable.e3);
			explosion[11] = BitmapFactory.decodeResource(getResources(), R.drawable.e2);
			explosion[12] = BitmapFactory.decodeResource(getResources(), R.drawable.e1);
			for(int i=1; i<13; i++){
				try{
					explosion[i] = Bitmap.createScaledBitmap(explosion[i], tileSize+(tileSize/2), tileSize+(tileSize/2), false);
				}catch(NullPointerException e){}
			}
			electric[1] = BitmapFactory.decodeResource(getResources(), R.drawable.l12);
			electric[2] = BitmapFactory.decodeResource(getResources(), R.drawable.l11);
			electric[3] = BitmapFactory.decodeResource(getResources(), R.drawable.l10);
			electric[4] = BitmapFactory.decodeResource(getResources(), R.drawable.l9);
			electric[5] = BitmapFactory.decodeResource(getResources(), R.drawable.l8);
			electric[6] = BitmapFactory.decodeResource(getResources(), R.drawable.l7);
			electric[7] = BitmapFactory.decodeResource(getResources(), R.drawable.l6);
			electric[8] = BitmapFactory.decodeResource(getResources(), R.drawable.l5);
			electric[9] = BitmapFactory.decodeResource(getResources(), R.drawable.l4);
			electric[10] = BitmapFactory.decodeResource(getResources(), R.drawable.l3);
			electric[11] = BitmapFactory.decodeResource(getResources(), R.drawable.l2);
			electric[12] = BitmapFactory.decodeResource(getResources(), R.drawable.l1);
			for(int i=1; i<13; i++){
				try{
					electric[i] = Bitmap.createScaledBitmap(electric[i], tileSize+(tileSize/2), tileSize*4, false);
				}catch(NullPointerException e){}
			}
			for(int i=0; i<numBlocks; i++){
				for(int j=0; j<2; j++){
					blockData[i][j]*=tileSize;
				}				
			}
			compute = new GameLogic(numBlocks, gridSize, tileSize);
			animData = new int[gridSize][gridSize];
			paint3.setAlpha(50);
			matrix.postRotate(90);
			initial = false;
		} 
		
		//draw grid
		for(int i=1; i<gridSize; i++){
			canvas.drawLine(tileSize*i, 0, tileSize*i, width, paint3);
			canvas.drawLine(0,tileSize*i, width, tileSize*i, paint3);
		}
		
		
		//draw all blocks
		for(int i=0; i<numBlocks; i++){
			canvas.drawBitmap(pic[blockData[i][2]], blockData[i][0], blockData[i][1], paint2);
		}
		
		//draw animation
		if(animate && !gameOver){
			for (int i=0; i<gridSize; i++){
				for (int j=0; j<gridSize; j++){
					if(animData[i][j]>0 && animData[i][j]<13){
						canvas.drawBitmap(explosion[animData[i][j]], j*tileSize-(tileSize/4), i*tileSize-(tileSize/4), paint);
						animData[i][j]--;
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) { e.printStackTrace();}
					}else if(animData[i][j]>13 && animData[i][j]<25){
						canvas.drawBitmap(electric[animData[i][j]/2], j*tileSize-(tileSize/4), i*tileSize, paint);
						animData[i][j]--;
						try {
							Thread.sleep(40);
						} catch (InterruptedException e) { e.printStackTrace();}
					}else if(animData[i][j]>25 && animData[i][j]<37){
						
						Bitmap rotatedBitmap = Bitmap.createBitmap(electric[animData[i][j]/3] , 0, 0,electric[animData[i][j]/3].getWidth(), electric[animData[i][j]/3].getHeight(), matrix, true);
						canvas.drawBitmap(rotatedBitmap, j*tileSize, i*tileSize-(tileSize/4), paint);
						animData[i][j]--;
						try {
							Thread.sleep(40);
						} catch (InterruptedException e) { e.printStackTrace();}
					}
				}
			}
		}
		
		if(gameOver){
			//canvas.drawText(count+"/"+maxMoves, width/2,width/2, txtp);
		}
		else invalidate();
	}
	
	public void setDirection(String dir){
		if(!moved && (block!=MAX-1)){
			if(dir.equals("up")) hDir=-1; 
			else if (dir.equals("down")) hDir=1;
			else if (dir.equals("left")) wDir=-1;
			else if (dir.equals("right")) wDir=1;
			if (collision()){ 
				hDir=0; wDir=0;
			}
			else moved = true;
		}
	}
	
	public void selectBlock(double mx, double my){
		if(!moved){
			for(int i=0; i<numBlocks; i++){
				if((blockData[i][0]<mx && blockData[i][0]+tileSize>mx)&& (blockData[i][1]<my && blockData[i][1]+tileSize>my)){ 
					block=i;
					break;
				}else block=MAX-1;
			}
		}
	}
	
	public boolean collision(){
		if(blockData[block][2]==6 || gameOver) return true;
		int mx=(blockData[block][0]/tileSize)+wDir;
		int my=(blockData[block][1]/tileSize)+hDir;
		if(mx==gridSize || mx==-1 || my==gridSize || my==-1) return true;
		for(int i=0; i<numBlocks; i++){
			if((blockData[i][0]/tileSize == mx) && (blockData[i][1]/tileSize == my) && block != i){ 
				return true;
			}
		}return false;
	}
	
	//set block type, gridSize, and coordinates
	public void loadData(int level){		
		InputStream ins = getResources().openRawResource(
	            getResources().getIdentifier("game_data",
	            "raw", "com.example.slideflow"));
		Scanner data = new Scanner(ins);
		while(true){
			String str = data.nextLine();
			for(int k=0; k<(level-1)*4; k++) str = data.nextLine();
			int moves = Integer.parseInt(str.substring(0,str.indexOf(" ")));
			maxMoves+=moves;
			gridSize = data.nextInt();
			numBlocks = data.nextInt();
			for (int i =0; i <numBlocks; i++){
				for (int j =0; j <3; j++){
					blockData[i][j] = data.nextInt();
				}
			}
			data.close();
			break;
		}
	}
	
	public void remove(int num){
		int[][] tempGrid = compute.getGrid();
		for (int i=0; i<gridSize; i++){
			for (int j=0; j<gridSize; j++){
				if((num==5 && tempGrid[i][j]==compute.fiveInRow) || (tempGrid[i][j]>base)){
					selectBlock(j*tileSize+(tileSize/2),i*tileSize+(tileSize/2));
					//remove blocks
					blockData[block][0] = -2*tileSize;
					blockData[block][1] = -2*tileSize;
					//explosion animation
					if(num==3 || num==5){ 
						animData[i][j] = 12; 					
					}
					//horizontal electric animation
					else if (tempGrid[i][j]==10){ 
						animData[i][j] = 36;
					}
					//vertical electric animation
					else if (tempGrid[i][j]==8){ 
						animData[i][j] = 24;
					}
					animate=true;
				}
			}
		}
		moved=true;
	}
	
	public void gameOver(){
		int[][] tempGrid = compute.getGrid();
		for (int i=0; i<gridSize; i++){
			for (int j=0; j<gridSize; j++){
				if(tempGrid[i][j]>base){
					blockData[numBlocks][0] = j*tileSize;
					blockData[numBlocks][1] = i*tileSize;
					blockData[numBlocks][2] = tempGrid[i][j];
					numBlocks++;
					moved=true;
					gameOver=true;
				}
			}
		}
	}
	
	
	
	
	@Override 
	public boolean onTouchEvent(MotionEvent event) {   
		switch (event.getAction()) {         
			case MotionEvent.ACTION_DOWN: 
			case MotionEvent.ACTION_MOVE:         
			case MotionEvent.ACTION_UP:
			
		} return false; 
	}
	
}
