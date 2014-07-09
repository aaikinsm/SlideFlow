package com.example.slideflow;

public class GameLogic {
	int numBlocks=0, gridSize=0, tileSize, end, base=7, fiveInRow=0;
	int[][] blockData, gridData;
	public GameLogic(int numBlocks, int gridSize, int tileSize){
		this.numBlocks = numBlocks;
		this.gridSize = gridSize;
		this.tileSize = tileSize;
		gridData = new int[gridSize][gridSize];
		end= gridSize-1;
	}
	
	public String check(int[][] data){
		String out = "";
		updateBlockData(data);		
		if(pathCleared(0,0)){
			optimizePath();
			for (int i = 0; i<gridSize; i++){
				for (int j = 0; j<gridSize; j++){
					if(gridData[i][j]>base){
						if((j>0 && j<gridSize-1) && gridData[i][j+1]>base && gridData[i][j-1]>base) gridData[i][j]=base+2;//out+= " -";
						else if((i>0 && i<gridSize-1) && gridData[i-1][j]>base && gridData[i+1][j]>base) gridData[i][j]=base+4;//out+= " |";
						else if((j>0 && i<gridSize-1) && gridData[i][j-1]>base && gridData[i+1][j]>base) gridData[i][j]=base+1; //out+= " 7";
						else if((i==gridSize-1 && j==end) && gridData[i][j-1]>base) gridData[i][j]=base+1; //out+= " 7";
						else if((i>0 && j<gridSize-1) && gridData[i-1][j]>base && gridData[i][j+1]>base) gridData[i][j]=base+6;//out+= " L";
						else if((i==0 && j==0) && gridData[i][j+1]>base) gridData[i][j]=base+6;//out+= " L";
						else if((j>0 && i>0) && gridData[i-1][j]>base && gridData[i][j-1]>base) gridData[i][j]=base+5;//out+= " )";
						else if((j<gridSize-1 && i<gridSize-1) && gridData[i][j+1]>base && gridData[i+1][j]>base) gridData[i][j]=base+3;//out+= " (";	
						else if (i==0 && j==0) gridData[i][j]=base+7;//out+= " S";
						else if (i==gridSize-1 && j==end) gridData[i][j]=base+8;//out+= " E";						
					}
				}
			}
			
			out="cleared"; return out;
		};
		
		updateBlockData(data);
		if(fiveInRow()){	
			out="five"; return out;
		}
		
		updateBlockData(data);
		if(fourInRow()){	
			out="four"; return out;
		}
		
		updateBlockData(data);
		if(threeInRow()){	
			out="three"; return out;
		}
		return out;
	}
	
	public void optimizePath(){
		for (int i = gridSize-1; i>=0; i--){
			for (int j = gridSize-1; j>=0; j--){
				if((j<gridSize-1 && i<gridSize-1) && gridData[i][j]>base && gridData[i][j+1]>base && gridData[i+1][j+1]>base && gridData[i+1][j]>base){
					gridData[i+1][j] = 0; gridData[i+1][j+1] = 0;
				}
			}
		}
	}
	
	public void updateBlockData(int[][] data){
		for (int i = 0; i<gridSize; i++){
			for (int j = 0; j<gridSize; j++){
				for (int k = 0; k<numBlocks; k++){
					gridData[j][i]=0;
					if(data[k][0]/tileSize == i && data[k][1]/tileSize == j){ 
						gridData[j][i]=data[k][2]+1; break;
					}
				}
			}
		}
	}
	
	public boolean pathCleared(int row, int column){
		boolean done = false;
	      
	      if (valid (row, column)) {
	         gridData[row][column] = 3;  // cell has been tried

	         if (row == gridSize-1 && column == end)
	            done = true;  // maze is pathClearedd
	         else {
	            done = pathCleared (row+1, column);  // down
	            if (!done)
	               done = pathCleared (row, column+1);  // right
	            if (!done)
	               done = pathCleared (row-1, column);  // up
	            if (!done)
	               done = pathCleared (row, column-1);  // left
	         }
	         if (done)  // part of the final path
	            gridData[row][column] = 9;
	      }
	      
	      return done;
	}
	
	private boolean valid (int row, int column) {
	      boolean result = false; 
	      // check if cell is in the bounds of the matrix
	      if (row >= 0 && row < gridSize &&
	          column >= 0 && column < gridData[0].length)
	         //  check if cell is not blocked and not previously tried
	         if (gridData[row][column] == 0)
	            result = true;
	      return result;
	}
	
	public boolean threeInRow(){
		for(int i = 0; i<gridSize; i++){
			for(int j = 0; j<gridSize; j++){
				if(gridData[i][j]==0) {}//do nothing
				else if((j<gridSize-1 && gridData[i][j+1]==gridData[i][j]) && ( j>0 && gridData[i][j]==gridData[i][j-1])){
					gridData[i][j+1] = 7; gridData[i][j] = 10; gridData[i][j-1] = 7;
					return true;
				}
				else if((i<gridSize-1 && gridData[i+1][j]==gridData[i][j]) && (i>0 && gridData[i][j]==gridData[i-1][j])){
					gridData[i+1][j] = 7; gridData[i][j] = 10; gridData[i-1][j] = 7;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean fourInRow(){
		for(int i = 0; i<gridSize; i++){
			for(int j = 0; j<gridSize; j++){
				if(gridData[i][j]==0) {}//do nothing
				else if(j<gridSize-3 && (gridData[i][j+1]==gridData[i][j]) && (gridData[i][j+2]==gridData[i][j]) && (gridData[i][j+3]==gridData[i][j])){
					gridData[i][j] = 10; gridData[i][j+1] = 9; gridData[i][j+2] = 9; gridData[i][j+3] = 9; gridData[i][j+3] = 9;
					return true;
				}
				else if(i<gridSize-3 && (gridData[i+1][j]==gridData[i][j]) && (gridData[i+2][j]==gridData[i][j]) && (gridData[i+3][j]==gridData[i][j])){
					gridData[i][j] = 8; gridData[i+1][j] = 9; gridData[i+2][j] = 9; gridData[i+3][j] = 9;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean fiveInRow(){
		for(int i = 0; i<gridSize; i++){
			for(int j = 0; j<gridSize; j++){
				if(gridData[i][j]==0) {}//do nothing
				else if(j<gridSize-4 && (gridData[i][j+1]==gridData[i][j]) && (gridData[i][j+2]==gridData[i][j]) && (gridData[i][j+3]==gridData[i][j]) && (gridData[i][j+4]==gridData[i][j])){				
					fiveInRow = gridData[i][j];
					return true;
				}
				else if(i<gridSize-4 && (gridData[i+1][j]==gridData[i][j]) && (gridData[i+2][j]==gridData[i][j]) && (gridData[i+3][j]==gridData[i][j]) && (gridData[i+4][j]==gridData[i][j])){				
					fiveInRow = gridData[i][j];
					return true;
				}
			}
		}
		return false;
	}
	
	public int[][] getGrid(){
		return gridData;
	}
}
